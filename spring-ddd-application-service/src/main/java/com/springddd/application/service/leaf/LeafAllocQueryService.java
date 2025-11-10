package com.springddd.application.service.leaf;

import com.springddd.application.service.leaf.dto.*;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeafAllocQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final LeafAllocViewMapStruct mapStruct;

    public Mono<PageResponse<LeafAllocView>> index(LeafAllocPageQuery query) {
        Criteria criteria = Criteria.empty();
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<LeafAllocView>> list = r2dbcEntityTemplate.select(LeafAllocEntity.class).matching(qry).all().collectList().map(mapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), LeafAllocEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<PageResponse<LeafAllocView>> recycle(LeafAllocPageQuery query) {
        Criteria criteria = Criteria.empty();
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<LeafAllocView>> list = r2dbcEntityTemplate.select(LeafAllocEntity.class).matching(qry).all().collectList().map(mapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), LeafAllocEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<List<LeafAllocView>> getAllLeafAlloc() {
        return r2dbcEntityTemplate.select(LeafAllocEntity.class).all().collectList().map(mapStruct::toViews);
    }

    public Mono<LeafAllocView> getLeafAlloc(String tag) {
        return r2dbcEntityTemplate.selectOne(Query.query(Criteria.where(LeafAllocQuery.Fields.bizTag).is(tag)), LeafAllocEntity.class).map(mapStruct::toView);
    }
}
