package com.springddd.domain.eventsourcing;

import com.springddd.domain.AggregateRootId;
import reactor.core.publisher.Mono;

/**
 * 事件溯源聚合根仓库接口。
 *
 * @param <ID> 聚合根 ID 类型
 * @param <E>  聚合根类型
 */
public interface EventSourcingRepository<ID extends AggregateRootId<?>, E extends EventSourcingAggregateRoot> {

    /**
     * 根据聚合根 ID 加载聚合根。
     *
     * <p>加载过程优先从快照恢复，然后回放快照之后的领域事件。
     */
    Mono<E> load(ID aggregateRootId);

    /**
     * 保存聚合根。
     *
     * <p>保存过程会持久化新增的领域事件，并在需要时生成快照。
     */
    Mono<Void> save(E aggregateRoot);
}
