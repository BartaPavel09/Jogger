package com.pavel.jogger.web.controller;

import com.pavel.jogger.persistence.mapper.ActivityMapper;
import com.pavel.jogger.service.AccessService;
import com.pavel.jogger.service.ActivityService;
import com.pavel.jogger.web.dto.activity.ActivityResponse;
import com.pavel.jogger.web.dto.activity.CreateActivityRequest;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing collections of activities belonging to a specific runner.
 * <p>
 * This controller handles endpoints that are hierarchical under a runner context:
 * {@code /runners/{id}/activities}. It is responsible for creating new activities
 * and retrieving the history of activities for a user.
 * </p>
 */
@RestController
@RequestMapping("/runners")
public class ActivityController {

    private final ActivityService activityService;
    private final AccessService accessService;

    public ActivityController(ActivityService activityService, AccessService accessService) {
        this.activityService = activityService;
        this.accessService = accessService;
    }

    /**
     * Retrieves a list of all jogging activities for a specific runner.
     * <p>
     * This endpoint performs a security check to ensure the requesting user
     * has permission to view the target runner's data.
     * </p>
     * @param id             The unique ID of the runner whose activities are being requested.
     * Mapped from the URL path variable.
     * @param authentication The security context containing the currently logged-in user's details.
     * Injected automatically by Spring Security.
     * @return A {@link List} of {@link ActivityResponse} DTOs representing the user's run history.
     * @throws com.pavel.jogger.web.exception.ForbiddenException If the logged-in user is not allowed to access this data.
     * @throws com.pavel.jogger.web.exception.NotFoundException  If the runner with the given ID does not exist.
     */
    @GetMapping("/{id}/activities")
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
     * <p>
     * Accepts a JSON payload with run details (distance, duration, date), validates the input,
     * and saves it to the database. It also triggers asynchronous badge evaluation.
     * </p>
     * @param id             The unique ID of the runner adding the activity.
     * @param request        The {@link CreateActivityRequest} DTO containing the run details.
     * Validated using {@code @Valid} to ensure positive distance/duration.
     * @param authentication The security context of the current user.
     * @return An {@link ActivityResponse} containing the saved activity data (including the generated ID).
     * @throws com.pavel.jogger.web.exception.ForbiddenException If the logged-in user tries to add an activity for someone else.
     * @throws org.springframework.web.bind.MethodArgumentNotValidException If the input data (JSON) is invalid (e.g. negative distance).
     */
    @PostMapping("/{id}/activities")
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
}
