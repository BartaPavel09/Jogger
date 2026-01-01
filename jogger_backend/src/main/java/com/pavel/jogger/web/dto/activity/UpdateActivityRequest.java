package com.pavel.jogger.web.dto.activity;

import jakarta.validation.constraints.Positive;

/**
 * DTO for updating an existing activity.
 * <p>
 * "date" is missing. Usually, we don't allow changing the date of a past run,
 * just the details like distance or calories if the watch synced wrongly.
 * </p>
 */
public class UpdateActivityRequest {

    @Positive(message = "Distance must be positive")
    private double distanceKm;

    @Positive(message = "Duration must be positive")
    private int durationSec;

    private String route;

    private Integer calories;

    public UpdateActivityRequest() {}

    public double getDistanceKm() {
        return distanceKm;
    }

    public int getDurationSec() {
        return durationSec;
    }

    public String getRoute() {
        return route;
    }

    public Integer getCalories() {
        return calories;
    }
}
