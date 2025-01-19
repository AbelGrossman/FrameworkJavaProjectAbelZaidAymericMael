package fr.pantheonsorbonne.ufr27.miage.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public record PlayerDTO(String playerId, String name, String level) {}
