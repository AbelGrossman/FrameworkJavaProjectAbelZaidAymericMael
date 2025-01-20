package fr.pantheonsorbonne.ufr27.miage.dao;
import fr.pantheonsorbonne.ufr27.miage.dto.User;

public class PlayerDao {
    public User fetchPlayerById(String playerId) {
        return new User(playerId, "default_theme", 1500); // mock
    }
}
