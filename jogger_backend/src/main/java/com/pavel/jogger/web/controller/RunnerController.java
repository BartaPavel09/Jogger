package com.pavel.jogger.web.controller;

import com.pavel.jogger.persistence.mapper.RunnerMapper;
import com.pavel.jogger.service.AccessService;
import com.pavel.jogger.service.RunnerService;
import com.pavel.jogger.web.dto.runner.RunnerResponse;
import com.pavel.jogger.web.dto.runner.UpdateRunnerRequest;
import com.pavel.jogger.web.exception.ForbiddenException;
import com.pavel.jogger.web.exception.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing user profiles.
 * <p>
 * Base URL: {@code /runners}
 * <br>
 * This controller handles operations related to the user account itself:
 * retrieving profile details, updating personal information (weight, email),
 * and account deletion. It relies on {@link AccessService} to enforce strict ownership rules.
 * </p>
 */
@RestController
@RequestMapping("/runners")
public class RunnerController {

    private final RunnerService runnerService;
    private final AccessService accessService;

    public RunnerController(RunnerService runnerService, AccessService accessService) {
        this.runnerService = runnerService;
        this.accessService = accessService;
    }

    /**
     * Admin Endpoint: Retrieves a list of ALL registered runners.
     * <p>
     * This endpoint is restricted to administrators only. It is typically used for
     * an admin dashboard to view the user base.
     * </p>
     * @param auth The security context used to verify if the requester has {@code ROLE_ADMIN}.
     * @return A list of all users in {@link RunnerResponse} format.
     * @throws ForbiddenException If a regular user tries to access this endpoint.
     */
    @GetMapping
    public ResponseEntity<List<RunnerResponse>> getAllRunners(Authentication auth) {
        if (!accessService.isAdmin(auth)) {
            throw new ForbiddenException("Access denied. Admins only.");
        }

        List<RunnerResponse> runners = runnerService.getAllRunners()
                .stream()
                .map(RunnerMapper::toResponse)
                .toList();

        return ResponseEntity.ok(runners);
    }
    /**
     * Retrieves the profile details of a specific runner by ID.
     * <p>
     * Used when viewing a specific user's profile.
     * Requires the requester to be the owner of the account or an admin.
     * </p>
     * @param id             The unique ID of the runner to retrieve.
     * @param authentication The security context of the current user.
     * @return The {@link RunnerResponse} containing public profile data.
     * @throws ForbiddenException If the logged-in user is not allowed to view this profile.
     * @throws NotFoundException  If the runner ID does not exist in the database.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RunnerResponse> getRunner(
            @PathVariable Long id,
            Authentication authentication
    ) {
        accessService.checkRunnerAccess(authentication, id);

        return runnerService.getRunnerById(id)
                .map(RunnerMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Runner not found"));
    }

    /**
     * Convenience Endpoint: Retrieves the CURRENTLY logged-in user's profile.
     * <p>
     * <b>Why is this important?</b>
     * When the Flutter app starts, it has a Token but doesn't necessarily know the user's ID.
     * Calling {@code /runners/me} allows the app to say "Tell me who I am" and get the full profile,
     * including the ID needed for other requests.
     * </p>
     * @param authentication The security context containing the username from the JWT token.
     * @return The {@link RunnerResponse} for the current user.
     * @throws NotFoundException If the user in the token no longer exists in the database.
     */
    @GetMapping("/me")
    public ResponseEntity<RunnerResponse> getMe(Authentication authentication) {
        String username = authentication.getName();
        return runnerService.getRunnerByUsername(username)
                .map(RunnerMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Updates the profile of an existing runner.
     * <p>
     * Allows changing the email and weight.
     * The input is validated using {@code @Valid} (e.g., checking email format).
     * </p>
     * @param id             The ID of the runner to update.
     * @param request        The {@link UpdateRunnerRequest} DTO containing new values.
     * @param authentication The security context to verify ownership.
     * @return The updated profile data.
     * @throws ForbiddenException If trying to update someone else's profile.
     * @throws com.pavel.jogger.web.exception.ConflictException If the new email is already taken.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RunnerResponse> updateRunner(
            @PathVariable Long id,
            @RequestBody @Valid UpdateRunnerRequest request,
            Authentication authentication
    ) {
        accessService.checkRunnerAccess(authentication, id);

        return ResponseEntity.ok(
                RunnerMapper.toResponse(
                        runnerService.updateRunner(id, request.getEmail(), request.getWeight())
                )
        );
    }

    /**
     * Permanently deletes a runner's account.
     * <p>
     * This action is irreversible. It triggers a cascade delete in the database,
     * removing all associated activities and badges (depending on DB configuration).
     * </p>
     * @param id             The ID of the account to delete.
     * @param authentication The security context to verify ownership.
     * @return HTTP 204 No Content upon success.
     * @throws ForbiddenException If trying to delete someone else's account.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRunner(
            @PathVariable Long id,
            Authentication authentication
    ) {
        accessService.checkRunnerAccess(authentication, id);

        runnerService.deleteRunner(id);
        return ResponseEntity.noContent().build();
    }
}