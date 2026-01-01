package com.pavel.jogger.service;

import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.repository.RunnerRepository;
import com.pavel.jogger.web.exception.ConflictException;
import com.pavel.jogger.web.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class encapsulating business logic for User management.
 * <p>
 * This service handles data retrieval, updates with validation logic (like unique emails),
 * and account removal. It acts as the bridge between the Controller and the Repository.
 * </p>
 */
@Service
public class RunnerService {

    private final RunnerRepository runnerRepository;

    public RunnerService(RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

    
    public Optional<RunnerEntity> getRunnerById(Long id) {
        return runnerRepository.findById(id);
    }

    /**
     * Finds a runner by their username.
     * Essential for the login process and the {@code /me} endpoint.
     */
    public Optional<RunnerEntity> getRunnerByUsername(String username) {
        return runnerRepository.findByUsername(username);
    }

    /**
     * Updates specific fields of a runner's profile.
     * <p>
     * <b>Logic:</b> <br>
     * 1. Check if the user exists. <br>
     * 2. If the email is being changed, verify that the NEW email isn't already taken by someone else. <br>
     * 3. Update fields and save.
     * </p>
     * @param id        The ID of the user to update.
     * @param newEmail  The new email address.
     * @param newWeight The new weight (can be null if not updating weight).
     * @return The updated entity.
     * @throws NotFoundException If user ID is invalid.
     * @throws ConflictException If the new email is already in use (HTTP 409).
     */
    public RunnerEntity updateRunner(Long id, String newEmail, Double newWeight) {
        RunnerEntity runner = runnerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Runner not found"));

        if (!runner.getEmail().equals(newEmail) && runnerRepository.existsByEmail(newEmail)) {
            throw new ConflictException("Email already exists");
        }

        runner.setEmail(newEmail);

        if (newWeight != null) {
            runner.setWeight(newWeight);
        }

        return runnerRepository.save(runner);
    }

    /**
     * Permanently deletes a user account by the user id.
     * @throws NotFoundException If the user does not exist.
     */
    public void deleteRunner(Long id) {
        if (!runnerRepository.existsById(id)) {
            throw new NotFoundException("Runner not found");
        }
        runnerRepository.deleteById(id);
    }

    /**
     * Retrieves all registered users.
     * Intended for Admin use only.
     */
    public List<RunnerEntity> getAllRunners() {
        return runnerRepository.findAll();
    }
}
