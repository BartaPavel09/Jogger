package com.pavel.jogger.service;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.repository.ActivityRepository;
import com.pavel.jogger.persistence.repository.BadgeRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

class BadgeServiceTest {

    @Test
    void first5k_badge_is_awarded() {
        BadgeRepository badgeRepo = mock(BadgeRepository.class);
        ActivityRepository activityRepo = mock(ActivityRepository.class);

        BadgeService badgeService = new BadgeService(badgeRepo, activityRepo);

        RunnerEntity runner = new RunnerEntity("user", "u@test.com", "hash");

        ActivityEntity activity = new ActivityEntity();
        activity.setDistanceKm(5.5);
        activity.setDate(LocalDate.now());

        when(badgeRepo.findByRunnerAndCode(runner, "FIRST_5K"))
                .thenReturn(Optional.empty());

        badgeService.evaluateBadgesAsync(runner, activity);

        verify(badgeRepo, atLeastOnce()).save(any());
    }
}