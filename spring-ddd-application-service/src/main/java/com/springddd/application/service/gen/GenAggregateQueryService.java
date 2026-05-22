package com.springddd.application.service.gen;


import com.springddd.application.service.gen.dto.GenAggregatePageQuery;
import com.springddd.application.service.gen.dto.GenAggregateQuery;
import com.springddd.application.service.gen.dto.GenAggregateView;
import com.springddd.application.service.gen.dto.GenAggregateViewMapStruct;
import com.springddd.application.service.permission.BaseQueryService;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenAggregateQueryService extends BaseQueryService<GenAggregateEntity> {

    private final GenAggregateViewMapStruct aggregateViewMapStruct;

    public Mono<PageResponse<GenAggregateView>> index(GenAggregatePageQuery query) {
        if (ObjectUtils.isEmpty(query.getInfoId())) {
            return Mono.empty();
        }
        Criteria criteria = Criteria.where(GenAggregateQuery.Fields.infoId).is(query.getInfoId());
        return applyDataScope(criteria, GenAggregateEntity.class).flatMap(scopedCriteria -> {
            Query qry = Query.query(scopedCriteria)
                    .limit(query.getPageSize())
                    .offset((long) (query.getPageNum() - 1) * query.getPageSize());

            Mono<List<GenAggregateView>> list = queryFactory.getR2dbcEntityTemplate().select(GenAggregateEntity.class).matching(qry).all().collectList().map(aggregateViewMapStruct::toViews);
            Mono<Long> count = queryFactory.getR2dbcEntityTemplate().count(Query.query(scopedCriteria), GenAggregateEntity.class);
            return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
        });
    }

    public Mono<List<GenAggregateView>> queryAggregateByInfoId(Long infoId) {
        Criteria criteria = Criteria.where(GenAggregateQuery.Fields.infoId).is(infoId);
        return queryFactory.getR2dbcEntityTemplate().select(GenAggregateEntity.class).matching(Query.query(criteria)).all().collectList().map(aggregateViewMapStruct::toViews);
    }
}
