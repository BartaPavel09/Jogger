package com.pavel.jogger.web.controller;

import com.pavel.jogger.service.AccessService;
import com.pavel.jogger.service.BadgeService;
import com.pavel.jogger.web.dto.badge.BadgeResponse;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/runners")
public class BadgeController {

    private final BadgeService badgeService;
    private final AccessService accessService;

    public BadgeController(BadgeService badgeService, AccessService accessService) {
        this.badgeService = badgeService;
        this.accessService = accessService;
    }

    
    @GetMapping("/{id}/badges")
    public List<BadgeResponse> getBadgesForRunner(
            @PathVariable Long id,
            Authentication authentication
    ) {
        accessService.checkRunnerAccess(authentication, id);
        return badgeService.getBadgesForRunner(id);
    }

    @PostMapping("/{id}/badges/seen")
    public void markBadgesSeen(
            @PathVariable Long id,
            Authentication authentication
    ) {
        accessService.checkRunnerAccess(authentication, id);
        badgeService.markBadgesAsSeen(id);
    }
}