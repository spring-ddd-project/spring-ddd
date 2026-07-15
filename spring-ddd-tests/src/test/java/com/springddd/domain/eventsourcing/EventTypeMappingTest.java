package com.springddd.domain.eventsourcing;

import com.springddd.domain.eventsourcing.fixture.TestCountChanged;
import com.springddd.domain.eventsourcing.fixture.TestCreated;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventTypeMappingTest {

    @AfterEach
    void tearDown() {
        EventTypeMapping.clear();
    }

    @Test
    void shouldRegisterAndRetrieveEventClass() {
        EventTypeMapping.register("TestCreated", TestCreated.class);

        assertEquals(TestCreated.class, EventTypeMapping.getEventTypeClass("TestCreated"));
    }

    @Test
    void shouldReturnNullForUnregisteredEventType() {
        assertNull(EventTypeMapping.getEventTypeClass("Unknown"));
    }

    @Test
    void shouldCheckContainsEventType() {
        EventTypeMapping.register("TestCountChanged", TestCountChanged.class);

        assertTrue(EventTypeMapping.contains("TestCountChanged"));
        assertFalse(EventTypeMapping.contains("TestCreated"));
    }
}
