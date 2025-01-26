package fr.pantheonsorbonne.ufr27.miage.service;

import fr.pantheonsorbonne.ufr27.miage.dto.AuthResponse;
import fr.pantheonsorbonne.ufr27.miage.dto.LoginRequest;
import fr.pantheonsorbonne.ufr27.miage.dto.RegisterRequest;
import fr.pantheonsorbonne.ufr27.miage.exception.InvalidCredentialsException;
import fr.pantheonsorbonne.ufr27.miage.exception.UsernameAlreadyExistsException;

public interface AuthenticationService {
    AuthResponse login(LoginRequest request) throws InvalidCredentialsException;
    AuthResponse register(RegisterRequest request) throws UsernameAlreadyExistsException;

}
