package com.pavel.jogger.web.dto.social;

/**
 * DTO representing a runner in search/discovery results,
 * including whether the current user is currently following them.
 */
public class UserSearchResponse {

    private Long id;
    private String username;
    private String email;
    private String role;
    private boolean following;

    public UserSearchResponse() {
    }

    public UserSearchResponse(Long id, String username, String email, String role, boolean following) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.following = following;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }
}
