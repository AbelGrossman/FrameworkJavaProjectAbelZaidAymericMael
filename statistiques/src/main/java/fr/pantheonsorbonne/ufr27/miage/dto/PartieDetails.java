package fr.pantheonsorbonne.ufr27.miage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.pantheonsorbonne.ufr27.miage.service.StatistiquesServiceImpl;

public record PartieDetails(
        @JsonProperty("playerId") String playerId,
        @JsonProperty("score") int scorePartie,
        @JsonProperty("gameId") String gameId,
        @JsonProperty("averageResponseTime") long tempsRepMoyen,
        @JsonProperty("category") String theme,
        @JsonProperty("rank") int rangPartie,
        @JsonProperty("totalQuestions") int nbQuestions
) {

    public Long getPlayerIdConverted() {
        StatistiquesServiceImpl statistiquesService = new StatistiquesServiceImpl();
        return statistiquesService.convertPlayerId(playerId);
    }
}
