package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.model.Queue;
import fr.pantheonsorbonne.ufr27.miage.dto.User;

import jakarta.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ApplicationScoped
public class QueueManager {
    private final Map<String, List<Queue>> queues = new HashMap<>();
    private final int queueSizeLimit = 50;

    public synchronized Queue getOrCreateQueue(String theme) {
        queues.putIfAbsent(theme, new ArrayList<>());
        List<Queue> themeQueues = queues.get(theme);

        // Get the first non-full queue or create a new one
        for (Queue queue : themeQueues) {
            if (!queue.isFull()) {
                return queue;
            }
        }

        // Create a new queue if all are full
        Queue newQueue = new Queue(theme);
        themeQueues.add(newQueue);
        return newQueue;
    }

    public synchronized void addPlayerToQueue(User user) {
        Queue queue = getOrCreateQueue(user.getTheme());
        queue.addPlayer(user);
        sortQueue(queue);
    }

    private void sortQueue(Queue queue) {
        queue.getPlayers().sort(Comparator.comparingInt(User::getMmr));
    }

    public synchronized List<User> findTeam(String theme, int allowedMmrDifference) {
        List<Queue> themeQueues = queues.getOrDefault(theme, new ArrayList<>());
        for (Queue queue : themeQueues) {
            List<User> players = queue.getPlayers();
            List<User> team = new ArrayList<>();

            for (User player : players) {
                if (team.isEmpty() || Math.abs(team.get(0).getMmr() - player.getMmr()) <= allowedMmrDifference) {
                    team.add(player);
                    if (team.size() == 6) {
                        queue.getPlayers().removeAll(team);
                        return team;
                    }
                }
            }
        }
        return new ArrayList<>();
    }
}
