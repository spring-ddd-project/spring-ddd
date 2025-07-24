package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenInfoPageQuery;
import com.springddd.application.service.gen.dto.GenInfoView;
import com.springddd.application.service.gen.dto.GenInfoViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenInfoEntity;
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
public class GenInfoQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final GenInfoViewMapStruct genInfoViewMapStruct;

    public Mono<PageResponse<GenInfoView>> index(GenInfoPageQuery query) {
        Criteria criteria = Criteria.where(GenInfoPageQuery.Fields.deleteStatus).is(false);
        if (!ObjectUtils.isEmpty(query.getTableName())) {
            criteria = criteria.and(GenInfoPageQuery.Fields.tableName).is(query.getTableName());
        }
        Query qry = Query.query(criteria);
        Mono<List<GenInfoView>> list = r2dbcEntityTemplate.select(GenInfoEntity.class).matching(qry).all().collectList().map(genInfoViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), GenInfoEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }
}
