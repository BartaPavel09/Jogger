package com.pavel.jogger.persistence.repository;

import com.pavel.jogger.persistence.entity.BadgeEntity;
import com.pavel.jogger.persistence.entity.RunnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BadgeRepository extends JpaRepository<BadgeEntity, Long> {

    Optional<BadgeEntity> findByRunnerAndCode(RunnerEntity runner, String code);

    List<BadgeEntity> findByRunnerId(Long runnerId);
}
