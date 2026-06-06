package com.springddd.infrastructure.persistence.leaf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeafSegmentTest {

    @Test
    void shouldGetAndSetStart() {
        LeafSegment segment = new LeafSegment();
        assertEquals(0L, segment.getStart());
        segment.setStart(1000L);
        assertEquals(1000L, segment.getStart());
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
