package com.pavel.jogger.persistence.repository;

/**
 * Spring Data JPA projection for global distance leaderboard aggregation.
 */
public interface LeaderboardProjection {
    Long getRunnerId();
    String getUsername();
    Double getTotalDistance();
    Long getTotalRuns();
}
