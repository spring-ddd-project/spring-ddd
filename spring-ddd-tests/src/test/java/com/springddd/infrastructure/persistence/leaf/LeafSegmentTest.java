package com.springddd.infrastructure.persistence.leaf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeafSegmentTest {

    @Test
    void shouldGetAndSetValue() {
        LeafSegment segment = new LeafSegment();
        assertEquals(0L, segment.getValue().get());
        segment.getValue().set(100L);
        assertEquals(100L, segment.getValue().get());
    }

    @Test
    void shouldGetAndSetMax() {
        LeafSegment segment = new LeafSegment();
        segment.setMax(1000L);
        assertEquals(1000L, segment.getMax());
    }

    @Test
    void shouldGetAndSetStep() {
        LeafSegment segment = new LeafSegment();
        segment.setStep(100);
        assertEquals(100, segment.getStep());
    }
}
