package com.pavel.jogger.web.controller;

import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.service.AccessService;
import com.pavel.jogger.service.FollowService;
import com.pavel.jogger.web.dto.activity.ActivityResponse;
import com.pavel.jogger.web.dto.social.UserSearchResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for social networking features:
 * following/unfollowing runners, searching runners, and viewing friends' activity feed.
 */
@RestController
public class FollowController {

    private final FollowService followService;
    private final AccessService accessService;

    public FollowController(FollowService followService, AccessService accessService) {
        this.followService = followService;
        this.accessService = accessService;
    }

    /**
     * Follows another runner by their user ID.
     */
    @PostMapping("/runners/{id}/follow")
    public ResponseEntity<Void> followUser(@PathVariable Long id, Authentication authentication) {
        RunnerEntity current = accessService.currentRunner(authentication);
        followService.follow(current.getId(), id);
        return ResponseEntity.ok().build();
    }

    /**
     * Unfollows a runner by their user ID.
     */
    @DeleteMapping("/runners/{id}/follow")
    public ResponseEntity<Void> unfollowUser(@PathVariable Long id, Authentication authentication) {
        RunnerEntity current = accessService.currentRunner(authentication);
        followService.unfollow(current.getId(), id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Unfollows a runner via POST (convenience endpoint for clients that prefer POST).
     */
    @PostMapping("/runners/{id}/unfollow")
    public ResponseEntity<Void> unfollowUserPost(@PathVariable Long id, Authentication authentication) {
        RunnerEntity current = accessService.currentRunner(authentication);
        followService.unfollow(current.getId(), id);
        return ResponseEntity.ok().build();
    }

    /**
     * Searches runners by username and returns results along with follow status.
     */
    @GetMapping("/runners/search")
    public ResponseEntity<List<UserSearchResponse>> searchRunners(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String username,
            Authentication authentication
    ) {
        RunnerEntity current = accessService.currentRunner(authentication);
        String search = (query != null && !query.isBlank()) ? query : username;
        return ResponseEntity.ok(followService.searchRunners(current.getId(), search));
    }

    /**
     * Retrieves the paginated friends activity feed of runs by followed runners.
     */
    @GetMapping({"/activities/feed", "/feed"})
    public ResponseEntity<Page<ActivityResponse>> getFriendsFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication
    ) {
        RunnerEntity current = accessService.currentRunner(authentication);
        return ResponseEntity.ok(followService.getFriendsFeed(current.getId(), page, size));
    }
}
