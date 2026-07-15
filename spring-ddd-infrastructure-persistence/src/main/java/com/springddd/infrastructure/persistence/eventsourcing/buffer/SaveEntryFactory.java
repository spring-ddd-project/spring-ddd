package com.springddd.infrastructure.persistence.eventsourcing.buffer;

import com.lmax.disruptor.EventFactory;

public class SaveEntryFactory implements EventFactory<SaveEntry> {

    @Override
    public SaveEntry newInstance() {
        return new SaveEntry();
    }
}
