package com.pavel.jogger.service;

import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.repository.RunnerRepository;
import com.pavel.jogger.web.exception.ForbiddenException;
import com.pavel.jogger.web.exception.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AccessService {

    private final RunnerRepository runnerRepository;

    public AccessService(RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

    public boolean isAdmin(Authentication authentication) {
        if (authentication == null) return false;

        for (GrantedAuthority a : authentication.getAuthorities()) {
            if ("ROLE_ADMIN".equals(a.getAuthority())) {
                return true;
            }
        }
        return false;
    }

    public RunnerEntity currentRunner(Authentication authentication) {
        if (authentication == null) {
            throw new ForbiddenException("Not authenticated");
        }

        String username = authentication.getName();

        return runnerRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Current user not found"));
    }

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
