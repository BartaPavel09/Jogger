package com.pavel.jogger.service;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.repository.ActivityRepository;
import com.pavel.jogger.persistence.repository.RunnerRepository;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ActivityServiceTest {

    @Test
    void addActivity_saves_activity() {
        ActivityRepository activityRepo = mock(ActivityRepository.class);
        RunnerRepository runnerRepo = mock(RunnerRepository.class);
        BadgeService badgeService = mock(BadgeService.class);

        ActivityService service = new ActivityService(activityRepo, runnerRepo, badgeService);

        RunnerEntity runner = new RunnerEntity("user", "u@test.com", "hash");
        when(runnerRepo.findById(1L)).thenReturn(Optional.of(runner));

        when(activityRepo.save(any(ActivityEntity.class))).thenAnswer(i -> i.getArguments()[0]);

        service.addActivity(
                1L,
                3.0,
                900,
                LocalDate.now(),
                "Park Run",
                null
        );

        verify(activityRepo).save(any(ActivityEntity.class));
        verify(badgeService).evaluateBadgesAsync(any(), any());
    }
}