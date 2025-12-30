package com.pavel.jogger.service;

import com.pavel.jogger.persistence.repository.ActivityRepository;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ChartServiceTest {

    @Test
    void getRunnerStats_returns_two_charts() {
        ActivityRepository repo = mock(ActivityRepository.class);

        when(repo.totalDistance(1L)).thenReturn(10.0);
        when(repo.totalActivities(1L)).thenReturn(5L);

        ChartService service = new ChartService(repo);

        var charts = service.getRunnerStats(1L);

        assertEquals(2, charts.size());
    }
}
