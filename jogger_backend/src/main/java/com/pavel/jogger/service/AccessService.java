package com.pavel.jogger.service;

import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.repository.RunnerRepository;
import com.pavel.jogger.web.exception.ForbiddenException;
import com.pavel.jogger.web.exception.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * Service dedicated to authorization logic and user context retrieval.
 * <p>
 * This class acts as a security guard for service methods. It bridges the gap between
 * Spring Security's generic Authentication object and our specific RunnerEntity domain.
 * It ensures users can only modify their own data unless they are administrators.
 * </p>
 */
@Service
public class AccessService {

    private final RunnerRepository runnerRepository;

    public AccessService(RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

    /**
     * Checks if the currently authenticated user has administrative privileges.
     * <p>
     * Iterates through the authorities granted by Spring Security to find "ROLE_ADMIN".
     * </p>
     * @param authentication The security context of the current user.
     * @return true if the user is an admin, false otherwise.
     */
    public boolean isAdmin(Authentication authentication) {
        if (authentication == null) return false;

        for (GrantedAuthority a : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(a.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retrieves the full RunnerEntity corresponding to the logged-in user.
     * <p>
     * This is a helper method to get the "Current User" object from the database
     * based on the username stored in the token.
     * </p>
     * @param authentication The security context containing the username.
     * @return The RunnerEntity object for the current user.
     * @throws ForbiddenException If the authentication object is null (user not logged in).
     * @throws NotFoundException If the username in the token no longer exists in the database.
     */
    public RunnerEntity currentRunner(Authentication authentication) {
        if (authentication == null) {
            throw new ForbiddenException("Not authenticated");
        }

        String username = authentication.getName();

        return runnerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Current user not found"));
    }

    /**
     * Verifies if the current user is allowed to access data belonging to a specific runner ID.
     * <p>
     * Logic: <br>
     * 1. If the user is an ADMIN, access is always granted. <br>
     * 2. Otherwise, fetch the current user's entity. <br>
     * 3. Compare the current user's ID with the requested runnerId. <br>
     * 4. If they don't match, block access.
     * </p>
     * @param authentication The security context.
     * @param runnerId       The ID of the runner whose data is being accessed.
     * @throws ForbiddenException If the user is neither an admin nor the owner of the account.
     */
    public void checkRunnerAccess(Authentication authentication, Long runnerId) {
        if (isAdmin(authentication)) {
            return;
        }

        RunnerEntity me = currentRunner(authentication);

        if (!me.getId().equals(runnerId)) {
            throw new ForbiddenException("Access denied");
        }
    }
}
