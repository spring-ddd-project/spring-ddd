package com.springddd.infrastructure.persistence.leaf;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

@Data
public class LeafSegment {

    private AtomicLong value = new AtomicLong(0);

    private volatile long max;

    private volatile int step;
}
