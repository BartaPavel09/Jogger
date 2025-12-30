package com.pavel.jogger.persistence.repository;

import com.pavel.jogger.persistence.entity.RunnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RunnerRepository extends JpaRepository<RunnerEntity, Long> {

    Optional<RunnerEntity> findByUsername(String username);

    Optional<RunnerEntity> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
