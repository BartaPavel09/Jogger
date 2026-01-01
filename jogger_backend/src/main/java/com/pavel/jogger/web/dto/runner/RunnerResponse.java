package com.pavel.jogger.web.dto.runner;

import java.time.LocalDateTime;

/**
 * Data Transfer Object (DTO) representing the public profile of a runner.
 * <p>
 * This object is returned to the frontend when user details are requested.
 * Crucially, it excludes sensitive information like password hashes.
 * </p>
 */
public class RunnerResponse {

    private Long id;
    private String username;
    private String email;
    private String role;
    private LocalDateTime dateJoined;

    public RunnerResponse(Long id, String username, String email, String role,LocalDateTime dateJoined) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.dateJoined = dateJoined;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
    public LocalDateTime getDateJoined() { return dateJoined; }
}
