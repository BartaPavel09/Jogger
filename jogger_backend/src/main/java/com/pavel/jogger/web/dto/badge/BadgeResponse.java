package com.pavel.jogger.web.dto.badge;

import java.time.LocalDate;

public class BadgeResponse {

    private String title;
    private String description;
    private String badgeType;
    private LocalDate dateEarned;
    private boolean seen;

    public BadgeResponse(String title, String description, String badgeType, LocalDate dateEarned, boolean seen) {
        this.title = title;
        this.description = description;
        this.badgeType = badgeType;
        this.dateEarned = dateEarned;
        this.seen = seen;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getBadgeType() { return badgeType; }
    public LocalDate getDateEarned() { return dateEarned; }
    public boolean isSeen() { return seen; }
}