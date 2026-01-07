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

/**
 * Service responsible for the Gamification logic (Badges).
 * <p>
 * This service checks rules defined in code against the user's statistics
 * and awards badges if criteria are met.
 * </p>
 */
@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final ActivityRepository activityRepository;

    public BadgeService(BadgeRepository badgeRepository,
                        ActivityRepository activityRepository) {
        this.badgeRepository = badgeRepository;
        this.activityRepository = activityRepository;
    }

    /**
     * Retrieves badges for a runner and maps them to a response DTO.
     * <p>
     * Logic included: Dynamically determines the "Type" (Gold/Silver/Bronze)
     * based on the badge code for UI styling purposes.
     * </p>
     * @param runnerId The user ID.
     * @return List of badge responses ready for the frontend.
     */
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

    /**
     * Marks all user's badges as "seen" so the notification icon can disappear.
     */
    public void markBadgesAsSeen(Long runnerId) {
        List<BadgeEntity> badges = badgeRepository.findByRunnerId(runnerId);
        for (BadgeEntity b : badges) {
            if (!b.isSeen()) b.setSeen(true);
        }
        badgeRepository.saveAll(badges);
    }

    /**
     * The MAIN logic for checking achievements.
     * <p>
     * Annotation @Async("badgeExecutor"):
     * This method runs on a separate thread (defined in AsyncConfig).
     * It prevents the "Add Activity" request from waiting for these calculations.
     * </p>
     * @param runner      The user to check.
     * @param newActivity The specific activity that just finished.
     */
    @Async("badgeExecutor")
    public void evaluateBadgesAsync(RunnerEntity runner, ActivityEntity newActivity) {
        checkSingleRunDistance(runner, newActivity);
        checkTotalStats(runner);
    }

    /**
     * Evaluates badges that depend on a single performance.
     * <p>
     * Example: "Run 10km in one go". This logic only cares about the current activity's stats.
     * </p>
     * @param runner   The user who performed the run.
     * @param activity The activity entity containing distance and duration.
     */
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

    /**
     * Evaluates badges that depend on lifetime statistics.
     * <p>
     * This method queries the database to get aggregated totals (SUM, COUNT)
     * and checks if cumulative milestones have been reached.
     * </p>
     * @param runner The user to check.
     */
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

    /**
     * Helper method to safely award a badge to a user.
     * <p>
     * Before saving, it checks if the user ALREADY has this badge code.
     * This prevents duplicate badges (e.g., getting "First 5K" every time you run 5K).
     * </p>
     * @param runner      The user receiving the badge.
     * @param code        Unique code identifier for the badge (e.g., "FIRST_5K").
     * @param name        Display name of the badge.
     * @param description Brief description of the achievement.
     */
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