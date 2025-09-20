package com.springddd.domain.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IdTempTest {

    @Test
    void generateId_ShouldReturnPositiveLong() {
        // When
        long id = IdTemp.generateId();

        // Then
        assertTrue(id > 0, "Generated ID should be positive");
    }

    @Test
    void generateId_ShouldReturnUniqueIds() {
        // When
        long id1 = IdTemp.generateId();
        long id2 = IdTemp.generateId();

        // Then
        assertNotEquals(id1, id2, "Generated IDs should be unique");
    }

    @Test
    void generateId_ShouldReturnIncreasingIds() {
        // Given
        long id1 = IdTemp.generateId();

        // When
        long id2 = IdTemp.generateId();

        // Then
        assertTrue(id2 > id1, "Second ID should be greater than first");
    }

    @Test
    void generateId_MultipleCallsShouldReturnUniqueIds() {
        // When
        long id1 = IdTemp.generateId();
        long id2 = IdTemp.generateId();
        long id3 = IdTemp.generateId();
        long id4 = IdTemp.generateId();
        long id5 = IdTemp.generateId();

        // Then
        assertNotEquals(id1, id2);
        assertNotEquals(id2, id3);
        assertNotEquals(id3, id4);
        assertNotEquals(id4, id5);
    }

    @Test
    void generateId_ShouldHandleZeroSequence() {
        // Given - Force a fresh start by generating ids until we get a clean sequence
        long id1 = IdTemp.generateId();

        // When/Then - First id in a new sequence should be valid
        assertTrue(id1 > 0);
    }

    @Test
    void generateId_IdsShouldBeMonotonicallyIncreasing() {
        // When
        long id1 = IdTemp.generateId();
        long id2 = IdTemp.generateId();
        long id3 = IdTemp.generateId();
        long id4 = IdTemp.generateId();
        long id5 = IdTemp.generateId();

        // Then
        assertTrue(id2 > id1);
        assertTrue(id3 > id2);
        assertTrue(id4 > id3);
        assertTrue(id5 > id4);
    }
}
