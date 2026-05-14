package com.springddd.application.service.leaf;

import com.springddd.application.service.leaf.dto.LeafAllocPageQuery;
import com.springddd.application.service.leaf.dto.LeafAllocView;
import com.springddd.application.service.leaf.dto.LeafAllocViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeafAllocQueryService {

    private final QueryFactory queryFactory;
    private final LeafAllocViewMapStruct leafAllocViewMapStruct;

    public Mono<PageResponse<LeafAllocView>> index(LeafAllocPageQuery query) {
        Criteria criteria = Criteria.where(LeafAllocPageQuery.Fields.deleteStatus).is(false);
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());

        Mono<List<LeafAllocView>> list = queryFactory.getR2dbcEntityTemplate()
                .select(LeafAllocEntity.class)
                .matching(qry)
                .all()
                .collectList()
                .map(leafAllocViewMapStruct::toViews);

        Mono<Long> count = queryFactory.getR2dbcEntityTemplate()
                .count(Query.query(criteria), LeafAllocEntity.class);

        return Mono.zip(list, count)
                .map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<PageResponse<LeafAllocView>> recycle(LeafAllocPageQuery query) {
        Criteria criteria = Criteria.where(LeafAllocPageQuery.Fields.deleteStatus).is(true);
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());

        Mono<List<LeafAllocView>> list = queryFactory.getR2dbcEntityTemplate()
                .select(LeafAllocEntity.class)
                .matching(qry)
                .all()
                .collectList()
                .map(leafAllocViewMapStruct::toViews);

        Mono<Long> count = queryFactory.getR2dbcEntityTemplate()
                .count(Query.query(criteria), LeafAllocEntity.class);

        return Mono.zip(list, count)
                .map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }
}
