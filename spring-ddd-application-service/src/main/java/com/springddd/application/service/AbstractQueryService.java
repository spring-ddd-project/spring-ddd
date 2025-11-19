package com.springddd.application.service;

import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Mono;
import java.util.List;

@RequiredArgsConstructor
public abstract class AbstractQueryService<T, V, Q extends com.springddd.domain.util.PageQuery> {
    protected final QueryFactory queryFactory;

    public Mono<PageResponse<V>> index(Q query) {
        Criteria criteria = buildIndexCriteria(query);
        return performQuery(query, criteria);
    }

    public Mono<PageResponse<V>> recycle(Q query) {
        Criteria criteria = buildRecycleCriteria(query);
        return performQuery(query, criteria);
    }

    protected abstract Criteria buildIndexCriteria(Q query);
    protected abstract Criteria buildRecycleCriteria(Q query);
    protected abstract Class<T> getEntityClass();
    protected abstract List<V> mapToViews(List<T> entities);

    private Mono<PageResponse<V>> performQuery(Q query, Criteria criteria) {
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        
        Mono<List<V>> list = queryFactory.getR2dbcEntityTemplate()
                .select(getEntityClass())
                .matching(qry)
                .all()
                .collectList()
                .map(this::mapToViews);

        Mono<Long> count = queryFactory.getR2dbcEntityTemplate()
                .count(Query.query(criteria), getEntityClass());

        return Mono.zip(list, count)
                .map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }
}
