package com.pavel.jogger.web.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, message = "Username must have at least 3 characters")
    public String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    public String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must have at least 6 characters")
    public String password;
}
