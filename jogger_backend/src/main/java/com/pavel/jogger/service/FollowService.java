package com.pavel.jogger.service;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.entity.UserFollowEntity;
import com.pavel.jogger.persistence.mapper.ActivityMapper;
import com.pavel.jogger.persistence.repository.ActivityRepository;
import com.pavel.jogger.persistence.repository.RunnerRepository;
import com.pavel.jogger.persistence.repository.UserFollowRepository;
import com.pavel.jogger.web.dto.activity.ActivityResponse;
import com.pavel.jogger.web.dto.social.UserSearchResponse;
import com.pavel.jogger.web.exception.ConflictException;
import com.pavel.jogger.web.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Service class handling social graph operations:
 * following/unfollowing runners, searching users, and generating the friends activity feed.
 */
@Service
public class FollowService {

    private final UserFollowRepository userFollowRepository;
    private final RunnerRepository runnerRepository;
    private final ActivityRepository activityRepository;

    public FollowService(UserFollowRepository userFollowRepository,
                         RunnerRepository runnerRepository,
                         ActivityRepository activityRepository) {
        this.userFollowRepository = userFollowRepository;
        this.runnerRepository = runnerRepository;
        this.activityRepository = activityRepository;
    }

    /**
     * Follows a target runner.
     *
     * @param followerId   The ID of the user initiating the follow.
     * @param targetUserId The ID of the user to be followed.
     */
    @Transactional
    public void follow(Long followerId, Long targetUserId) {
        if (followerId.equals(targetUserId)) {
            throw new ConflictException("You cannot follow yourself");
        }

        RunnerEntity target = runnerRepository.findById(targetUserId)
                .orElseThrow(() -> new NotFoundException("Target user not found"));
        RunnerEntity follower = runnerRepository.findById(followerId)
                .orElseThrow(() -> new NotFoundException("Follower not found"));

        if (!userFollowRepository.existsByFollowerAndFollowed(follower, target)) {
            UserFollowEntity follow = new UserFollowEntity(follower, target);
            userFollowRepository.save(follow);
        }
    }

    /**
     * Unfollows a target runner.
     *
     * @param followerId   The ID of the user initiating the unfollow.
     * @param targetUserId The ID of the user to unfollow.
     */
    @Transactional
    public void unfollow(Long followerId, Long targetUserId) {
        userFollowRepository.findByFollowerIdAndFollowedId(followerId, targetUserId)
                .ifPresent(userFollowRepository::delete);
    }

    /**
     * Checks if a user is following another user.
     */
    public boolean isFollowing(Long followerId, Long targetUserId) {
        return userFollowRepository.existsByFollowerIdAndFollowedId(followerId, targetUserId);
    }

    /**
     * Searches runners by username query and indicates whether the current user is following each result.
     *
     * @param currentRunnerId The ID of the currently logged-in user.
     * @param query           The username search substring (or null/blank for all other users).
     * @return List of matching runners.
     */
    public List<UserSearchResponse> searchRunners(Long currentRunnerId, String query) {
        List<RunnerEntity> runners;
        if (query == null || query.trim().isEmpty()) {
            runners = runnerRepository.findByIdNot(currentRunnerId);
        } else {
            runners = runnerRepository.findByUsernameContainingIgnoreCaseAndIdNot(query.trim(), currentRunnerId);
        }

        List<Long> followedIds = userFollowRepository.findFollowedIdsByFollowerId(currentRunnerId);
        Set<Long> followedSet = new HashSet<>(followedIds);

        return runners.stream()
                .map(r -> new UserSearchResponse(
                        r.getId(),
                        r.getUsername(),
                        r.getEmail(),
                        r.getRole(),
                        followedSet.contains(r.getId())
                ))
                .toList();
    }

    /**
     * Retrieves a paginated activity feed containing runs exclusively from followed users,
     * ordered by newest date first.
     *
     * @param currentRunnerId The ID of the current user.
     * @param page            0-based page index.
     * @param size            Number of items per page.
     * @return Page of ActivityResponse.
     */
    public Page<ActivityResponse> getFriendsFeed(Long currentRunnerId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ActivityEntity> activityPage = activityRepository.findFriendsFeed(currentRunnerId, pageable);
        return activityPage.map(ActivityMapper::toResponse);
    }
}
