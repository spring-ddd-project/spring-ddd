package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenColumnBindPageQuery;
import com.springddd.application.service.gen.dto.GenColumnBindView;
import com.springddd.application.service.gen.dto.GenColumnBindViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenColumnBindEntity;
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
public class GenColumnBindQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final GenColumnBindViewMapStruct genColumnBindViewMapStruct;

    public Mono<PageResponse<GenColumnBindView>> index(GenColumnBindPageQuery query) {
        Criteria criteria = Criteria.where(GenColumnBindPageQuery.Fields.deleteStatus).is(false);
        if (!ObjectUtils.isEmpty(query.getColumnType())) {
            criteria = criteria.and(GenColumnBindPageQuery.Fields.columnType).like("%" + query.getColumnType() + "%");
        }
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<GenColumnBindView>> list = r2dbcEntityTemplate.select(GenColumnBindEntity.class).matching(qry).all().collectList().map(genColumnBindViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), GenColumnBindEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<PageResponse<GenColumnBindView>> recycle(GenColumnBindPageQuery query) {
        Criteria criteria = Criteria.where(GenColumnBindPageQuery.Fields.deleteStatus).is(true);
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<GenColumnBindView>> list = r2dbcEntityTemplate.select(GenColumnBindEntity.class).matching(qry).all().collectList().map(genColumnBindViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), GenColumnBindEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }
}
