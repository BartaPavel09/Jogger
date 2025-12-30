package com.pavel.jogger.web.controller;

import com.pavel.jogger.service.AuthService;
import com.pavel.jogger.web.dto.auth.AuthResponse;
import com.pavel.jogger.web.dto.auth.LoginRequest;
import com.pavel.jogger.web.dto.auth.RegisterRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody @Valid RegisterRequest req) {
        return new AuthResponse(authService.register(req));
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest req) {
        return new AuthResponse(authService.login(req));
    }
}
