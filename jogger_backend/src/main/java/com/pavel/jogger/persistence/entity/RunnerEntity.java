package com.pavel.jogger.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "runners")
public class RunnerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(name = "date_joined", nullable = false)
    private LocalDateTime dateJoined;

    @Column(nullable = false)
    private String role;

    @Column(nullable = true)
    private Double weight;

    @OneToMany(
            mappedBy = "runner",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ActivityEntity> activities = new ArrayList<>();

    @OneToMany(
            mappedBy = "runner",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BadgeEntity> badges = new ArrayList<>();

    public RunnerEntity() {
        
    }

    public RunnerEntity(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.dateJoined = LocalDateTime.now();
        this.role = "USER";
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public LocalDateTime getDateJoined() {
        return dateJoined;
    }

    public void setDateJoined(LocalDateTime dateJoined) {
        this.dateJoined = dateJoined;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<ActivityEntity> getActivities() {
        return activities;
    }

    public List<BadgeEntity> getBadges() {
        return badges;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
