package com.springddd.domain.event;

import com.springddd.domain.AbstractDomainMask;
import com.springddd.domain.util.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Getter
@Setter
public abstract class EventSourcingAggregateRoot extends AbstractDomainMask {
    public static final int DEFAULT_SNAPSHOT_THRESHOLD = 20;

    private static final Map<Class<?>, Map<Class<?>, Method>> APPLY_METHOD_CACHE = new ConcurrentHashMap<>();

    private final List<DomainEvent> domainEvents = new ArrayList<>();
    private Boolean takeSnapshot = Boolean.FALSE;
    protected final void registerDomainEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }
    protected final void registerDomainEvents(List<DomainEvent> events) {
        this.domainEvents.addAll(events);
    }
    public final void clearDomainEvents() {
        this.domainEvents.clear();
    }
    public void apply(DomainEvent domainEvent) {
        Method method = resolveApplyMethod(domainEvent.getClass());
        if (method == null) {
            throw new EventSourcingException(
                    ErrorCode.EVENT_APPLY_METHOD_NOT_FOUND,
                    getClass().getName(), domainEvent.getClass().getName());
        }
        try {
            method.invoke(this, domainEvent);
        } catch (Exception e) {
            throw new EventSourcingException(
                    ErrorCode.EVENT_APPLY_METHOD_INVOKE_FAILED,
                    getClass().getName(), domainEvent.getClass().getName(), e.getMessage());
        }
    }
    public void rebuild(List<DomainEvent> events) {
        for (DomainEvent domainEvent : events) {
            apply(domainEvent);
        }
        if (events.size() > snapshotThreshold()) {
            this.takeSnapshot = Boolean.TRUE;
        }
        clearDomainEvents();
    }
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
