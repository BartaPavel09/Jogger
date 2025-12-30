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

@RestController
@RequestMapping("/runners")
public class ActivityController {

    private final ActivityService activityService;
    private final AccessService accessService;

    public ActivityController(ActivityService activityService, AccessService accessService) {
        this.activityService = activityService;
        this.accessService = accessService;
    }

    
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
