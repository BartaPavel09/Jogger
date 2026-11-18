package com.pavel.jogger.service;

import com.pavel.jogger.persistence.repository.ActivityRepository;
import com.pavel.jogger.persistence.repository.LeaderboardProjection;
import com.pavel.jogger.web.dto.leaderboard.LeaderboardEntryResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LeaderboardServiceTest {

    private final ActivityRepository activityRepository = mock(ActivityRepository.class);
    private final LeaderboardService leaderboardService = new LeaderboardService(activityRepository);

    @Test
    void getGlobalLeaderboard_shouldReturnRankedEntries() {
        LeaderboardProjection p1 = mock(LeaderboardProjection.class);
        when(p1.getRunnerId()).thenReturn(10L);
        when(p1.getUsername()).thenReturn("fast_runner");
        when(p1.getTotalDistance()).thenReturn(42.195);
        when(p1.getTotalRuns()).thenReturn(5L);

        LeaderboardProjection p2 = mock(LeaderboardProjection.class);
        when(p2.getRunnerId()).thenReturn(20L);
        when(p2.getUsername()).thenReturn("second_runner");
        when(p2.getTotalDistance()).thenReturn(21.1);
        when(p2.getTotalRuns()).thenReturn(2L);

        when(activityRepository.findGlobalLeaderboard()).thenReturn(List.of(p1, p2));

        List<LeaderboardEntryResponse> result = leaderboardService.getGlobalLeaderboard();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getRank());
        assertEquals("fast_runner", result.get(0).getUsername());
        assertEquals(42.20, result.get(0).getTotalDistanceKm());
        assertEquals(2, result.get(1).getRank());
        assertEquals("second_runner", result.get(1).getUsername());
    }
}
