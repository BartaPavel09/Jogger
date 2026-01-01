package com.pavel.jogger.service;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.repository.ActivityRepository;
import com.pavel.jogger.persistence.repository.RunnerRepository;
import com.pavel.jogger.web.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Service class for managing jogging activities.
 * <p>
 * This class handles the business logic for creating, updating, retrieving, and deleting runs.
 * It also automatically triggers badge evaluation whenever a new activity is added.
 * </p>
 */
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

    /**
     * Creates and saves a new activity for a specific runner.
     * @param runnerId    The ID of the user performing the activity.
     * @param distanceKm  Distance run in kilometers.
     * @param durationSec Duration of the run in seconds.
     * @param date        The date of the run.
     * @param route       Optional description of the route taken.
     * @param calories    Calories burned. If null, they are calculated automatically.
     * @return The saved ActivityEntity.
     * @throws NotFoundException If the runnerId does not exist.
     */
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

    /**
     * Estimates calories burned based on distance and weight.
     * Formula approximation: Distance(km) * Weight(kg) * 1.036
     * @param distanceKm   Distance run.
     * @param runnerWeight User's weight (defaults to 70kg if not set).
     * @return Estimated calories as an integer.
     */
    private int estimateCalories(double distanceKm, Double runnerWeight) {
        double weight = (runnerWeight != null) ? runnerWeight : 70.0;

        return (int) Math.round(distanceKm * weight * 1.036);
    }

    /**
     * Retrieves a single activity by ID.
     * @throws NotFoundException If the activity doesn't exist.
     */
    public ActivityEntity getActivityById(Long activityId) {
        return activityRepository.findById(activityId)
                .orElseThrow(() -> new NotFoundException("Activity not found"));
    }

    /**
     * Returns all activities for a specific runner.
     */
    public List<ActivityEntity> getActivitiesForRunner(Long runnerId) {
        return activityRepository.findByRunnerId(runnerId);
    }

    /**
     * Updates an existing activity.
     * Recalculates calories if distance or weight logic changes.
     */
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

    /**
     * Deletes an activity based on it's ID
     * @param activityId The ID of an Activity
     */
    public void deleteActivity(Long activityId) {
        ActivityEntity activity = getActivityById(activityId);
        activityRepository.delete(activity);
    }
}
