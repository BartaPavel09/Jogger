package com.pavel.jogger.service;

import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.repository.RunnerRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RunnerServiceTest {

    private final RunnerRepository runnerRepository = mock(RunnerRepository.class);
    private final RunnerService runnerService = new RunnerService(runnerRepository);

    @Test
    void getRunnerById_shouldReturnRunner() {
        RunnerEntity runner = new RunnerEntity("u", "e", "p");

        when(runnerRepository.findById(1L))
                .thenReturn(Optional.of(runner));

        Optional<RunnerEntity> result = runnerService.getRunnerById(1L);

        assertTrue(result.isPresent());
        assertEquals("u", result.get().getUsername());
    }

    @Test
    void updateRunner_shouldUpdateEmail() {
        RunnerEntity runner = new RunnerEntity("u", "old@test.com", "p");

        when(runnerRepository.findById(1L))
                .thenReturn(Optional.of(runner));
        when(runnerRepository.existsByEmail("new@test.com"))
                .thenReturn(false);
        when(runnerRepository.save(any()))
                .thenReturn(runner);

        RunnerEntity updated = runnerService.updateRunner(1L, "new@test.com");

        assertEquals("new@test.com", updated.getEmail());
    }

    @Test
    void deleteRunner_shouldCallRepository() {
        when(runnerRepository.existsById(1L)).thenReturn(true);

        runnerService.deleteRunner(1L);

        verify(runnerRepository).deleteById(1L);
    }
}
