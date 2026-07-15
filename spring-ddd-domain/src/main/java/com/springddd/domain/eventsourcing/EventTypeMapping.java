package com.springddd.domain.eventsourcing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public final class EventTypeMapping {

    private static final Map<String, Class<? extends DomainEvent>> EVENT_TYPE_MAP = new ConcurrentHashMap<>();

    private EventTypeMapping() {
    }
    public static void register(String eventType, Class<? extends DomainEvent> clazz) {
        EVENT_TYPE_MAP.put(eventType, clazz);
    }
    public static Class<? extends DomainEvent> getEventTypeClass(String eventType) {
        return EVENT_TYPE_MAP.get(eventType);
    }
    public static boolean contains(String eventType) {
        return EVENT_TYPE_MAP.containsKey(eventType);
    }
    public static void clear() {
        EVENT_TYPE_MAP.clear();
    }
}
