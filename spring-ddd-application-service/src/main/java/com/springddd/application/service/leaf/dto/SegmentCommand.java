package com.springddd.application.service.leaf.dto;

import java.util.concurrent.atomic.AtomicLong;

public record SegmentCommand(SegmentBufferCommand buffer, AtomicLong value, long max, int step) {

    public SegmentCommand(SegmentBufferCommand buffer) {
        this(buffer, new AtomicLong(0), 0L, 0);
    }

    public SegmentCommand() {

    }

    // Getter for idle computation
    public long getIdle() {
        return max - value.get();
    }

    @Override
    public String toString() {
        return String.format("Segment(value=%s, max=%d, step=%d)", value, max, step);
    }
}

