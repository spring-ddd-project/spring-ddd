package com.springddd.infrastructure.persistence.leaf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class DisruptorLockTest {

    private DisruptorLock lock;

    @BeforeEach
    void setUp() {
        lock = new DisruptorLock();
    }

    @Test
    @DisplayName("init 后 tryClaim 应返回起始值")
    void tryClaim_shouldReturnStartValue_afterInit() {
        lock.init(1000L, 1100L);

        long claimed = lock.tryClaim();

        assertEquals(1000L, claimed);
        assertTrue(lock.isAvailable(claimed));
    }

    @Test
    @DisplayName("连续 tryClaim 应返回递增的 ID")
    void tryClaim_shouldReturnIncrementalIds() {
        lock.init(1000L, 1100L);

        assertEquals(1000L, lock.tryClaim());
        assertEquals(1001L, lock.tryClaim());
        assertEquals(1002L, lock.tryClaim());
    }

    @Test
    @DisplayName("isAvailable 应在 claimed < max 时返回 true")
    void isAvailable_shouldReturnTrue_whenClaimedLessThanMax() {
        lock.init(1000L, 1100L);

        assertTrue(lock.isAvailable(1000L));
        assertTrue(lock.isAvailable(1099L));
    }

    @Test
    @DisplayName("isAvailable 应在 claimed >= max 时返回 false")
    void isAvailable_shouldReturnFalse_whenClaimedEqualsOrGreaterThanMax() {
        lock.init(1000L, 1100L);

        assertFalse(lock.isAvailable(1100L));
        assertFalse(lock.isAvailable(1101L));
    }

    @Test
    @DisplayName("segment 耗尽后 tryClaim 返回的值 isAvailable 为 false")
    void tryClaim_shouldReturnUnavailableId_whenSegmentExhausted() {
        lock.init(1000L, 1002L); // 只有 2 个 ID: 1000, 1001

        lock.tryClaim(); // 1000
        lock.tryClaim(); // 1001
        long claimed = lock.tryClaim(); // 1002, exhausted

        assertFalse(lock.isAvailable(claimed));
    }

    @Test
    @DisplayName("switchToSegment 应重置 cursor 并更新 available")
    void switchToSegment_shouldResetCursorAndUpdateAvailable() {
        lock.init(1000L, 1100L);
        lock.tryClaim(); // 1000
        lock.tryClaim(); // 1001

        lock.switchToSegment(1100L, 1200L);

        long claimed = lock.tryClaim();
        assertEquals(1100L, claimed);
        assertTrue(lock.isAvailable(claimed));
        assertEquals(1200L, lock.getAvailable());
    }

    @Test
    @DisplayName("getCursor 应返回最后分配的 ID")
    void getCursor_shouldReturnLastAllocatedId() {
        lock.init(1000L, 1100L);

        assertEquals(999L, lock.getCursor()); // init 后 cursor = start - 1

        lock.tryClaim();
        assertEquals(1000L, lock.getCursor());

        lock.tryClaim();
        assertEquals(1001L, lock.getCursor());
    }

    @Test
    @DisplayName("getAvailable 应返回当前可用上限")
    void getAvailable_shouldReturnCurrentMax() {
        lock.init(1000L, 1100L);
        assertEquals(1100L, lock.getAvailable());

        lock.switchToSegment(1100L, 1200L);
        assertEquals(1200L, lock.getAvailable());
    }

    @Test
    @DisplayName("waitFor 在条件已满足时应立即返回 true")
    void waitFor_shouldReturnTrue_whenConditionAlreadyMet() {
        AtomicBoolean condition = new AtomicBoolean(true);

        boolean result = DisruptorLock.waitFor(condition::get, TimeUnit.SECONDS.toNanos(1));

        assertTrue(result);
    }

    @Test
    @DisplayName("waitFor 在条件延迟满足时应返回 true")
    void waitFor_shouldReturnTrue_whenConditionBecomesTrue() throws InterruptedException {
        AtomicBoolean condition = new AtomicBoolean(false);

        new Thread(() -> {
            try {
                Thread.sleep(100);
                condition.set(true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();

        boolean result = DisruptorLock.waitFor(condition::get, TimeUnit.SECONDS.toNanos(5));

        assertTrue(result);
        assertTrue(condition.get());
    }

    @Test
    @DisplayName("waitFor 在条件始终不满足时应返回 false（超时）")
    void waitFor_shouldReturnFalse_whenTimeout() {
        AtomicBoolean condition = new AtomicBoolean(false);

        long start = System.nanoTime();
        boolean result = DisruptorLock.waitFor(condition::get, TimeUnit.MILLISECONDS.toNanos(100));
        long elapsed = System.nanoTime() - start;

        assertFalse(result);
        assertTrue(elapsed >= TimeUnit.MILLISECONDS.toNanos(90),
                "应等待至少 90ms，实际: " + TimeUnit.NANOSECONDS.toMillis(elapsed) + "ms");
    }

    @Test
    @DisplayName("多个线程并发 tryClaim 应产生唯一 ID")
    void tryClaim_shouldProduceUniqueIds_underConcurrency() throws InterruptedException {
        lock.init(0L, 10000L);

        int threadCount = 10;
        int claimsPerThread = 100;
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < claimsPerThread; j++) {
                    lock.tryClaim();
                }
            });
            threads[i].start();
        }

        for (Thread t : threads) {
            t.join();
        }

        // 1000 次分配后 cursor 应为 999
        assertEquals(999L, lock.getCursor());
    }
}
