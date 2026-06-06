package com.springddd.infrastructure.persistence.leaf;

import com.lmax.disruptor.Sequence;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Supplier;

/**
 * Disruptor 风格的无锁分段 ID 分配协调器。
 *
 * <p>使用 Disruptor 的 {@link Sequence}（缓存行填充）替代 AtomicLong，
 * 使用 {@link LockSupport#parkNanos(long)} 实现非阻塞等待（与 Disruptor 内部机制一致）。
 *
 * <p>核心流程：
 * <pre>
 *   1. 快路径：tryClaim() → CAS 分配 ID，无需锁
 *   2. 慢路径：ID 超出 available → CAS 切换 segment → 重试
 *   3. 等待：next segment 未就绪 → parkNanos 非阻塞等待
 * </pre>
 */
public class DisruptorLock {

    /**
     * 当前 segment 的 ID 分配游标。
     * Sequence 是缓存行填充的，避免 false sharing。
     */
    private final Sequence cursor = new Sequence(-1);

    /**
     * 当前 segment 的最大可用 ID（exclusive）。
     */
    private final Sequence available = new Sequence(-1);

    /**
     * 初始化 cursor 和 available。
     *
     * @param startValue 起始值（第一个分配的 ID）
     * @param maxValue   最大值（exclusive）
     */
    public void init(long startValue, long maxValue) {
        cursor.set(startValue - 1);
        available.set(maxValue);
    }

    /**
     * 切换到新 segment：重置 cursor 并更新 available。
     *
     * @param startValue 新 segment 起始值
     * @param maxValue   新 segment 最大值（exclusive）
     */
    public void switchToSegment(long startValue, long maxValue) {
        cursor.set(startValue - 1);
        available.set(maxValue);
    }

    /**
     * 尝试分配一个 ID。
     * 返回 claimed 值，调用方需自行判断是否 {@code claimed < max}。
     *
     * @return 分配到的 ID 值
     */
    public long tryClaim() {
        return cursor.incrementAndGet();
    }

    /**
     * 判断 claimed ID 是否仍在可用范围内。
     *
     * @param claimed 分配到的 ID
     * @return true 表示有效
     */
    public boolean isAvailable(long claimed) {
        return claimed < available.get();
    }

    /**
     * 获取当前 cursor 位置（用于阈值判断）。
     *
     * @return cursor 当前值
     */
    public long getCursor() {
        return cursor.get();
    }

    /**
     * 获取当前 available 上限。
     *
     * @return available 值
     */
    public long getAvailable() {
        return available.get();
    }

    /**
     * Disruptor 风格的非阻塞等待：使用 {@link LockSupport#parkNanos} 轮询条件。
     *
     * <p>与 {@link com.lmax.disruptor.BlockingWaitStrategy} 内部机制一致：
     * 当条件不满足时，线程被 park 而非自旋，条件满足时自动唤醒。
     *
     * @param condition    等待的条件供应器
     * @param timeoutNanos 最大等待时间（纳秒）
     * @return true 表示条件满足；false 表示超时
     */
    public static boolean waitFor(Supplier<Boolean> condition, long timeoutNanos) {
        if (condition.get()) {
            return true;
        }

        long deadline = System.nanoTime() + timeoutNanos;
        long maxPark = TimeUnit.MILLISECONDS.toNanos(1); // 每次 park 最多 1ms

        while (!condition.get()) {
            long remaining = deadline - System.nanoTime();
            if (remaining <= 0) {
                return false; // 超时
            }
            LockSupport.parkNanos(Math.min(remaining, maxPark));
        }
        return true;
    }
}
