package com.pavel.jogger.service;

import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.repository.RunnerRepository;
import com.pavel.jogger.web.exception.ConflictException;
import com.pavel.jogger.web.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class RunnerService {

    private final RunnerRepository runnerRepository;

    public RunnerService(RunnerRepository runnerRepository) {
        this.runnerRepository = runnerRepository;
    }

    
    public Optional<RunnerEntity> getRunnerById(Long id) {
        return runnerRepository.findById(id);
    }

    public Optional<RunnerEntity> getRunnerByUsername(String username) {
        return runnerRepository.findByUsername(username);
    }

    
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

    
    public void deleteRunner(Long id) {
        if (!runnerRepository.existsById(id)) {
            throw new NotFoundException("Runner not found");
        }
        runnerRepository.deleteById(id);
    }

    public List<RunnerEntity> getAllRunners() {
        return runnerRepository.findAll();
    }
}
