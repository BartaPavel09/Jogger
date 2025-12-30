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

@Service
public class AuthService {

    private final RunnerRepository runnerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

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

    public String login(LoginRequest req) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.username, req.password)
        );

        RunnerEntity runner = runnerRepository.findByUsername(req.username)
                .orElseThrow(() -> new NotFoundException("User not found"));

        return jwtService.generateToken(runner.getUsername(), runner.getRole());
    }
}
