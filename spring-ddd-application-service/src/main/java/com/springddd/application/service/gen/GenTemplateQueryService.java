package com.springddd.application.service.gen;

import com.springddd.application.service.common.DataScopeQueryFilter;
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
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenTemplateQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final GenTemplateViewMapStruct genTemplateViewMapStruct;

    private final DataScopeQueryFilter dataScopeQueryFilter;

    public Mono<PageResponse<GenTemplateView>> index(Long menuId, GenTemplatePageQuery query) {
        return dataScopeQueryFilter.apply(menuId)
                .flatMap(scopeResult -> {
                    Criteria criteria = Criteria.where(GenTemplateQuery.Fields.deleteStatus).is(false);
                    if (!ObjectUtils.isEmpty(query.getTemplateName())) {
                        criteria = criteria.and(GenTemplateQuery.Fields.templateName).like("%" + query.getTemplateName() + "%");
                    }
                    if (!scopeResult.isAll()) {
                        criteria = criteria.and(DataScopeQueryFilter.createByInCriteria(GenTemplateQuery.Fields.createBy, scopeResult.getVisibleUsernames()));
                    }
                    Query qry = Query.query(criteria)
                            .limit(query.getPageSize())
                            .offset((long) (query.getPageNum() - 1) * query.getPageSize());
                    Mono<List<GenTemplateView>> list = r2dbcEntityTemplate.select(GenTemplateEntity.class).matching(qry).all().collectList().map(genTemplateViewMapStruct::toViews);
                    Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), GenTemplateEntity.class);
                    return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
                });
    }

    public Mono<PageResponse<GenTemplateView>> recycle(Long menuId, GenTemplatePageQuery query) {
        return dataScopeQueryFilter.apply(menuId)
                .flatMap(scopeResult -> {
                    Criteria criteria = Criteria.where(GenTemplateQuery.Fields.deleteStatus).is(true);
                    if (!scopeResult.isAll()) {
                        criteria = criteria.and(DataScopeQueryFilter.createByInCriteria(GenTemplateQuery.Fields.createBy, scopeResult.getVisibleUsernames()));
                    }
                    Query qry = Query.query(criteria)
                            .limit(query.getPageSize())
                            .offset((long) (query.getPageNum() - 1) * query.getPageSize());
                    Mono<List<GenTemplateView>> list = r2dbcEntityTemplate.select(GenTemplateEntity.class).matching(qry).all().collectList().map(genTemplateViewMapStruct::toViews);
                    Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), GenTemplateEntity.class);
                    return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
                });
    }

    public Mono<List<GenTemplateView>> queryAllTemplate() {
        return r2dbcEntityTemplate.select(GenTemplateEntity.class).matching(Query.query(Criteria.where(GenTemplateQuery.Fields.deleteStatus).is(false))).all().collectList().map(genTemplateViewMapStruct::toViews);
    }
}
