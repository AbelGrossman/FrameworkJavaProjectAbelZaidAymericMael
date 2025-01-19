package fr.pantheonsorbonne.ufr27.miage.dto;

public record JoinGameRequest(   Long playerId,
                                 String theme,
                                 Integer mmr
                                 ) {

    }