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

/**
 * REST Controller for handling authentication-related requests.
 * <p>
 * This class exposes endpoints for user registration and login.
 * It acts as an entry point for external clients (Flutter app)
 * to interact with the authentication logic.
 * </p>
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    /**
     * Constructor injection for the authentication service.
     * @param authService The service containing the business logic for login/register.
     */
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint for user registration.
     * <p>
     * Accepts a POST request with user details, creates a new account,
     * and returns a JWT token for immediate access.
     * </p>
     * @param req The data transfer object (DTO) containing username, email, and password.
     * Mapped automatically from the JSON body of the request.
     * @return A ResponseEntity containing the JWT token wrapped in an AuthResponse object.
     */
    @PostMapping("/register")
    public AuthResponse register(@RequestBody @Valid RegisterRequest req) {
        return new AuthResponse(authService.register(req));
    }

    /**
     * Endpoint for user login.
     * <p>
     * Accepts a POST request with credentials, verifies them, and returns a JWT token.
     * </p>
     * @param req The DTO containing the username and password provided by the user.
     * @return A ResponseEntity containing the JWT token if credentials are valid.
     */
    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest req) {
        return new AuthResponse(authService.login(req));
    }
}
