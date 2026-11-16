package com.pavel.jogger.web.dto.activity;

import java.time.LocalDate;

/**
 * DTO for sending activity data to the client.
 * <p>
 * This creates a clean contract with the frontend. Even if the database changes,
 * this structure stays the same so the app doesn't break.
 * </p>
 */
public class ActivityResponse {

    private Long id;
    private double distanceKm;
    private int durationSec;
    private int calories;
    private LocalDate date;
    private String route;
    private double pace;
    private Long runnerId;
    private String runnerUsername;

    public ActivityResponse(Long id, double distanceKm, int durationSec,
                            int calories, LocalDate date, String route, double pace) {
        this(id, distanceKm, durationSec, calories, date, route, pace, null, null);
    }

    public ActivityResponse(Long id, double distanceKm, int durationSec,
                            int calories, LocalDate date, String route, double pace,
                            Long runnerId, String runnerUsername) {
        this.id = id;
        this.distanceKm = distanceKm;
        this.durationSec = durationSec;
        this.calories = calories;
        this.date = date;
        this.route = route;
        this.pace = pace;
        this.runnerId = runnerId;
        this.runnerUsername = runnerUsername;
    }

    public Long getId() { return id; }
    public double getDistanceKm() { return distanceKm; }
    public int getDurationSec() { return durationSec; }
    public int getCalories() { return calories; }
    public LocalDate getDate() { return date; }
    public String getRoute() { return route; }
    public double getPace() { return pace; }
    public Long getRunnerId() { return runnerId; }
    public String getRunnerUsername() { return runnerUsername; }
}
