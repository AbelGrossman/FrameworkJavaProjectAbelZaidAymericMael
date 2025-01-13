package fr.pantheonsorbonne.ufr27.miage.service;

import java.util.List;

import fr.pantheonsorbonne.ufr27.miage.dao.LobbyDAO;
import fr.pantheonsorbonne.ufr27.miage.model.Lobby;
import fr.pantheonsorbonne.ufr27.miage.model.Player;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class LobbyService {
    @Inject
    LobbyDAO lobbyDAO;

    public Lobby createLobby(String theme, String level, List<Player> players) {
        Lobby lobby = new Lobby();
        lobby.setTheme(theme);
        lobby.setLevel(level);
        lobby.setPlayers(players);
        lobbyDAO.saveLobby(lobby);
        return lobby;
    }
}
