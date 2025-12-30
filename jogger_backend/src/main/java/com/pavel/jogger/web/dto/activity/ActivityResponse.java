package com.pavel.jogger.web.dto.activity;

import java.time.LocalDate;

public class ActivityResponse {

    private Long id;
    private double distanceKm;
    private int durationSec;
    private int calories;
    private LocalDate date;
    private String route;
    private double pace;

    public ActivityResponse(Long id, double distanceKm, int durationSec,
                            int calories, LocalDate date, String route, double pace) {
        this.id = id;
        this.distanceKm = distanceKm;
        this.durationSec = durationSec;
        this.calories = calories;
        this.date = date;
        this.route = route;
        this.pace = pace;
    }

    public Long getId() { return id; }
    public double getDistanceKm() { return distanceKm; }
    public int getDurationSec() { return durationSec; }
    public int getCalories() { return calories; }
    public LocalDate getDate() { return date; }
    public String getRoute() { return route; }
    public double getPace() { return pace; }
}
