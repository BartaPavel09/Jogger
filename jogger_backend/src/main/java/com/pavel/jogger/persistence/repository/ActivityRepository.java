package com.pavel.jogger.persistence.repository;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.persistence.entity.RunnerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * @return The sum of distanceKm for all activities of this runner, or 0.0 if no runs exist.
     */
    @Query("""
        SELECT COALESCE(SUM(a.distanceKm), 0.0)
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
     * Retrieves paginated activities posted exclusively by runners that the given runner follows,
     * ordered by newest date and id first.
     */
    @Query(
        value = """
            SELECT a FROM ActivityEntity a
            JOIN FETCH a.runner r
            WHERE r.id IN (
                SELECT f.followed.id FROM UserFollowEntity f WHERE f.follower.id = :currentRunnerId
            )
            ORDER BY a.date DESC, a.id DESC
        """,
        countQuery = """
            SELECT COUNT(a) FROM ActivityEntity a
            WHERE a.runner.id IN (
                SELECT f.followed.id FROM UserFollowEntity f WHERE f.follower.id = :currentRunnerId
            )
        """
    )
    Page<ActivityEntity> findFriendsFeed(@Param("currentRunnerId") Long currentRunnerId, Pageable pageable);

    /**
     * Computes the global distance leaderboard by aggregating cumulative distance per runner.
     * Uses: SUM(distance) GROUP BY user_id ORDER BY total_distance DESC
     */
    @Query("""
        SELECT a.runner.id AS runnerId, a.runner.username AS username, SUM(a.distanceKm) AS totalDistance, COUNT(a.id) AS totalRuns
        FROM ActivityEntity a
        GROUP BY a.runner.id, a.runner.username
        ORDER BY SUM(a.distanceKm) DESC
    """)
    List<LeaderboardProjection> findGlobalLeaderboard();
}
