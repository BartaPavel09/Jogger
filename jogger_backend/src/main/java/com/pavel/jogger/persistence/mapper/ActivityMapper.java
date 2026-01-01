package com.pavel.jogger.persistence.mapper;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.web.dto.activity.ActivityResponse;

/**
 * Utility class to convert Activity entities into Response DTOs.
 * <p>
 * This class handles formatting and derived calculations (like pace) that are
 * required for the UI but are not stored directly in the database.
 * </p>
 */
public class ActivityMapper {

    /**
     * Converts a raw database entity into a client-friendly response.
     * <p>
     * <b>Calculated Field: Pace (min/km)</b>
     * The database stores duration (seconds) and distance (km).
     * The mapper calculates pace = (durationMin / DistanceKm).
     * </p>
     * @param entity The source entity.
     * @return The DTO populated with data and calculated fields.
     */
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