package com.pavel.jogger.service;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.persistence.repository.ActivityRepository;
import com.pavel.jogger.web.dto.chart.ChartResponse;
import org.springframework.stereotype.Service;

import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsible for calculating statistics and preparing chart data.
 * <p>
 * This class transforms raw activity data from the database into simplified
 * label-value pairs (ChartResponse) ready to be displayed by the frontend charts.
 * </p>
 */
@Service
public class ChartService {

    private final ActivityRepository activityRepository;

    public ChartService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    /**
     * Calculates general summary statistics for a runner.
     * <p>
     * This method aggregates the total distance and total number of runs
     * to provide a big picture view of the user's performance.
     * </p>
     * @param runnerId The ID of the user.
     * @return A list containing two data points: Total km and Total runs.
     */
    public List<ChartResponse> getRunnerStats(Long runnerId) {
        List<ChartResponse> charts = new ArrayList<>();

        
        Double totalKmObj = activityRepository.totalDistance(runnerId);
        Long runsObj = activityRepository.totalActivities(runnerId);

        double totalKm = (totalKmObj == null) ? 0.0 : totalKmObj;
        long runs = (runsObj == null) ? 0L : runsObj;

        charts.add(new ChartResponse("Total km", totalKm));
        charts.add(new ChartResponse("Total runs", (double) runs));

        return charts;
    }

    /**
     * Groups activities by week number to show consistency over time.
     * <p>
     * Logic: <br>
     * 1. Fetches all activities for the user. <br>
     * 2. Extracts the ISO Week Number (1-52) from each activity's date. <br>
     * 3. Counts how many runs happened in each week. <br>
     * 4. Returns a sorted list (e.g., W10: 3 runs, W11: 5 runs).
     * </p>
     * @param runnerId The ID of the user.
     * @return A list of weekly run counts, sorted chronologically by week number.
     */
    public List<ChartResponse> getRunsPerWeek(Long runnerId) {
        List<ActivityEntity> activities = activityRepository.findByRunnerId(runnerId);

        
        Map<Integer, Long> runsPerWeek = activities.stream()
                .collect(Collectors.groupingBy(
                        a -> a.getDate().get(IsoFields.WEEK_OF_WEEK_BASED_YEAR),
                        Collectors.counting()
                ));

        
        return runsPerWeek.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) 
                .map(entry -> new ChartResponse(
                        "W" + entry.getKey(),
                        entry.getValue().doubleValue()
                ))
                .toList();
    }
}