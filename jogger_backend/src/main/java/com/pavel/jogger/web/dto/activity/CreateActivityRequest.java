package com.pavel.jogger.web.dto.activity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * DTO for creating a new activity.
 * <p>
 * This object captures the input from the "Add Run" screen in Flutter.
 * It uses Jakarta Validation annotations to ensure data integrity before
 * it even reaches the logic layer.
 * </p>
 */
public class CreateActivityRequest {

    @Positive(message = "Distance must be positive")
    private double distanceKm;

    @Positive(message = "Duration must be positive")
    private int durationSec;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private String route;

    private Integer calories;

    public CreateActivityRequest() {}

    public double getDistanceKm() {
        return distanceKm;
    }

    public int getDurationSec() {
        return durationSec;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getRoute() {
        return route;
    }

    public Integer getCalories() {
        return calories;
    }
}
