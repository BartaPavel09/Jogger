package com.pavel.jogger.web.dto.activity;

import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

/**
 * DTO for updating an existing activity.
 */
public class UpdateActivityRequest {

    @Positive(message = "Distance must be positive")
    private double distanceKm;

    @Positive(message = "Duration must be positive")
    private int durationSec;

    private LocalDate date;

    private String route;

    private Integer calories;

    public UpdateActivityRequest() {}

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
