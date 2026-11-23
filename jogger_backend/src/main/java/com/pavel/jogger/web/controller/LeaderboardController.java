package com.pavel.jogger.web.controller;

import com.pavel.jogger.service.LeaderboardService;
import com.pavel.jogger.web.dto.leaderboard.LeaderboardEntryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for the global running leaderboard.
 */
@RestController
public class LeaderboardController {

    private final LeaderboardService leaderboardService;

    public LeaderboardController(LeaderboardService leaderboardService) {
        this.leaderboardService = leaderboardService;
    }

    /**
     * Retrieves the global distance leaderboard ranking all runners by cumulative kilometers.
     */
    @GetMapping("/runners/leaderboard")
    public ResponseEntity<List<LeaderboardEntryResponse>> getLeaderboard() {
        return ResponseEntity.ok(leaderboardService.getGlobalLeaderboard());
    }
}
