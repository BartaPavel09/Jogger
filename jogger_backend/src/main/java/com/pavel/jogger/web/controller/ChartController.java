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

/**
 * REST Controller responsible for providing aggregated data for UI charts.
 * <p>
 * This controller serves as the data provider for the dashboard visualizations.
 * It uses the ChartService to aggregate raw data into simple label-value pairs
 * that the frontend can render immediately.
 * </p>
 */
@RestController
@RequestMapping("/runners")
public class ChartController {

    private final ChartService chartService;
    private final AccessService accessService;

    public ChartController(ChartService chartService, AccessService accessService) {
        this.chartService = chartService;
        this.accessService = accessService;
    }

    /**
     * Retrieves general statistics for the runner (Total Distance, Total Runs).
     * <p>
     * This endpoint provides the numbers usually displayed at the top of a profile.
     * It calls {@code chartService.getRunnerStats(id)} which performs efficient SQL aggregations.
     * </p>
     * @param id             The ID of the runner.
     * @param authentication The security context to verify access rights.
     * @return A list of {@link ChartResponse} objects (e.g., [{"Total km", 150.5}, {"Total runs", 20.0}]).
     */
    @GetMapping("/{id}/charts")
    public List<ChartResponse> getCharts(
            @PathVariable Long id,
            Authentication authentication
    ) {
        accessService.checkRunnerAccess(authentication, id);
        return chartService.getRunnerStats(id);
    }

    /**
     * Retrieves the volume of runs grouped by week number.
     * <p>
     * Unlike a daily chart, this endpoint provides a broader view of consistency over the year.
     * It calls {@code chartService.getRunsPerWeek(id)} which groups dates by ISO Week Number.
     * <br>
     * <b>Example Response:</b> [{"label": "W10", "value": 3.0}, {"label": "W11", "value": 5.0}]
     * </p>
     * @param id             The ID of the runner.
     * @param authentication The security context to verify access rights.
     * @return A list of {@link ChartResponse} objects sorted by week number.
     */
    @GetMapping("/{id}/charts/weekly")
    public List<ChartResponse> getWeeklyProgress(
            @PathVariable Long id,
            Authentication authentication
    ) {
        accessService.checkRunnerAccess(authentication, id);
        return chartService.getRunsPerWeek(id);
    }
}