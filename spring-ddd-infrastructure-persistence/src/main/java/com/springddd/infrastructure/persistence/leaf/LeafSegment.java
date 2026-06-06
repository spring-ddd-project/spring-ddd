package com.springddd.infrastructure.persistence.leaf;

import lombok.Data;

/**
 * Leaf 分段 ID 的一个 segment。
 *
 * <p>ID 分配游标由 {@link DisruptorLock}（Sequence）管理，
 * 不再使用 AtomicLong，避免 false sharing。
 */
@Data
public class LeafSegment {

    /** segment 起始 ID（inclusive）。 */
    private volatile long start;

    /** segment 最大 ID（exclusive）。 */
    private volatile long max;

    private volatile int step;
}
