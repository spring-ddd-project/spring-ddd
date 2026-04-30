package com.springddd.application.service.leaf.dto;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

public class SegmentCommand implements Serializable {

    private AtomicLong value = new AtomicLong(0);
    private volatile long max;
    private volatile int step;
    private SegmentBufferCommand buffer;

    public SegmentCommand(SegmentBufferCommand buffer) {
        this.buffer = buffer;
    }

    public AtomicLong getValue() {
        return value;
    }

    public void setValue(AtomicLong value) {
        this.value = value;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public SegmentBufferCommand getBuffer() {
        return buffer;
    }

    public long getIdle() {
        return this.getMax() - getValue().get();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Segment(");
        sb.append("value:");
        sb.append(value);
        sb.append(",max:");
        sb.append(max);
        sb.append(",step:");
        sb.append(step);
        sb.append(")");
        return sb.toString();
    }
}

