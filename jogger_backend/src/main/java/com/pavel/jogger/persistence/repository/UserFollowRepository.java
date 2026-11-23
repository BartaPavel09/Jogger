package com.pavel.jogger.persistence.repository;

import com.pavel.jogger.persistence.entity.RunnerEntity;
import com.pavel.jogger.persistence.entity.UserFollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing follow relationships between users.
 */
public interface UserFollowRepository extends JpaRepository<UserFollowEntity, Long> {

    boolean existsByFollowerAndFollowed(RunnerEntity follower, RunnerEntity followed);

    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);

    Optional<UserFollowEntity> findByFollowerAndFollowed(RunnerEntity follower, RunnerEntity followed);

    Optional<UserFollowEntity> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    void deleteByFollowerAndFollowed(RunnerEntity follower, RunnerEntity followed);

    void deleteByFollowerIdOrFollowedId(Long followerId, Long followedId);

    List<UserFollowEntity> findByFollower(RunnerEntity follower);

    List<UserFollowEntity> findByFollowerId(Long followerId);

    @Query("SELECT f.followed.id FROM UserFollowEntity f WHERE f.follower.id = :followerId")
    List<Long> findFollowedIdsByFollowerId(@Param("followerId") Long followerId);

    long countByFollower(RunnerEntity follower);

    long countByFollowed(RunnerEntity followed);
}
