package com.pavel.jogger.web.dto.runner;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

/**
 * DTO for updating an existing runner's profile.
 * <p>
 * This class captures the input from the "Edit Profile" screen.
 * It enforces validation rules to ensure data integrity (e.g., valid email format).
 * Note: Username is usually not changeable to maintain ID consistency.
 * </p>
 */
public class UpdateRunnerRequest {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Positive(message = "Weight must be positive")
    private Double weight;

    public UpdateRunnerRequest() {}

    public String getEmail() {
        return email;
    }

    public Double getWeight() {
        return weight;
    }
}
