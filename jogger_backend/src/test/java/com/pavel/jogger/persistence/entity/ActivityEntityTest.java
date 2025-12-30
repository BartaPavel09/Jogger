package com.pavel.jogger.persistence.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ActivityEntityTest {

    @Test
    void constructor_shouldSetFieldsCorrectly() {
        LocalDate date = LocalDate.now();

        ActivityEntity activity = new ActivityEntity(
                5.0,
                1500,
                date
        );

        assertEquals(5.0, activity.getDistanceKm());
        assertEquals(1500, activity.getDurationSec());
        assertEquals(date, activity.getDate());
    }
}
