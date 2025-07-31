package com.springddd.application.service.leaf.dto;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 双buffer
 */
public record SegmentBufferCommand(
        String key,
        SegmentCommand[] segments,
        int currentPos,
        boolean nextReady,
        boolean initOk,
        AtomicBoolean threadRunning,
        ReadWriteLock lock,
        int step,
        int minStep,
        long updateTimestamp
) {

    // 自定义构造函数，初始化默认值
    public SegmentBufferCommand(String key) {
        this(key, new SegmentCommand[]{new SegmentCommand()}, 0, false, false, new AtomicBoolean(false), new ReentrantReadWriteLock(), 0, 0, 0L);
    }

    // 获取当前Segment
    public SegmentCommand getCurrent() {
        return segments[currentPos];
    }

    // 计算下一个Segment的索引
    public int nextPos() {
        return (currentPos + 1) % 2;
    }

    // 创建一个新的实例，并切换 Segment
    public SegmentBufferCommand switchPos() {
        int newPos = nextPos();
        return new SegmentBufferCommand(key, segments, newPos, nextReady, initOk, threadRunning, lock, step, minStep, updateTimestamp);
    }

    // 获取读锁
    public Lock rLock() {
        return lock.readLock();
    }

    // 获取写锁
    public Lock wLock() {
        return lock.writeLock();
    }

    @Override
    public String toString() {
        return "SegmentBuffer{" +
                "key='" + key + '\'' +
                ", segments=" + Arrays.toString(segments) +
                ", currentPos=" + currentPos +
                ", nextReady=" + nextReady +
                ", initOk=" + initOk +
                ", threadRunning=" + threadRunning +
                ", step=" + step +
                ", minStep=" + minStep +
                ", updateTimestamp=" + updateTimestamp +
                '}';
    }
}