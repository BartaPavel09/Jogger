package com.pavel.jogger.service;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.entity.UserFollowEntity;
import com.pavel.jogger.persistence.repository.ActivityRepository;
import com.pavel.jogger.persistence.repository.RunnerRepository;
import com.pavel.jogger.persistence.repository.UserFollowRepository;
import com.pavel.jogger.web.dto.activity.ActivityResponse;
import com.pavel.jogger.web.dto.social.UserSearchResponse;
import com.pavel.jogger.web.exception.ConflictException;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FollowServiceTest {

    private final UserFollowRepository userFollowRepository = mock(UserFollowRepository.class);
    private final RunnerRepository runnerRepository = mock(RunnerRepository.class);
    private final ActivityRepository activityRepository = mock(ActivityRepository.class);
    private final FollowService followService = new FollowService(userFollowRepository, runnerRepository, activityRepository);

    @Test
    void follow_shouldSave_whenValid() {
        RunnerEntity follower = new RunnerEntity("follower", "f@test.com", "pass");
        RunnerEntity target = new RunnerEntity("target", "t@test.com", "pass");

        when(runnerRepository.findById(1L)).thenReturn(Optional.of(follower));
        when(runnerRepository.findById(2L)).thenReturn(Optional.of(target));
        when(userFollowRepository.existsByFollowerAndFollowed(follower, target)).thenReturn(false);

        followService.follow(1L, 2L);

        verify(userFollowRepository).save(any(UserFollowEntity.class));
    }

    @Test
    void follow_shouldThrowConflict_whenFollowingSelf() {
        assertThrows(ConflictException.class, () -> followService.follow(1L, 1L));
    }

    @Test
    void unfollow_shouldDelete_whenExists() {
        UserFollowEntity follow = new UserFollowEntity();
        when(userFollowRepository.findByFollowerIdAndFollowedId(1L, 2L)).thenReturn(Optional.of(follow));

        followService.unfollow(1L, 2L);

        verify(userFollowRepository).delete(follow);
    }

    @Test
    void searchRunners_shouldReturnMappedList() {
        RunnerEntity r2 = new RunnerEntity("bob", "b@test.com", "pass");
        when(runnerRepository.findByUsernameContainingIgnoreCaseAndIdNot("bob", 1L)).thenReturn(List.of(r2));
        when(userFollowRepository.findFollowedIdsByFollowerId(1L)).thenReturn(List.of(2L));

        List<UserSearchResponse> results = followService.searchRunners(1L, "bob");

        assertEquals(1, results.size());
        assertEquals("bob", results.get(0).getUsername());
    }

    @Test
    void getFriendsFeed_shouldReturnPage() {
        RunnerEntity friend = new RunnerEntity("alice", "a@test.com", "pass");
        ActivityEntity act = new ActivityEntity(5.0, 1800, LocalDate.now());
        act.setRunner(friend);

        Page<ActivityEntity> page = new PageImpl<>(List.of(act));
        when(activityRepository.findFriendsFeed(eq(1L), any(Pageable.class))).thenReturn(page);

        Page<ActivityResponse> feed = followService.getFriendsFeed(1L, 0, 10);

        assertEquals(1, feed.getContent().size());
        assertEquals(5.0, feed.getContent().get(0).getDistanceKm());
    }
}
