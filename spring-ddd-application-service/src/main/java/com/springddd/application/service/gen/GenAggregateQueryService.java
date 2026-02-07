package com.springddd.application.service.gen;


import com.springddd.application.service.gen.dto.GenAggregatePageQuery;
import com.springddd.application.service.gen.dto.GenAggregateQuery;
import com.springddd.application.service.gen.dto.GenAggregateView;
import com.springddd.application.service.gen.dto.GenAggregateViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenAggregateQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final GenAggregateViewMapStruct aggregateViewMapStruct;

    public Mono<PageResponse<GenAggregateView>> index(GenAggregatePageQuery query) {
        if (ObjectUtils.isEmpty(query.getInfoId())) {
            return Mono.empty();
        }
        Criteria criteria = Criteria.where(GenAggregateQuery.Fields.infoId).is(query.getInfoId());
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());

        Mono<List<GenAggregateView>> list = r2dbcEntityTemplate.select(GenAggregateEntity.class).matching(qry).all().collectList().map(aggregateViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), GenAggregateEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<List<GenAggregateView>> queryAggregateByInfoId(Long infoId) {
        Criteria criteria = Criteria.where(GenAggregateQuery.Fields.infoId).is(infoId);
        return r2dbcEntityTemplate.select(GenAggregateEntity.class).matching(Query.query(criteria)).all().collectList().map(aggregateViewMapStruct::toViews);
    }
}
