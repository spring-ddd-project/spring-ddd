package com.springddd.domain.util;

public class IdTemp {

    private static long lastTimestamp;
    private static int sequence;
    private static final int MAX_SEQUENCE;

    static {
        lastTimestamp = -1L;
        sequence = 0;
        MAX_SEQUENCE = 999;
    }

    private IdTemp() {
    }

    public static synchronized long generateId() {
        long timestamp = System.currentTimeMillis();
        if (timestamp == lastTimestamp) {
            sequence++;
            if (sequence > MAX_SEQUENCE) {
                while (timestamp <= lastTimestamp) {
                    timestamp = System.currentTimeMillis();
                }
                sequence = 0;
                lastTimestamp = timestamp;
            }
        } else {
            sequence = 0;
            lastTimestamp = timestamp;
        }

        return timestamp * 1000 + sequence;
    }
}

