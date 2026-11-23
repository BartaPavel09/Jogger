package com.pavel.jogger.config;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.entity.UserFollowEntity;
import com.pavel.jogger.persistence.repository.ActivityRepository;
import com.pavel.jogger.persistence.repository.RunnerRepository;
import com.pavel.jogger.persistence.repository.UserFollowRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Initializes default sample users and activities on application startup
 * if the runners repository is empty.
 */
@Configuration
public class DataSeederConfig {

    private static final Logger log = LoggerFactory.getLogger(DataSeederConfig.class);

    @Bean
    public CommandLineRunner initDatabase(
            RunnerRepository runnerRepository,
            ActivityRepository activityRepository,
            UserFollowRepository userFollowRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (runnerRepository.count() > 0) {
                return;
            }

            log.info("Populating database with sample data...");

            String rawPassword = "password123";
            String passwordHash = passwordEncoder.encode(rawPassword);

            RunnerEntity admin = new RunnerEntity("admin", "admin@jogger.com", passwordHash);
            admin.setRole("ADMIN");
            admin.setWeight(75.0);
            admin.setDateJoined(LocalDateTime.now().minusMonths(3));
            runnerRepository.save(admin);

            RunnerEntity alex = new RunnerEntity("alex_marathon", "alex@jogger.com", passwordHash);
            alex.setRole("USER");
            alex.setWeight(68.0);
            alex.setDateJoined(LocalDateTime.now().minusMonths(2));
            alex = runnerRepository.save(alex);

            createActivity(activityRepository, alex, 21.1, 6300, LocalDate.now().minusDays(18), "Riverside Half-Marathon", 1480);
            createActivity(activityRepository, alex, 15.0, 4500, LocalDate.now().minusDays(14), "Hill Trail Challenge", 1050);
            createActivity(activityRepository, alex, 18.0, 5400, LocalDate.now().minusDays(9), "Long Weekend Run", 1260);
            createActivity(activityRepository, alex, 16.0, 4800, LocalDate.now().minusDays(5), "Tempo Park Loop", 1120);
            createActivity(activityRepository, alex, 14.9, 4400, LocalDate.now().minusDays(1), "Evening City Loop", 1040);

            RunnerEntity sarah = new RunnerEntity("sarah_runs", "sarah@jogger.com", passwordHash);
            sarah.setRole("USER");
            sarah.setWeight(58.0);
            sarah.setDateJoined(LocalDateTime.now().minusMonths(2));
            sarah = runnerRepository.save(sarah);

            createActivity(activityRepository, sarah, 12.0, 3960, LocalDate.now().minusDays(16), "Lake Shore Trail", 720);
            createActivity(activityRepository, sarah, 10.0, 3300, LocalDate.now().minusDays(12), "Sunset 10K", 600);
            createActivity(activityRepository, sarah, 15.0, 4950, LocalDate.now().minusDays(7), "Forest Nature Path", 900);
            createActivity(activityRepository, sarah, 8.0, 2640, LocalDate.now().minusDays(4), "Morning Recovery Jog", 480);
            createActivity(activityRepository, sarah, 7.5, 2475, LocalDate.now().minusDays(2), "Quick Park Interval", 450);

            RunnerEntity david = new RunnerEntity("david_pace", "david@jogger.com", passwordHash);
            david.setRole("USER");
            david.setWeight(74.0);
            david.setDateJoined(LocalDateTime.now().minusMonths(1));
            david = runnerRepository.save(david);

            createActivity(activityRepository, david, 11.0, 3630, LocalDate.now().minusDays(15), "Downtown Sunrise Run", 840);
            createActivity(activityRepository, david, 10.0, 3300, LocalDate.now().minusDays(10), "Neighborhood Circuit", 770);
            createActivity(activityRepository, david, 10.0, 3300, LocalDate.now().minusDays(3), "Campus 10K", 770);

            RunnerEntity demo = new RunnerEntity("demo_runner", "demo@jogger.com", passwordHash);
            demo.setRole("USER");
            demo.setWeight(70.0);
            demo.setDateJoined(LocalDateTime.now().minusDays(10));
            demo = runnerRepository.save(demo);

            userFollowRepository.save(new UserFollowEntity(demo, alex));
            userFollowRepository.save(new UserFollowEntity(demo, sarah));
            userFollowRepository.save(new UserFollowEntity(david, alex));

            log.info("Default seed data successfully populated.");
            log.info("Demo users created with default password: '{}'", rawPassword);
        };
    }

    private void createActivity(
            ActivityRepository activityRepository,
            RunnerEntity runner,
            double distanceKm,
            int durationSec,
            LocalDate date,
            String route,
            int calories
    ) {
        ActivityEntity activity = new ActivityEntity();
        activity.setRunner(runner);
        activity.setDistanceKm(distanceKm);
        activity.setDurationSec(durationSec);
        activity.setDate(date);
        activity.setRoute(route);
        activity.setCalories(calories);
        activityRepository.save(activity);
    }
}
