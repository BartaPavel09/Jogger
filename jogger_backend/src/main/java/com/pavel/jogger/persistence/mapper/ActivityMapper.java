package com.pavel.jogger.persistence.mapper;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.web.dto.activity.ActivityResponse;

public class ActivityMapper {

    public static ActivityResponse toResponse(ActivityEntity entity) {
        double durationMin = entity.getDurationSec() / 60.0;
        double pace = 0.0;

        if (entity.getDistanceKm() > 0) {
            pace = durationMin / entity.getDistanceKm();
            pace = Math.round(pace * 100.0) / 100.0;
        }

        return new ActivityResponse(
                entity.getId(),
                entity.getDistanceKm(),
                entity.getDurationSec(),
                entity.getCalories(),
                entity.getDate(),
                entity.getRoute(),
                pace
        );
    }
}