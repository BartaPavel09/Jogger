package com.pavel.jogger.web.dto.runner;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

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
