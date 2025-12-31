package com.pavel.jogger.persistence.repository;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.persistence.entity.RunnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for accessing Activity data.
 * <p>
 * This interface extends JpaRepository, which provides standard CRUD operations
 * (Create, Read, Update, Delete) automatically. We also define custom queries here
 * to fetch statistics specifically for our jogging app.
 * </p>
 */
public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {

    /**
     * Finds all activities belonging to a specific runner.
     * <p>
     * Spring Data JPA automatically generates the SQL for this based on the method name.
     * SQL equivalent: SELECT * FROM activities WHERE runner_id = ?
     * </p>
     * @param runnerId The ID of the user.
     * @return A list of activities sorted by default order.
     */
    List<ActivityEntity> findByRunnerId(Long runnerId);

    /**
     * Counts how many activities a specific runner has.
     * <p>
     * Useful for checking badge conditions (e.g., "10th Run Badge").
     * </p>
     * @param runner The runner entity object.
     * @return The total count of activities.
     */
    long countByRunner(RunnerEntity runner);

    /**
     * Calculates the total distance run by a user using a custom JPQL query.
     * @param runnerId The ID of the user.
     * @return The sum of distanceKm for all activities of this runner. Can be null if no runs exist.
     */
    @Query("""
        SELECT SUM(a.distanceKm)
        FROM ActivityEntity a
        WHERE a.runner.id = :runnerId
    """)
    Double totalDistance(@Param("runnerId") Long runnerId);

    /**
     * Counts the total activities for a user (Alternative to countByRunner).
     * <p>
     * This uses a custom query to do the exact same thing as countByRunner,
     * but explicitly selects the count based on the ID.
     * </p>
     * @param runnerId The ID of the user.
     * @return The count of rows.
     */
    @Query("""
        SELECT COUNT(a)
        FROM ActivityEntity a
        WHERE a.runner.id = :runnerId
    """)
    Long totalActivities(@Param("runnerId") Long runnerId);

    /**
     * Calculates total distance ensuring a non-null result.
     * <p>
     * COALESCE is a SQL function that returns the first non-null value.
     * If SUM returns null (user has 0 runs), this returns 0 instead.
     * This is safer than 'totalDistance' above which might return null.
     * </p>
     * @param runner The runner entity.
     * @return The total distance or 0.0 if no runs found.
     */
    @Query("SELECT COALESCE(SUM(a.distanceKm), 0) FROM ActivityEntity a WHERE a.runner = :runner")
    Double sumDistanceByRunner(@Param("runner") RunnerEntity runner);
}
