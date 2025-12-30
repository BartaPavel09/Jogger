package com.pavel.jogger.persistence.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class BadgeEntityTest {

    @Test
    void entity_shouldStoreValuesUsingSetters() {
        RunnerEntity runner = new RunnerEntity("user", "user@test.com", "hash");

        BadgeEntity badge = new BadgeEntity();
        badge.setName("First 5 km run");
        badge.setDescription("Completed first 5 km");
        badge.setCode("FIRST_5K");
        badge.setSeen(false);
        badge.setAwardedAt(LocalDate.now());
        badge.setRunner(runner);

        assertEquals("First 5 km run", badge.getName());
        assertEquals("FIRST_5K", badge.getCode());
        assertEquals(runner, badge.getRunner());
        assertFalse(badge.isSeen());
        assertNull(badge.getId());
    }

    @Test
    void constructor_shouldSetFieldsCorrectly() {
        RunnerEntity runner = new RunnerEntity("user", "user@test.com", "hash");

        BadgeEntity badge = new BadgeEntity(
                "FIRST_10K",
                "10K Run",
                "Ran 10km",
                runner
        );

        assertEquals("FIRST_10K", badge.getCode());
        assertEquals("10K Run", badge.getName());
        assertEquals("Ran 10km", badge.getDescription());
        assertEquals(runner, badge.getRunner());
        assertNotNull(badge.getAwardedAt());
        assertFalse(badge.isSeen());
    }
}