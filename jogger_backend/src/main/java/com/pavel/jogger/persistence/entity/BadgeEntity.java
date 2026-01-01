package com.pavel.jogger.persistence.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entity representing an achievement unlocked by the user.
 * <p>
 * Maps to the "badges" table.
 * <b>Constraint:</b> The <code>@UniqueConstraint</code> ensures that a user cannot receive
 * the same badge code twice (e.g., you can't get "FIRST_5K" two times).
 * </p>
 */
@Entity
@Table(
        name = "badges",
        uniqueConstraints = @UniqueConstraint(columnNames = {"runner_id", "code"})
)
public class BadgeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * A unique code identifying the type of badge.
     * Used in the backend logic to check if a badge is already earned.
     */
    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "awarded_at")
    private LocalDate awardedAt;

    @Column(nullable = false)
    private boolean seen = false; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "runner_id", nullable = false)
    private RunnerEntity runner;

    public BadgeEntity() {}

    public BadgeEntity(String code, String name, String description, RunnerEntity runner) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.runner = runner;
        this.awardedAt = LocalDate.now();
        this.seen = false;
    }

    public Long getId() { return id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getAwardedAt() { return awardedAt; }
    public void setAwardedAt(LocalDate awardedAt) { this.awardedAt = awardedAt; }

    public boolean isSeen() { return seen; }
    public void setSeen(boolean seen) { this.seen = seen; }

    public RunnerEntity getRunner() { return runner; }
    public void setRunner(RunnerEntity runner) { this.runner = runner; }
}