package com.pavel.jogger.web.controller;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.persistence.mapper.ActivityMapper;
import com.pavel.jogger.service.AccessService;
import com.pavel.jogger.service.ActivityService;
import com.pavel.jogger.web.dto.activity.ActivityResponse;
import com.pavel.jogger.web.dto.activity.CreateActivityRequest;
import com.pavel.jogger.web.dto.activity.UpdateActivityRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing activities.
 * <p>
 * Handles both user-nested endpoints (/runners/{id}/activities) and
 * direct activity endpoints (/activities/{activityId}).
 * </p>
 */
@RestController
public class ActivityController {

    private final ActivityService activityService;
    private final AccessService accessService;

    public ActivityController(ActivityService activityService, AccessService accessService) {
        this.activityService = activityService;
        this.accessService = accessService;
    }

    /**
     * Retrieves a list of all jogging activities for a specific runner.
     */
    @GetMapping("/runners/{id}/activities")
    public List<ActivityResponse> getActivitiesForRunner(
            @PathVariable Long id,
            Authentication authentication
    ) {
        accessService.checkRunnerAccess(authentication, id);

        return activityService.getActivitiesForRunner(id)
                .stream()
                .map(ActivityMapper::toResponse)
                .toList();
    }

    /**
     * Creates and saves a new activity for a specific runner.
     */
    @PostMapping("/runners/{id}/activities")
    public ActivityResponse addActivity(
            @PathVariable Long id,
            @Valid @RequestBody CreateActivityRequest request,
            Authentication authentication
    ) {
        accessService.checkRunnerAccess(authentication, id);

        return ActivityMapper.toResponse(
                activityService.addActivity(
                        id,
                        request.getDistanceKm(),
                        request.getDurationSec(),
                        request.getDate(),
                        request.getRoute(),
                        request.getCalories()
                )
        );
    }

    /**
     * Updates an existing activity.
     */
    @PutMapping("/activities/{activityId}")
    public ActivityResponse updateActivity(
            @PathVariable Long activityId,
            @Valid @RequestBody UpdateActivityRequest request,
            Authentication authentication
    ) {
        ActivityEntity existing = activityService.getActivityById(activityId);
        Long runnerId = existing.getRunner().getId();

        accessService.checkRunnerAccess(authentication, runnerId);

        return ActivityMapper.toResponse(
                activityService.updateActivity(
                        activityId,
                        request.getDistanceKm(),
                        request.getDurationSec(),
                        request.getDate(),
                        request.getRoute(),
                        request.getCalories()
                )
        );
    }

    /**
     * Deletes an activity permanently.
     */
    @DeleteMapping("/activities/{activityId}")
    public ResponseEntity<Void> deleteActivity(
            @PathVariable Long activityId,
            Authentication authentication
    ) {
        ActivityEntity existing = activityService.getActivityById(activityId);
        Long runnerId = existing.getRunner().getId();

        accessService.checkRunnerAccess(authentication, runnerId);

        activityService.deleteActivity(activityId);
        return ResponseEntity.noContent().build();
    }
}
