package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenColumnsQuery;
import com.springddd.application.service.gen.dto.GenColumnsView;
import com.springddd.application.service.gen.dto.GenColumnsViewMapStruct;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GenColumnsQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final GenColumnsViewMapStruct genColumnsViewMapStruct;

    public Mono<GenColumnsView> queryColumnsByGenInfoId(Long infoId) {
        Criteria criteria = Criteria.where(GenColumnsQuery.Fields.deleteStatus).is(false);
        Query qry = Query.query(criteria);
        return r2dbcEntityTemplate.select(GenColumnsEntity.class).matching(qry).one().map(genColumnsViewMapStruct::toView);
    }
}
