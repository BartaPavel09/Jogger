package com.pavel.jogger.service;

import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.repository.RunnerRepository;
import com.pavel.jogger.security.JwtService;
import com.pavel.jogger.web.dto.auth.RegisterRequest;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private final RunnerRepository runnerRepository = mock(RunnerRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private final JwtService jwtService = mock(JwtService.class);

    private final AuthService authService = new AuthService(
            runnerRepository,
            passwordEncoder,
            authenticationManager,
            jwtService
    );

    @Test
    void register_shouldCreateUserAndReturnToken() {
        RegisterRequest req = new RegisterRequest();
        req.username = "user";
        req.email = "user@test.com";
        req.password = "pass";

        when(runnerRepository.existsByUsername("user")).thenReturn(false);
        when(runnerRepository.existsByEmail("user@test.com")).thenReturn(false);
        when(passwordEncoder.encode("pass")).thenReturn("hashed");
        when(jwtService.generateToken(any(), any())).thenReturn("TOKEN");

        String token = authService.register(req);

        assertEquals("TOKEN", token);
        verify(runnerRepository).save(any(RunnerEntity.class));
    }
}