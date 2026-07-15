package com.springddd.application.service.eventsourcing;

import com.springddd.domain.AggregateRootId;
import com.springddd.domain.eventsourcing.DomainEvent;
import com.springddd.domain.eventsourcing.EventSourcingAggregateRoot;
import com.springddd.domain.eventsourcing.EventSourcingRepository;
import com.springddd.infrastructure.publisher.eventsourcing.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 事件溯源命令服务抽象模板。
 *
 * <p>统一封装「加载聚合根 → 执行业务逻辑 → 保存聚合根 → 发布领域事件」的流程。
 *
 * @param <ID> 聚合根 ID 类型
 * @param <E>  聚合根类型
 */
@RequiredArgsConstructor
public abstract class AbstractEventSourcingCommandService<
        ID extends AggregateRootId<?>,
        E extends EventSourcingAggregateRoot> {

    private final EventSourcingRepository<ID, E> repository;
    private final DomainEventPublisher eventPublisher;

    /**
     * 对指定聚合根执行业务操作并保存、发布事件。
     *
     * @param aggregateRootId 聚合根 ID
     * @param action          业务操作
     * @return 完成信号
     */
    protected Mono<Void> execute(ID aggregateRootId, Consumer<E> action) {
        return repository.load(aggregateRootId)
                .doOnNext(action)
                .flatMap(aggregate -> {
                    List<DomainEvent> events = new ArrayList<>(aggregate.getDomainEvents());
                    return repository.save(aggregate)
                            .then(publishEvents(events));
                });
    }

    /**
     * 对指定聚合根执行业务操作并保存、发布事件，返回自定义结果。
     *
     * @param aggregateRootId 聚合根 ID
     * @param action          业务操作，返回一个结果与聚合根的元组
     * @return 业务结果
     */
    protected <R> Mono<R> executeAndReturn(ID aggregateRootId, Function<E, Tuple2<R, E>> action) {
        return repository.load(aggregateRootId)
                .map(action)
                .flatMap(tuple -> {
                    List<DomainEvent> events = new ArrayList<>(tuple.getT2().getDomainEvents());
                    return repository.save(tuple.getT2())
                            .then(publishEvents(events))
                            .thenReturn(tuple.getT1());
                });
    }

    private Mono<Void> publishEvents(List<DomainEvent> events) {
        if (events.isEmpty()) {
            return Mono.empty();
        }
        return eventPublisher.publish(events);
    }
}
