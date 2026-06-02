package com.springddd.infrastructure.persistence.leaf;

import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
public class LeafSegmentBuffer {

    private String bizTag;

    private final LeafSegment[] segments = new LeafSegment[]{new LeafSegment(), new LeafSegment()};

    private volatile int currentPos;

    private volatile boolean nextReady;

    private final AtomicBoolean threadRunning = new AtomicBoolean(false);

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    private volatile long stepUpdateTime;

    private volatile int minStep;

    public LeafSegmentBuffer(String bizTag) {
        this.bizTag = bizTag;
    }

    public LeafSegment getCurrent() {
        return segments[currentPos];
    }

    public LeafSegment getNext() {
        return segments[nextPos()];
    }

    public int nextPos() {
        return (currentPos + 1) & 1;
    }

    public void switchPos() {
        currentPos = nextPos();
    }

    public Lock rLock() {
        return lock.readLock();
    }

    public Lock wLock() {
        return lock.writeLock();
    }

    public boolean isInitOk() {
        return getCurrent().getStep() > 0;
    }
}
