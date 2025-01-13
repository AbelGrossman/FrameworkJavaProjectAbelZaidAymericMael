package fr.pantheonsorbonne.ufr27.miage.dto;

import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.model.Player;

public record LobbyDTO(String lobbyId, String theme, String level, List<Player> players) {}
