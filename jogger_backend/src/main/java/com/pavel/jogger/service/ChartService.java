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

@Service
public class ChartService {

    private final ActivityRepository activityRepository;

    public ChartService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

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