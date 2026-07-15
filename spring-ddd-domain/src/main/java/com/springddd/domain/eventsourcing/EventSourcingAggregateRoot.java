package com.springddd.domain.eventsourcing;

import com.springddd.domain.AbstractDomainMask;
import com.springddd.domain.util.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 事件溯源聚合根基类。
 *
 * <p>继承该类的聚合根可以通过回放领域事件重建状态。
 * 子类需要提供形如 {@code public void apply(XxxEvent event)} 的方法处理具体事件。
 */
@Getter
@Setter
public abstract class EventSourcingAggregateRoot extends AbstractDomainMask {

    /**
     * 默认快照阈值：当聚合根需要回放超过 20 个事件时触发快照生成。
     */
    public static final int DEFAULT_SNAPSHOT_THRESHOLD = 20;

    private static final Map<Class<?>, Map<Class<?>, Method>> APPLY_METHOD_CACHE = new ConcurrentHashMap<>();

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * 是否需要生成快照
     */
    private Boolean takeSnapshot = Boolean.FALSE;

    /**
     * 注册领域事件。
     */
    protected final void registerDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    /**
     * 注册多个领域事件。
     */
    protected final void registerDomainEvents(List<DomainEvent> events) {
        this.domainEvents.addAll(events);
    }

    /**
     * 清空领域事件，通常在持久化后调用。
     */
    public final void clearDomainEvents() {
        this.domainEvents.clear();
    }

    /**
     * 应用单个领域事件到聚合根。
     */
    public void apply(DomainEvent domainEvent) {
        Method method = resolveApplyMethod(domainEvent.getClass());
        if (method == null) {
            throw new EventSourcingException(
                    ErrorCode.EVENT_SOURCING_APPLY_METHOD_NOT_FOUND,
                    getClass().getName(), domainEvent.getClass().getName());
        }
        try {
            method.invoke(this, domainEvent);
        } catch (Exception e) {
            throw new EventSourcingException(
                    ErrorCode.EVENT_SOURCING_APPLY_METHOD_INVOKE_FAILED,
                    getClass().getName(), domainEvent.getClass().getName(), e.getMessage());
        }
    }

    /**
     * 回放一系列领域事件重建聚合根状态。
     *
     * <p>重建完成后会清空领域事件列表，避免将历史事件再次持久化。
     * 如果回放事件数超过阈值，则标记需要生成快照。
     */
    public void rebuild(List<DomainEvent> events) {
        for (DomainEvent domainEvent : events) {
            apply(domainEvent);
        }
        if (events.size() > snapshotThreshold()) {
            this.takeSnapshot = Boolean.TRUE;
        }
        clearDomainEvents();
    }

    /**
     * 子类可覆盖此方法自定义快照阈值。
     */
    protected int snapshotThreshold() {
        return DEFAULT_SNAPSHOT_THRESHOLD;
    }

    private Method resolveApplyMethod(Class<?> eventClass) {
        Map<Class<?>, Method> methodMap = APPLY_METHOD_CACHE.computeIfAbsent(getClass(), k -> {
            Map<Class<?>, Method> map = new ConcurrentHashMap<>();
            for (Method method : getClass().getMethods()) {
                if (!"apply".equals(method.getName())) {
                    continue;
                }
                Class<?>[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length != 1 || !DomainEvent.class.isAssignableFrom(parameterTypes[0])) {
                    continue;
                }
                map.put(parameterTypes[0], method);
            }
            return map;
        });
        return methodMap.get(eventClass);
    }
}
