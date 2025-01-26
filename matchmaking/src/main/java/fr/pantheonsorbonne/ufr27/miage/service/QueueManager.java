package fr.pantheonsorbonne.ufr27.miage.service;

import java.util.List;
import java.util.Map;

import fr.pantheonsorbonne.ufr27.miage.dto.UserWithMmr;
import fr.pantheonsorbonne.ufr27.miage.model.Queue;
import jakarta.ws.rs.core.Response;

public interface QueueManager {

    public Queue getOrCreateQueue(String theme);

    public Response addPlayerToQueue(UserWithMmr user);

    public void formTeams(String theme);

    public void adjustMmrDifferencePeriodically();

    public List<String> getThemes();

    public Map<String, Queue> getQueues();

    public Map<String, Long> getLastTeamFormedTime();

    public void removePlayerFromQueue(Long userId);
}
