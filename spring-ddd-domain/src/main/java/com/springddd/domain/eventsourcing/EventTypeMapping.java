package com.springddd.domain.eventsourcing;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件类型映射注册表。
 *
 * <p>用于将事件类型字符串反序列化为具体的领域事件类。
 * 业务方在系统启动时注册自己的事件类型映射。
 */
public final class EventTypeMapping {

    private static final Map<String, Class<? extends DomainEvent>> EVENT_TYPE_MAP = new ConcurrentHashMap<>();

    private EventTypeMapping() {
    }

    /**
     * 注册事件类型映射。
     *
     * @param eventType 事件类型名称
     * @param clazz     对应的领域事件类
     */
    public static void register(String eventType, Class<? extends DomainEvent> clazz) {
        EVENT_TYPE_MAP.put(eventType, clazz);
    }

    /**
     * 根据事件类型名称获取领域事件类。
     *
     * @param eventType 事件类型名称
     * @return 领域事件类，未找到返回 null
     */
    public static Class<? extends DomainEvent> getEventTypeClass(String eventType) {
        return EVENT_TYPE_MAP.get(eventType);
    }

    /**
     * 判断是否已注册指定事件类型。
     */
    public static boolean contains(String eventType) {
        return EVENT_TYPE_MAP.containsKey(eventType);
    }

    /**
     * 清空所有映射，主要用于测试。
     */
    public static void clear() {
        EVENT_TYPE_MAP.clear();
    }
}
