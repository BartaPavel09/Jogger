package com.pavel.jogger.persistence.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RunnerEntityTest {

    @Test
    void constructor_shouldSetFieldsCorrectly() {
        RunnerEntity runner = new RunnerEntity(
                "testuser",
                "test@test.com",
                "hashedPassword"
        );

        assertEquals("testuser", runner.getUsername());
        assertEquals("test@test.com", runner.getEmail());
        assertEquals("hashedPassword", runner.getPasswordHash());
        assertNotNull(runner.getDateJoined());
    }
}
