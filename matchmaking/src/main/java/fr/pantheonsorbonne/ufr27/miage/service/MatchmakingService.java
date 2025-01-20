package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.model.Queue;
import fr.pantheonsorbonne.ufr27.miage.dto.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MatchmakingService {
    private final Map<String, Queue> themeQueues = new HashMap<>();

    public void addPlayerToQueue(User user) {
        String theme = user.getTheme();
        themeQueues.putIfAbsent((theme), new Queue(theme));
        Queue queue = themeQueues.get(theme);

        if (!queue.isFull()) {
            queue.addPlayer(user);
        } else {
            throw new RuntimeException("Queue is full for theme: " + theme);
        }
    }

    public List<User> findTeam(String theme) {
        Queue queue = themeQueues.get(theme);

        if (queue == null || queue.getPlayers().isEmpty()) {
            return new ArrayList<>();
        }

        List<User> players = queue.getPlayers();
        players.sort((p1, p2) -> Integer.compare(p1.getMmr(), p2.getMmr())); // Sort by MMR

        List<User> team = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (team.size() < 6) {
                team.add(players.get(i));
            } else {
                break;
            }
        }

        queue.getPlayers().removeAll(team);
        return team;
    }
}
