package com.pavel.jogger.persistence.repository;

import com.pavel.jogger.persistence.entity.ActivityEntity;
import com.pavel.jogger.persistence.entity.RunnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ActivityRepository extends JpaRepository<ActivityEntity, Long> {

    List<ActivityEntity> findByRunnerId(Long runnerId);

    long countByRunner(RunnerEntity runner);

    @Query("""
        SELECT SUM(a.distanceKm)
        FROM ActivityEntity a
        WHERE a.runner.id = :runnerId
    """)
    Double totalDistance(@Param("runnerId") Long runnerId);

    @Query("""
        SELECT COUNT(a)
        FROM ActivityEntity a
        WHERE a.runner.id = :runnerId
    """)
    Long totalActivities(@Param("runnerId") Long runnerId);

    @Query("SELECT COALESCE(SUM(a.distanceKm), 0) FROM ActivityEntity a WHERE a.runner = :runner")
    Double sumDistanceByRunner(@Param("runner") RunnerEntity runner);
}
