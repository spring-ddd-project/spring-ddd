package com.springddd.domain.util;

public class IdTemp {

    private static long lastTimestamp = -1L;
    private static int sequence = 0;
    private static final int MAX_SEQUENCE = 999;

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

