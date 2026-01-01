package com.pavel.jogger.persistence.mapper;

import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.web.dto.runner.RunnerResponse;

/**
 * Utility class for mapping between Runner entities and DTOs.
 * <p>
 * This keeps the conversion logic centralized. If we ever add a new field to the response
 * we only need to update it here, not in every controller method.
 * </p>
 */
public class RunnerMapper {

    public static RunnerResponse toResponse(RunnerEntity entity) {
        return new RunnerResponse(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getRole(),
                entity.getDateJoined()
        );
    }
}
