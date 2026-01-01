package com.pavel.jogger.web.controller;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.persistence.mapper.ActivityMapper;
import com.pavel.jogger.service.AccessService;
import com.pavel.jogger.service.ActivityService;
import com.pavel.jogger.web.dto.activity.ActivityResponse;
import com.pavel.jogger.web.dto.activity.UpdateActivityRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for managing individual activity resources.
 * <p>
 * This controller handles endpoints that operate directly on a specific activity ID:
 * {@code /activities/{activityId}}. It provides functionality for updating and deleting existing runs.
 * </p>
 */
@RestController
@RequestMapping("/activities")
public class ActivitiesController {

    private final ActivityService activityService;
    private final AccessService accessService;

    public ActivitiesController(ActivityService activityService, AccessService accessService) {
        this.activityService = activityService;
        this.accessService = accessService;
    }

    /**
     * Updates the details of an existing activity.
     * <p>
     * This method first retrieves the activity to identify its owner, then checks if the
     * currently authenticated user has permission to modify it.
     * </p>
     * @param activityId     The unique ID of the activity to update.
     * @param request        The {@link UpdateActivityRequest} DTO containing the new values.
     * Only fields provided in the DTO will be considered (e.g. date cannot be changed here).
     * @param authentication The security context of the current user.
     * @return The updated {@link ActivityResponse} reflecting the changes.
     * @throws com.pavel.jogger.web.exception.NotFoundException  If the activity ID does not exist.
     * @throws com.pavel.jogger.web.exception.ForbiddenException If the user does not own this activity.
     */
    @PutMapping("/{activityId}")
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
                        request.getRoute(),
                        request.getCalories()
                )
        );
    }

    /**
     * Deletes a specific activity permanently.
     * <p>
     * Similar to update, it verifies ownership before deletion.
     * returns HTTP 204 No Content upon success.
     * </p>
     * @param activityId     The unique ID of the activity to delete.
     * @param authentication The security context of the current user.
     * @return A {@link ResponseEntity} with status 204 (No Content) and no body.
     * @throws com.pavel.jogger.web.exception.NotFoundException  If the activity ID does not exist.
     * @throws com.pavel.jogger.web.exception.ForbiddenException If the user does not own this activity.
     */
    @DeleteMapping("/{activityId}")
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
