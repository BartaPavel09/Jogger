package com.pavel.jogger.persistence.mapper;

import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.web.dto.runner.RunnerResponse;

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
