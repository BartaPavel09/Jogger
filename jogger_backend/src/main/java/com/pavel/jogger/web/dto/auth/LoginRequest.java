package com.pavel.jogger.web.dto.auth;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for the login request.
 * <p>
 * Defines the structure of the JSON body expected by the /auth/login endpoint.
 * </p>
 */
public class LoginRequest {

    /**
     * The username of the user trying to log in.
     */
    @NotBlank(message = "Username cannot be empty")
    public String username;

    /**
     * The raw password entered by the user.
     */
    @NotBlank(message = "Password cannot be empty")
    public String password;
}