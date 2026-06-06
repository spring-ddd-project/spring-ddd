package com.springddd.infrastructure.persistence.leaf;

import lombok.Data;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Leaf 双 Buffer 分段 ID 分配器。
 *
 * <p>使用 {@link DisruptorLock}（Disruptor Sequence + LockSupport）
 * 替代传统的 {@link java.util.concurrent.locks.ReentrantLock}，
 * 实现无锁、非阻塞的 segment 切换。
 */
@Data
public class LeafSegmentBuffer {

    private static final AtomicIntegerFieldUpdater<LeafSegmentBuffer> CURRENT_POS_UPDATER =
            AtomicIntegerFieldUpdater.newUpdater(LeafSegmentBuffer.class, "currentPos");

    private String bizTag;

    private final LeafSegment[] segments = new LeafSegment[]{new LeafSegment(), new LeafSegment()};

    private volatile int currentPos;

    private volatile boolean nextReady;

    private final AtomicBoolean threadRunning = new AtomicBoolean(false);

    /**
     * Disruptor 风格的无锁协调器：替代 ReentrantLock。
     */
    private final DisruptorLock disruptorLock = new DisruptorLock();

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

    /**
     * CAS 切换 currentPos。
     *
     * @param expected 期望的当前位置
     * @param next     目标位置
     * @return CAS 是否成功
     */
    public boolean casCurrentPos(int expected, int next) {
        return CURRENT_POS_UPDATER.compareAndSet(this, expected, next);
    }

    public boolean isInitOk() {
        return getCurrent().getStep() > 0;
    }
}
