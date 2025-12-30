package com.pavel.jogger.web.controller;

import com.pavel.jogger.persistence.entity.RunnerEntity;
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

@RestController
@RequestMapping("/runners")
public class RunnerController {

    private final RunnerService runnerService;
    private final AccessService accessService;

    public RunnerController(RunnerService runnerService, AccessService accessService) {
        this.runnerService = runnerService;
        this.accessService = accessService;
    }

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

    
    @GetMapping("/me")
    public ResponseEntity<RunnerResponse> getMe(Authentication authentication) {
        String username = authentication.getName();
        return runnerService.getRunnerByUsername(username)
                .map(RunnerMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    
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