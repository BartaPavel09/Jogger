package com.pavel.jogger.persistence.repository;

import com.pavel.jogger.persistence.entity.BadgeEntity;
import com.pavel.jogger.persistence.entity.RunnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for managing Badge entities.
 */
public interface BadgeRepository extends JpaRepository<BadgeEntity, Long> {

    /**
     * Checks if a specific runner already has a specific badge type.
     * <p>
     * This is critical for <b>Idempotency</b>: preventing the system from awarding
     * the "First 5K" badge every time the user runs 5km.
     * </p>
     * @param runner The user entity.
     * @param code   The unique badge code.
     * @return An Optional containing the badge if it exists.
     */
    Optional<BadgeEntity> findByRunnerAndCode(RunnerEntity runner, String code);

    /**
     * Retrieves all badges earned by a specific runner.
     */
    List<BadgeEntity> findByRunnerId(Long runnerId);
}
