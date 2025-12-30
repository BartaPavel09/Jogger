package com.pavel.jogger.service;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.repository.ActivityRepository;
import com.pavel.jogger.persistence.repository.RunnerRepository;
import com.pavel.jogger.web.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final RunnerRepository runnerRepository;
    private final BadgeService badgeService;

    public ActivityService(ActivityRepository activityRepository,
                           RunnerRepository runnerRepository,
                           BadgeService badgeService) {
        this.activityRepository = activityRepository;
        this.runnerRepository = runnerRepository;
        this.badgeService = badgeService;
    }

    
    public ActivityEntity addActivity(
            Long runnerId,
            double distanceKm,
            int durationSec,
            LocalDate date,
            String route,
            Integer calories
    ) {
        RunnerEntity runner = runnerRepository.findById(runnerId)
                .orElseThrow(() -> new NotFoundException("Runner not found"));

        ActivityEntity activity = new ActivityEntity();
        activity.setDistanceKm(distanceKm);
        activity.setDurationSec(durationSec);
        activity.setDate(date);
        activity.setRoute(route);
        activity.setRunner(runner);

        if (calories != null) {
            activity.setCalories(calories);
        } else {
            activity.setCalories(estimateCalories(distanceKm, runner.getWeight()));
        }

        ActivityEntity saved = activityRepository.save(activity);
        badgeService.evaluateBadgesAsync(runner, saved);

        return saved;
    }
    private int estimateCalories(double distanceKm, Double runnerWeight) {
        double weight = (runnerWeight != null) ? runnerWeight : 70.0;

        return (int) Math.round(distanceKm * weight * 1.036);
    }

    
    public ActivityEntity getActivityById(Long activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Activity not found"));
    }

    public List<ActivityEntity> getActivitiesForRunner(Long runnerId) {
        return activityRepository.findByRunnerId(runnerId);
    }

    
    public ActivityEntity updateActivity(
            Long activityId,
            double distanceKm,
            int durationSec,
            String route,
            Integer calories
    ) {
        ActivityEntity activity = getActivityById(activityId);

        activity.setDistanceKm(distanceKm);
        activity.setDurationSec(durationSec);
        if (route != null) activity.setRoute(route);

        if (calories != null) {
            activity.setCalories(calories);
        } else {
            activity.setCalories(estimateCalories(distanceKm, activity.getRunner().getWeight()));
        }

        return activityRepository.save(activity);
    }

    
    public void deleteActivity(Long activityId) {
        ActivityEntity activity = getActivityById(activityId);
        activityRepository.delete(activity);
    }
}
