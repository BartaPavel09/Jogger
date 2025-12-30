package com.pavel.jogger.service;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.persistence.entity.BadgeEntity;
import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.repository.ActivityRepository;
import com.pavel.jogger.persistence.repository.BadgeRepository;
import com.pavel.jogger.web.dto.badge.BadgeResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final ActivityRepository activityRepository;

    public BadgeService(BadgeRepository badgeRepository,
                        ActivityRepository activityRepository) {
        this.badgeRepository = badgeRepository;
        this.activityRepository = activityRepository;
    }

    
    public List<BadgeResponse> getBadgesForRunner(Long runnerId) {
        return badgeRepository.findByRunnerId(runnerId)
                .stream()
                .map(b -> {
                    String type = "BRONZE"; 

                    
                    if (b.getCode().contains("10K") || b.getCode().contains("100_KM")) {
                        type = "SILVER";
                    } else if (b.getCode().contains("MARATHON") || b.getCode().contains("1000_KM") || b.getCode().contains("50_RUNS")) {
                        type = "GOLD";
                    }

                    return new BadgeResponse(
                            b.getName(),
                            b.getDescription(),
                            type,
                            b.getAwardedAt(),
                            b.isSeen()
                    );
                })
                .toList();
    }

    public void markBadgesAsSeen(Long runnerId) {
        List<BadgeEntity> badges = badgeRepository.findByRunnerId(runnerId);
        for (BadgeEntity b : badges) {
            if (!b.isSeen()) b.setSeen(true);
        }
        badgeRepository.saveAll(badges);
    }

    @Async("badgeExecutor")
    public void evaluateBadgesAsync(RunnerEntity runner, ActivityEntity newActivity) {
        checkSingleRunDistance(runner, newActivity);
        checkTotalStats(runner);
    }

    private void checkSingleRunDistance(RunnerEntity runner, ActivityEntity activity) {
        double dist = activity.getDistanceKm();

        
        if (dist >= 5.0) {
            assignBadge(runner, "FIRST_5K", "First 5K", "Completed a run of at least 5 km");
        }

        
        if (dist >= 10.0) {
            assignBadge(runner, "FIRST_10K", "10K Finisher", "Completed a run of at least 10 km");
        }

        
        if (dist >= 21.0) {
            assignBadge(runner, "HALF_MARATHON", "Half Marathon", "Ran 21 km in one go!");
        }
    }

    private void checkTotalStats(RunnerEntity runner) {
        long totalRuns = activityRepository.countByRunner(runner);
        Double totalDistance = activityRepository.sumDistanceByRunner(runner);

        
        if (totalRuns >= 1) {
            assignBadge(runner, "FIRST_RUN", "First Steps", "Completed your very first run");
        }
        if (totalRuns >= 10) {
            assignBadge(runner, "10_RUNS", "Consistent", "Completed 10 runs total");
        }
        if (totalRuns >= 50) {
            assignBadge(runner, "50_RUNS", "Dedicated", "Completed 50 runs total");
        }

        
        if (totalDistance >= 100.0) {
            assignBadge(runner, "DIST_100_KM", "Century Club", "Ran a total of 100 km");
        }
        if (totalDistance >= 500.0) {
            assignBadge(runner, "DIST_500_KM", "Pro Runner", "Ran a total of 500 km");
        }
        if (totalDistance >= 1000.0) {
            assignBadge(runner, "DIST_1000_KM", "Kilometer Eater", "Ran a total of 1000 km");
        }
    }

    
    private void assignBadge(RunnerEntity runner, String code, String name, String description) {
        
        if (badgeRepository.findByRunnerAndCode(runner, code).isPresent()) {
            return;
        }

        BadgeEntity badge = new BadgeEntity();
        badge.setCode(code);
        badge.setName(name);
        badge.setDescription(description);
        badge.setAwardedAt(LocalDate.now());
        badge.setRunner(runner);
        badge.setSeen(false);

        badgeRepository.save(badge);
    }
}