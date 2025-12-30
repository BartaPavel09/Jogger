package com.pavel.jogger.web.dto.auth;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    @NotBlank(message = "Username cannot be empty")
    public String username;

    @NotBlank(message = "Password cannot be empty")
    public String password;
}