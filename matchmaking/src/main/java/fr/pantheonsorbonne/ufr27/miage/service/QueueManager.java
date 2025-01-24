package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dao.TeamDAO;
import fr.pantheonsorbonne.ufr27.miage.dto.UserWithMmr;
import fr.pantheonsorbonne.ufr27.miage.model.Queue;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import jakarta.ws.rs.*;


@Path("/queue")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class QueueManager {

    @Inject
    TeamDAO teamDAO;

    private final Map<String, Queue> queues = new HashMap<>();

    private final Map<String, Long> lastTeamFormedTime = new HashMap<>();

    private final int mmrAdjustmentInterval= 10_000;


    public QueueManager(TeamDAO teamDAO) {
        this.teamDAO = teamDAO;
    }
    
    public Queue getOrCreateQueue(String theme) {
        Queue queue = queues.get(theme);
        if (queue == null) {
            queue = new Queue(theme);
            queues.put(theme, queue);
        }
        return queue;
    }

    public Response addPlayerToQueue(UserWithMmr user) {
        Queue queue = getOrCreateQueue(user.theme());
        synchronized (queue) {
            queue.addPlayer(user);
        }
        return Response.ok().build();
    } 
    public void formTeams(@PathParam("theme") String theme) {
        Queue queue = getOrCreateQueue(theme);
    
        synchronized (queue) {
            List<UserWithMmr> players = queue.getPlayers();
            players.sort(Comparator.comparingInt(UserWithMmr::mmr));

            List<List<UserWithMmr>> teams = new ArrayList<>();
            List<UserWithMmr> currentTeam = new ArrayList<>();
            boolean teamFormed = false;
    
            for (UserWithMmr player : players) {
                if (currentTeam.size()<6 && (currentTeam.isEmpty() || 
                    player.mmr() - currentTeam.get(0).mmr() <= queue.getAllowedMmrDifference())) {
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
    
            if (currentTeam.size() == 6) {
                teams.add(new ArrayList<>(currentTeam));
                players.removeAll(currentTeam);
                teamFormed = true;
            }
    
            for (List<UserWithMmr> team : teams) {
                teamDAO.addTeamToDatabase(team);
            }
    
            if (teamFormed) {
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

    public Map<String, Queue> getQueues() {
        return queues;
    }

    public Map<String, Long> getLastTeamFormedTime() {
        return lastTeamFormedTime;
    }

    public void removePlayerFromQueue(Long userId){
        for(Queue queue : queues.values()){
            synchronized (queue) {
                List<UserWithMmr> players = queue.getPlayers();
                for(UserWithMmr player : players){
                    if(player.playerId().equals(userId)){
                        queue.removePlayer(player);
                    }
                }
            }
        }
    }

}