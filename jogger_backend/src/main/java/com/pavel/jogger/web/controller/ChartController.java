package com.pavel.jogger.web.controller;

import com.pavel.jogger.service.AccessService;
import com.pavel.jogger.service.ChartService;
import com.pavel.jogger.web.dto.chart.ChartResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/runners")
public class ChartController {

    private final ChartService chartService;
    private final AccessService accessService;

    public ChartController(ChartService chartService, AccessService accessService) {
        this.chartService = chartService;
        this.accessService = accessService;
    }

    @GetMapping("/{id}/charts")
    public List<ChartResponse> getCharts(
            @PathVariable Long id,
            Authentication authentication
    ) {
        accessService.checkRunnerAccess(authentication, id);
        return chartService.getRunnerStats(id);
    }

    @GetMapping("/{id}/charts/weekly")
    public List<ChartResponse> getWeeklyProgress(
            @PathVariable Long id,
            Authentication authentication
    ) {
        accessService.checkRunnerAccess(authentication, id);
        return chartService.getRunsPerWeek(id);
    }
}