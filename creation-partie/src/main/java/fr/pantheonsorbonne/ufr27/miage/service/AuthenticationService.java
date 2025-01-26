package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dto.AuthResponse;
import fr.pantheonsorbonne.ufr27.miage.dto.LoginRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.RegisterRequest;

public interface AuthenticationService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);

}
