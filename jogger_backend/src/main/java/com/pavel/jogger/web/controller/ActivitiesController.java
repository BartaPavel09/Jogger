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

@RestController
@RequestMapping("/activities")
public class ActivitiesController {

    private final ActivityService activityService;
    private final AccessService accessService;

    public ActivitiesController(ActivityService activityService, AccessService accessService) {
        this.activityService = activityService;
        this.accessService = accessService;
    }

    
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
