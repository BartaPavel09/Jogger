package com.pavel.jogger.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing a follow relationship between two runners.
 * <p>
 * Maps to the "user_follows" table.
 * A runner (follower) can follow another runner (followed).
 * </p>
 */
@Entity
@Table(
        name = "user_follows",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_user_follows",
                        columnNames = {"follower_id", "followed_id"}
                )
        }
)
public class UserFollowEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private RunnerEntity follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "followed_id", nullable = false)
    private RunnerEntity followed;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public UserFollowEntity() {
    }

    public UserFollowEntity(RunnerEntity follower, RunnerEntity followed) {
        this.follower = follower;
        this.followed = followed;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public RunnerEntity getFollower() {
        return follower;
    }

    public void setFollower(RunnerEntity follower) {
        this.follower = follower;
    }

    public RunnerEntity getFollowed() {
        return followed;
    }

    public void setFollowed(RunnerEntity followed) {
        this.followed = followed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
