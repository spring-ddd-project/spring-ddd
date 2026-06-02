package com.springddd.domain.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class IdTempTest {

    @Test
    void shouldGenerateUniqueIds() {
        long id1 = IdTemp.generateId();
        long id2 = IdTemp.generateId();

        assertTrue(id2 > id1, "Second generated ID should be greater than first");
    }

    @Test
    void shouldGenerateIdGreaterThanZero() {
        long id = IdTemp.generateId();

        assertTrue(id > 0, "Generated ID should be greater than zero");
    }

    @Test
    void shouldGenerateMultipleUniqueIds() {
        long[] ids = new long[10];
        for (int i = 0; i < 10; i++) {
            ids[i] = IdTemp.generateId();
        }

        for (int i = 0; i < 10; i++) {
            for (int j = i + 1; j < 10; j++) {
                assertNotEquals(ids[i], ids[j], "Each generated ID should be unique");
            }
        }
    }

    @Test
    void shouldGenerateIdsWithCorrectFormat() {
        long timestamp = System.currentTimeMillis();
        long id = IdTemp.generateId();

        long idTimestamp = id / 1000;
        assertEquals(timestamp, idTimestamp, 1, "ID timestamp should match current timestamp");
    }

    @Test
    void shouldGenerateIdsRapidly() {
        long id1 = IdTemp.generateId();
        long id2 = IdTemp.generateId();
        long id3 = IdTemp.generateId();

        assertTrue(id3 >= id2, "IDs should be generated in non-decreasing order");
        assertTrue(id2 >= id1, "IDs should be generated in non-decreasing order");
    }

    @Test
    void shouldHandleSequenceRollover() throws InterruptedException {
        // Generate many IDs rapidly to trigger sequence rollover
        long firstId = IdTemp.generateId();
        for (int i = 0; i < 2000; i++) {
            long id = IdTemp.generateId();
            assertTrue(id > 0, "Generated ID should be positive at iteration " + i);
        }
        long lastId = IdTemp.generateId();
        assertTrue(lastId > firstId, "Last ID should be greater than first ID");
    }

    @Test
    void shouldResetSequence_whenMaxSequenceExceeded() throws Exception {
        Field lastTimestampField = IdTemp.class.getDeclaredField("lastTimestamp");
        lastTimestampField.setAccessible(true);
        Field sequenceField = IdTemp.class.getDeclaredField("sequence");
        sequenceField.setAccessible(true);

        long current = System.currentTimeMillis();
        lastTimestampField.setLong(null, current);
        sequenceField.setInt(null, 999);

        long id = IdTemp.generateId();
        long timestamp = id / 1000;

        assertTrue(timestamp >= current, "Timestamp should be >= current after overflow handling");
    }
}
