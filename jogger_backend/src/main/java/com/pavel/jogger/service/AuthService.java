package com.pavel.jogger.service;

import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.repository.RunnerRepository;
import com.pavel.jogger.security.JwtService;
import com.pavel.jogger.web.dto.auth.LoginRequest;
import com.pavel.jogger.web.dto.auth.RegisterRequest;
import com.pavel.jogger.web.exception.ConflictException;
import com.pavel.jogger.web.exception.NotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service class handling core authentication logic.
 * <p>
 * This class acts as the bridge between the API layer (Controllers) and the data/security layers.
 * It handles user registration (saving new users safely) and user login (verifying credentials).
 * </p>
 */
@Service
public class AuthService {

    private final RunnerRepository runnerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    /**
     * Constructor injection for required dependencies.
     * @param runnerRepository      To access and save user data in the database.
     * @param passwordEncoder       To hash passwords before saving them (security best practice).
     * @param authenticationManager To verify username/password combinations during login.
     * @param jwtService            To generate the JWT token upon successful auth.
     */
    public AuthService(
            RunnerRepository runnerRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService
    ) {
        this.runnerRepository = runnerRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    /**
     * Registers a new user (Runner) in the system.
     * @param req The registration request containing username, email, and raw password.
     * @return A valid JWT token so the user is immediately logged in after registration.
     * @throws ConflictException If the username or email is already taken.
     */
    public String register(RegisterRequest req) {

        if (runnerRepository.existsByUsername(req.username)) {
            throw new ConflictException("Username already exists");
        }
        if (runnerRepository.existsByEmail(req.email)) {
            throw new ConflictException("Email already exists");
        }

        RunnerEntity runner = new RunnerEntity();
        runner.setUsername(req.username);
        runner.setEmail(req.email);
        runner.setPasswordHash(passwordEncoder.encode(req.password));
        runner.setRole("USER");
        runner.setDateJoined(LocalDateTime.now());

        runnerRepository.save(runner);

        return jwtService.generateToken(runner.getUsername(), runner.getRole());
    }

    /**
     * Authenticates an existing user.
     * @param req The login request containing username and raw password.
     * @return A valid JWT token if credentials are correct.
     * @throws org.springframework.security.core.AuthenticationException If login fails.
     */
    public String login(LoginRequest req) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username, req.password)
        );

        RunnerEntity runner = runnerRepository.findByUsername(req.username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return jwtService.generateToken(runner.getUsername(), runner.getRole());
    }
}
