package fr.pantheonsorbonne.ufr27.miage.dto;

public record StatistiquesResponse(
        Long playerId,
        String theme,
        Integer mmr
) {
}