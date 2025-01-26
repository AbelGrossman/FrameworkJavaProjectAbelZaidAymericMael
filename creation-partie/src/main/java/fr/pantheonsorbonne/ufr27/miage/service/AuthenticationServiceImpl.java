package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dao.PlayerDao;
import fr.pantheonsorbonne.ufr27.miage.model.Player;
import fr.pantheonsorbonne.ufr27.miage.dto.LoginRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.RegisterRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.AuthResponse;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@ApplicationScoped
public class AuthenticationServiceImpl implements AuthenticationService {

    @Inject
    PlayerDao playerDao;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (playerDao.existsByUsername(request.username())) {
            throw new RuntimeException("Username already exists");
        }

        Player player = new Player();
        player.setUsername(request.username());
        player.setPassword(hashPassword(request.password()));

        playerDao.persist(player);

        String token = generateToken(player);
        return new AuthResponse(player.getId(), player.getUsername(), token);
    }

    public AuthResponse login(LoginRequest request) {
        Player player = playerDao.findByUsername(request.username())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!verifyPassword(request.password(), player.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = generateToken(player);
        return new AuthResponse(player.getId(), player.getUsername(), token);
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    private boolean verifyPassword(String inputPassword, String storedHash) {
        String inputHash = hashPassword(inputPassword);
        return inputHash.equals(storedHash);
    }

    private String generateToken(Player player) {
        // In a real application, use JWT or similar
        return Base64.getEncoder().encodeToString(
                (player.getUsername() + ":" + System.currentTimeMillis()).getBytes()
        );
    }
}