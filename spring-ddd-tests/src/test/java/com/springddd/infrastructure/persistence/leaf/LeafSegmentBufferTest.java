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
    void shouldLockAndUnlockWithoutError() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.lock();
        try {
            assertTrue(true); // locked successfully
        } finally {
            buffer.unlock();
        }
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
}
