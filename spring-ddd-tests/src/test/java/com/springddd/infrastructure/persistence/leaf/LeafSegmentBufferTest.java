package com.springddd.infrastructure.persistence.leaf;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LeafSegmentBufferTest {

    @Test
    void shouldConstructWithBizTag() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        assertEquals("test", buffer.getBizTag());
        assertNotNull(buffer.getSegments());
        assertEquals(2, buffer.getSegments().length);
        assertEquals(0, buffer.getCurrentPos());
        assertFalse(buffer.isNextReady());
        assertFalse(buffer.getThreadRunning().get());
        assertEquals(0L, buffer.getStepUpdateTime());
        assertEquals(0, buffer.getMinStep());
        assertNotNull(buffer.getDisruptorLock());
    }

    @Test
    void shouldGetCurrentSegment() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        assertSame(buffer.getSegments()[0], buffer.getCurrent());
    }

    @Test
    void shouldGetNextSegment() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        assertSame(buffer.getSegments()[1], buffer.getNext());
    }

    @Test
    void shouldCalculateNextPos() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        assertEquals(1, buffer.nextPos());
        buffer.setCurrentPos(1);
        assertEquals(0, buffer.nextPos());
    }

    @Test
    void shouldSwitchPos() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.switchPos();
        assertEquals(1, buffer.getCurrentPos());
        buffer.switchPos();
        assertEquals(0, buffer.getCurrentPos());
    }

    @Test
    void shouldSwitchPosViaCas() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        assertTrue(buffer.casCurrentPos(0, 1));
        assertEquals(1, buffer.getCurrentPos());
        assertFalse(buffer.casCurrentPos(0, 1)); // CAS 失败
        assertEquals(1, buffer.getCurrentPos());
        assertTrue(buffer.casCurrentPos(1, 0));
        assertEquals(0, buffer.getCurrentPos());
    }

    @Test
    void shouldReturnInitOk_whenStepGreaterThanZero() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        assertFalse(buffer.isInitOk());
        buffer.getCurrent().setStep(1);
        assertTrue(buffer.isInitOk());
    }

    @Test
    void shouldReturnInitOkFalse_whenStepIsZero() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        assertFalse(buffer.isInitOk());
    }

    @Test
    void shouldSetAndGetStepUpdateTime() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.setStepUpdateTime(12345L);
        assertEquals(12345L, buffer.getStepUpdateTime());
    }

    @Test
    void shouldSetAndGetMinStep() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.setMinStep(50);
        assertEquals(50, buffer.getMinStep());
    }

    @Test
    void shouldSetAndGetNextReady() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        assertFalse(buffer.isNextReady());
        buffer.setNextReady(true);
        assertTrue(buffer.isNextReady());
    }

    @Test
    void shouldSetAndGetThreadRunning() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        assertFalse(buffer.getThreadRunning().get());
        buffer.getThreadRunning().set(true);
        assertTrue(buffer.getThreadRunning().get());
    }

    @Test
    void shouldAccessSegmentsDirectly() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        LeafSegment[] segments = buffer.getSegments();
        assertNotNull(segments[0]);
        assertNotNull(segments[1]);
        segments[0].setStart(1000L);
        segments[0].setMax(1100L);
        segments[0].setStep(100);
        assertEquals(1000L, buffer.getCurrent().getStart());
    }
}
