package com.springddd.domain.eventsourcing;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 领域事件基类。
 *
 * <p>所有业务领域事件必须继承此类，并通过 {@link EventTypeMapping} 注册事件类型映射。
 */
@Data
@EqualsAndHashCode(callSuper = false)
public abstract class DomainEvent {

    /**
     * 事件唯一标识
     */
    private String eventId;

    /**
     * 聚合根唯一标识
     */
    private String entityId;

    /**
     * 事件类型，用于反序列化时查找具体事件类
     */
    private String eventType;

    /**
     * 事件发生时间
     */
    private LocalDateTime eventTime;

    /**
     * 子类必须提供事件类型名称。
     */
    protected DomainEvent() {
        this.eventType = eventType();
    }

    /**
     * 返回事件类型名称，默认使用简单类名。
     * 业务方可以覆盖以使用自定义事件类型名称。
     */
    protected String eventType() {
        return getClass().getSimpleName();
    }
}
