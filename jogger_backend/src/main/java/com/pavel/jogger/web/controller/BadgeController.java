package com.pavel.jogger.web.controller;

import com.pavel.jogger.service.AccessService;
import com.pavel.jogger.service.BadgeService;
import com.pavel.jogger.web.dto.badge.BadgeResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for managing user achievements (badges).
 * <p>
 * This controller handles the "Trophy Room" aspect of the application.
 * It provides endpoints to retrieve earned badges and to manage their "seen" state
 * </p>
 */
@RestController
@RequestMapping("/runners")
public class BadgeController {

    private final BadgeService badgeService;
    private final AccessService accessService;

    public BadgeController(BadgeService badgeService, AccessService accessService) {
        this.badgeService = badgeService;
        this.accessService = accessService;
    }

    /**
     * Retrieves the list of badges earned by a specific runner.
     * <p>
     * This endpoint is called when the user opens the "Awards" or "Profile" screen.
     * It includes details like the badge name, description, and whether it has been seen yet.
     * </p>
     * @param id             The unique ID of the runner.
     * @param authentication The security context of the current user.
     * @return A list of {@link BadgeResponse} DTOs.
     * @throws com.pavel.jogger.web.exception.ForbiddenException If the user tries to view someone else's badges.
     */
    @GetMapping("/{id}/badges")
    public List<BadgeResponse> getBadgesForRunner(
            @PathVariable Long id,
            Authentication authentication
    ) {
        accessService.checkRunnerAccess(authentication, id);
        return badgeService.getBadgesForRunner(id);
    }

    /**
     * Marks all badges for a specific runner as "seen".
     * <p>
     * <b>UI Logic:</b> When a user earns a new badge, the app shows a popup.
     * This endpoint is called automatically by the frontend when the user
     * earns a badge, telling the backend to remove the "new" status.
     * </p>
     * @param id             The unique ID of the runner.
     * @param authentication The security context of the current user.
     * @throws com.pavel.jogger.web.exception.ForbiddenException If the user tries to modify someone else's badge state.
     */
    @PostMapping("/{id}/badges/seen")
    public void markBadgesSeen(
            @PathVariable Long id,
            Authentication authentication
    ) {
        accessService.checkRunnerAccess(authentication, id);
        badgeService.markBadgesAsSeen(id);
    }
}