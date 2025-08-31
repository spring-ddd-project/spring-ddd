package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenTemplatePageQuery;
import com.springddd.application.service.gen.dto.GenTemplateQuery;
import com.springddd.application.service.gen.dto.GenTemplateView;
import com.springddd.application.service.gen.dto.GenTemplateViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenTemplateEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenTemplateQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final GenTemplateViewMapStruct genTemplateViewMapStruct;

    public Mono<PageResponse<GenTemplateView>> index(GenTemplatePageQuery query) {
        Criteria criteria = Criteria.where(GenTemplateQuery.Fields.deleteStatus).is(false);
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<GenTemplateView>> list = r2dbcEntityTemplate.select(GenTemplateEntity.class).matching(qry).all().collectList().map(genTemplateViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), GenTemplateEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<PageResponse<GenTemplateView>> recycle(GenTemplatePageQuery query) {
        Criteria criteria = Criteria.where(GenTemplateQuery.Fields.deleteStatus).is(true);
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<GenTemplateView>> list = r2dbcEntityTemplate.select(GenTemplateEntity.class).matching(qry).all().collectList().map(genTemplateViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), GenTemplateEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<List<GenTemplateView>> queryAllTemplate() {
        return r2dbcEntityTemplate.select(GenTemplateEntity.class).all().collectList().map(genTemplateViewMapStruct::toViews);
    }
}
