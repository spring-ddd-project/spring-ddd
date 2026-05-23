package com.springddd.domain.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IdTempTest {

    @Test
    void testConstructor() {
        IdTemp idTemp = new IdTemp();
        assertThat(idTemp).isNotNull();
    }

    @Test
    void testGenerateId() {
        long id1 = IdTemp.generateId();
        long id2 = IdTemp.generateId();
        assertThat(id1).isPositive();
        assertThat(id2).isPositive();
        assertThat(id2).isGreaterThanOrEqualTo(id1);
    }

    @Test
    void testGenerateIdUnique() {
        long id1 = IdTemp.generateId();
        long id2 = IdTemp.generateId();
        assertThat(id1).isNotEqualTo(id2);
    }

    @Test
    void testGenerateIdSequenceOverflow() throws Exception {
        java.lang.reflect.Field lastTimestampField = IdTemp.class.getDeclaredField("lastTimestamp");
        lastTimestampField.setAccessible(true);
        java.lang.reflect.Field sequenceField = IdTemp.class.getDeclaredField("sequence");
        sequenceField.setAccessible(true);

        // Reset state
        lastTimestampField.setLong(null, -1L);
        sequenceField.setInt(null, 0);

        // Establish a baseline timestamp by calling generateId once
        IdTemp.generateId();
        long baseline = lastTimestampField.getLong(null);

        boolean overflowTriggered = false;
        for (int attempt = 0; attempt < 50000 && !overflowTriggered; attempt++) {
            long currentTime = System.currentTimeMillis();
            if (currentTime == baseline) {
                sequenceField.setInt(null, 999);
                IdTemp.generateId();
                // After overflow branch: sequence is reset to 0 and lastTimestamp should be >= baseline
                if (sequenceField.getInt(null) == 0 && lastTimestampField.getLong(null) >= baseline) {
                    overflowTriggered = true;
                }
            } else {
                // Time advanced, update baseline
                baseline = lastTimestampField.getLong(null);
            }
        }
        assertThat(overflowTriggered).isTrue();
    }
}
