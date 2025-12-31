package com.pavel.jogger.web.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for the registration request.
 * <p>
 * Defines the structure of the JSON body expected by the /auth/register endpoint.
 * </p>
 */
public class RegisterRequest {

    /**
     * The desired username for the new account.
     */
    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, message = "Username must have at least 3 characters")
    public String username;

    /**
     * The user's email address.
     */
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    public String email;

    /**
     * The password for the new account.
     * This will be hashed via BCrypt before saving to the database.
     */
    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must have at least 6 characters")
    public String password;
}
