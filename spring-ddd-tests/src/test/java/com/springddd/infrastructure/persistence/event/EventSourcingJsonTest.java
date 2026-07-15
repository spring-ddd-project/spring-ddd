package com.springddd.infrastructure.persistence.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.domain.event.DomainEvent;
import com.springddd.domain.event.EventSourcingException;
import com.springddd.domain.event.EventTypeMapping;
import com.springddd.domain.event.fixture.TestCountChanged;
import com.springddd.domain.event.fixture.TestCreated;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EventSourcingJsonTest {

    private EventSourcingJson eventSourcingJson;

    @BeforeEach
    void setUp() {
        EventTypeMapping.clear();
        EventTypeMapping.register("TestCreated", TestCreated.class);
        eventSourcingJson = new EventSourcingJson(new ObjectMapper());
    }

    @AfterEach
    void tearDown() {
        EventTypeMapping.clear();
    }

    @Test
    void shouldSerializeAndDeserializeObject() {
        TestCreated event = TestCreated.of("1", "name1");
        String json = eventSourcingJson.toJson(event);

        TestCreated deserialized = eventSourcingJson.toObject(json, TestCreated.class);

        assertEquals(event.getEntityId(), deserialized.getEntityId());
        assertEquals(event.getName(), deserialized.getName());
    }

    @Test
    void shouldDeserializeDomainEventByType() {
        TestCreated event = TestCreated.of("1", "name1");
        String json = eventSourcingJson.toJson(event);

        TestCreated deserialized = (TestCreated) eventSourcingJson.toDomainEvent("TestCreated", json);

        assertEquals(event.getName(), deserialized.getName());
    }

    @Test
    void shouldThrowExceptionWhenEventTypeNotRegistered() {
        String json = eventSourcingJson.toJson(TestCountChanged.of("1", 5));

        assertThrows(EventSourcingException.class, () -> eventSourcingJson.toDomainEvent("TestCountChanged", json));
    }

    @Test
    void shouldSerializeLocalDateTime() {
        TestCreated event = TestCreated.of("1", "name1");
        event.setEventTime(LocalDateTime.of(2026, 1, 1, 12, 0, 0));

        String json = eventSourcingJson.toJson(event);

        assertTrue(json.contains("2026"));
    }
}
