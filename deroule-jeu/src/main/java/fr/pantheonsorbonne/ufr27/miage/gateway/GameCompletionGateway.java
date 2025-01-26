package fr.pantheonsorbonne.ufr27.miage.gateway;

import fr.pantheonsorbonne.ufr27.miage.dto.PlayerResultsRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.camel.ProducerTemplate;

import java.util.List;

@ApplicationScoped
public class GameCompletionGateway {
    @Inject
    ProducerTemplate producerTemplate;

    public void handleGameCompletion(Long gameId, List<PlayerResultsRequest> playerResults, String teamId) {
        producerTemplate.sendBodyAndHeader("direct:endGame", playerResults, "gameId", gameId);
    }
}