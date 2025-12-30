package com.pavel.jogger.web.dto.runner;

import java.time.LocalDateTime;

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
