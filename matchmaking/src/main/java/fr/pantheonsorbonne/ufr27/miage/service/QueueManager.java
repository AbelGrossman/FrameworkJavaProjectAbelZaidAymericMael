package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.model.Queue;
import fr.pantheonsorbonne.ufr27.miage.dto.User;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@ApplicationScoped
public class QueueManager {

    @Inject
    TeamEmitter teamEmitter;

    private final Map<String, Queue> queues = new HashMap<>();
    private final Map<String, Long> lastTeamFormedTime = new HashMap<>();
    private final int mmrAdjustmentInterval= 10_000; // 10 seconds


    public QueueManager(TeamEmitter teamEmitter) {
        this.teamEmitter = teamEmitter;
    }
    
    public Queue getOrCreateQueue(String theme) {
        Queue queue = queues.get(theme);
        if (queue == null) {
            queue = new Queue(theme);
            queues.put(theme, queue);
        }
        return queue;
    }

    public void addPlayerToQueue(User user) {
        Queue queue = getOrCreateQueue(user.getTheme());
        synchronized (queue) {
            queue.addPlayer(user);
        }
    }

    public void formTeams(String theme) {
        Queue queue = getOrCreateQueue(theme);
    
        synchronized (queue) {
            List<User> players = queue.getPlayers();
            players.sort(Comparator.comparingInt(User::getMmr));
    
            List<List<User>> teams = new ArrayList<>();
            List<User> currentTeam = new ArrayList<>();
            boolean teamFormed = false;
    
            for (User player : players) {
                if (currentTeam.size()<6 && (currentTeam.isEmpty() || 
                    player.getMmr() - currentTeam.get(0).getMmr() <= queue.getAllowedMmrDifference())) {
                    currentTeam.add(player);
                } else {
                    if (currentTeam.size() == 6) {
                        teams.add(new ArrayList<>(currentTeam));
                        players.removeAll(currentTeam);
                        teamFormed = true;
                    }
                    currentTeam.clear();
                    currentTeam.add(player);
                }
            }
    
            // Final check for the last team
            if (currentTeam.size() == 6) {
                teams.add(new ArrayList<>(currentTeam));
                players.removeAll(currentTeam);
                teamFormed = true;
            }
    
            // Emit all formed teams to creation-partie
            for (List<User> team : teams) {
                teamEmitter.sendTeamToCreationPartie(team);
            }
    
            if (teamFormed) {
                // Reset allowed MMR difference and update last team formation time
                queue.setAllowedMmrDifference(20);
                lastTeamFormedTime.put(theme, System.currentTimeMillis());
            }
        }
    }

    public void adjustMmrDifferencePeriodically() {
        long currentTime = System.currentTimeMillis();

        for (Map.Entry<String, Queue> entry : queues.entrySet()) {
            String theme = entry.getKey();
            Queue queue = entry.getValue();

            synchronized (queue) {
                long lastFormedTime = lastTeamFormedTime.getOrDefault(theme, 0L);

                // If no teams were formed in the last interval, increase allowed MMR difference
                if (currentTime - lastFormedTime >= mmrAdjustmentInterval) {
                    queue.setAllowedMmrDifference(queue.getAllowedMmrDifference() + 20);
                    lastTeamFormedTime.put(theme, currentTime);
                }
            }
        }
    }

    public List<String> getThemes() {
        return new ArrayList<>(queues.keySet());
    }
}