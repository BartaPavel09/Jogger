package com.pavel.jogger.service;

import com.pavel.jogger.persistence.repository.ActivityRepository;
import com.pavel.jogger.persistence.repository.LeaderboardProjection;
import com.pavel.jogger.web.dto.leaderboard.LeaderboardEntryResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for computing the global running distance leaderboard.
 */
@Service
public class LeaderboardService {

    private final ActivityRepository activityRepository;

    public LeaderboardService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * Computes the global distance leaderboard ranking all runners by cumulative kilometers.
     * Uses JPA aggregation: SUM(distance) GROUP BY user_id ORDER BY total_distance DESC.
     *
     * @return Ordered list of LeaderboardEntryResponse with assigned ranks (#1, #2, #3...).
     */
    public List<LeaderboardEntryResponse> getGlobalLeaderboard() {
        List<LeaderboardProjection> raw = activityRepository.findGlobalLeaderboard();
        List<LeaderboardEntryResponse> result = new ArrayList<>();
        int rank = 1;

        for (LeaderboardProjection p : raw) {
            double rawDistance = (p.getTotalDistance() != null) ? p.getTotalDistance() : 0.0;
            double distance = Math.round(rawDistance * 100.0) / 100.0;
            long runs = (p.getTotalRuns() != null) ? p.getTotalRuns() : 0L;

            result.add(new LeaderboardEntryResponse(
                    rank++,
                    p.getRunnerId(),
                    p.getUsername(),
                    distance,
                    runs
            ));
        }

        return result;
    }
}
