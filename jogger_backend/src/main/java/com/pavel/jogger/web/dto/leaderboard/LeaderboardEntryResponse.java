package com.pavel.jogger.web.dto.leaderboard;

/**
 * DTO representing an entry in the global distance leaderboard.
 */
public class LeaderboardEntryResponse {

    private int rank;
    private Long runnerId;
    private String username;
    private double totalDistanceKm;
    private long totalRuns;

    public LeaderboardEntryResponse() {
    }

    public LeaderboardEntryResponse(int rank, Long runnerId, String username, double totalDistanceKm, long totalRuns) {
        this.rank = rank;
        this.runnerId = runnerId;
        this.username = username;
        this.totalDistanceKm = totalDistanceKm;
        this.totalRuns = totalRuns;
    }

    public int getRank() {
        return rank;
    }

    public Long getRunnerId() {
        return runnerId;
    }

    public String getUsername() {
        return username;
    }

    public double getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public long getTotalRuns() {
        return totalRuns;
    }
}
