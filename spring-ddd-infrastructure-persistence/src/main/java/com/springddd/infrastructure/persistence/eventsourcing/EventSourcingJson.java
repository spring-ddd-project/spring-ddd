package com.springddd.infrastructure.persistence.eventsourcing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.domain.eventsourcing.DomainEvent;
import com.springddd.domain.eventsourcing.EventSourcingException;
import com.springddd.domain.eventsourcing.EventTypeMapping;
import com.springddd.domain.util.ErrorCode;
public class EventSourcingJson {

    private final ObjectMapper objectMapper;

    public EventSourcingJson(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.copy();
        this.objectMapper.findAndRegisterModules();
    }
    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new EventSourcingException(ErrorCode.EVENT_SOURCING_APPLY_METHOD_INVOKE_FAILED,
                    "JSON 序列化失败: " + object.getClass().getName(), e.getMessage());
        }
    }
    public <T> T toObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new EventSourcingException(ErrorCode.EVENT_SOURCING_APPLY_METHOD_INVOKE_FAILED,
                    "JSON 反序列化失败: " + clazz.getName(), e.getMessage());
        }
    }
    public DomainEvent toDomainEvent(String eventType, String eventData) {
        Class<? extends DomainEvent> eventClass = EventTypeMapping.getEventTypeClass(eventType);
        if (eventClass == null) {
            throw new EventSourcingException(ErrorCode.EVENT_TYPE_NOT_REGISTERED, eventType);
        }
        return toObject(eventData, eventClass);
    }
}
