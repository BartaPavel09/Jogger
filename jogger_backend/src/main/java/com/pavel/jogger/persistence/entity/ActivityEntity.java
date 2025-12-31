package com.pavel.jogger.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entity class representing a jogging activity stored in the database.
 * <p>
 * This class maps directly to the "activities" table. It holds all the metrics
 * recorded during a run (distance, duration, calories) and links them to the user who performed it.
 * </p>
 */
@Entity
@Table(name = "activities")
public class ActivityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double distanceKm;

    @Column(nullable = false)
    private int durationSec;

    @Column(nullable = false)
    private LocalDate date;

    private String route;

    private int calories;

    /**
     * Relationship mapping: Multiple activities belong to one Runner.
     * <p>
     * - FetchType.LAZY: <br>
     * - The runner data is not loaded from the DB unless explicitly requested. <br>
     * - @JoinColumn: Defines the foreign key column "runner_id" in the "activities" table.
     * </p>
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "runner_id", nullable = false)
    private RunnerEntity runner;

    public ActivityEntity() {}

    /**
     * Convenience constructor for creating a basic activity.
     * @param distanceKm Distance in kilometers.
     * @param durationSec Duration in seconds.
     * @param date Date of the activity.
     */
    public ActivityEntity(double distanceKm, int durationSec, LocalDate date) {
        this.distanceKm = distanceKm;
        this.durationSec = durationSec;
        this.date = date;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public int getDurationSec() {
        return durationSec;
    }

    public void setDurationSec(int durationSec) {
        this.durationSec = durationSec;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public RunnerEntity getRunner() {
        return runner;
    }

    public void setRunner(RunnerEntity runner) {
        this.runner = runner;
    }
}
