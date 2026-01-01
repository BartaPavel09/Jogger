package com.pavel.jogger.persistence.repository;

import com.pavel.jogger.persistence.entity.RunnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository for managing Runner entities.
 * <p>
 * Provides built-in methods for CRUD operations and custom finders for authentication.
 * </p>
 */
public interface RunnerRepository extends JpaRepository<RunnerEntity, Long> {

    /**
     * Finds a user by their username.
     * Used extensively in authentication (loading user details).
     */
    Optional<RunnerEntity> findByUsername(String username);

    /**
     * Checks if a username is already taken.
     * @return true if the username exists, false otherwise.
     */
    boolean existsByUsername(String username);

    /**
     * Checks if an email is already registered.
     * @return true if the email exists, false otherwise.
     */
    boolean existsByEmail(String email);
}
