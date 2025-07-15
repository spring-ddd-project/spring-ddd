package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptQuery;
import com.springddd.application.service.dept.dto.SysDeptView;
import com.springddd.application.service.dept.dto.SysDeptViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.domain.util.ReactiveTreeUtils;
import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysDeptQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final SysDeptViewMapStruct sysDeptViewMapStruct;

    public Mono<PageResponse<SysDeptView>> index(SysDeptQuery query) {
        Criteria criteria = Criteria.where(SysDeptQuery.Fields.deleteStatus).is(false);
        Query qry = Query.query(criteria);
        Mono<List<SysDeptView>> list = r2dbcEntityTemplate.select(SysDeptEntity.class).matching(qry).all().collectList().map(sysDeptViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysDeptEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), 0, 0));
    }

    public Mono<PageResponse<SysDeptView>> recycle(SysDeptQuery query) {
        Criteria criteria = Criteria.where(SysDeptQuery.Fields.deleteStatus).is(true);
        Query qry = Query.query(criteria);
        Mono<List<SysDeptView>> list = r2dbcEntityTemplate.select(SysDeptEntity.class).matching(qry).all().collectList().map(sysDeptViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysDeptEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), 0, 0));
    }

    public Mono<List<SysDeptView>> deptTree() {
        return r2dbcEntityTemplate.select(SysDeptEntity.class).matching(Query.query(Criteria.where(SysDeptQuery.Fields.deleteStatus).is(false)))
                .all().collectList().map(sysDeptViewMapStruct::toViews)
                .flatMap(views -> ReactiveTreeUtils.buildTree(
                        views,
                        SysDeptView::getId,
                        SysDeptView::getParentId,
                        SysDeptView::setChildren,
                        v -> v.getParentId() == null,
                        Comparator.comparing(SysDeptView::getSortOrder),
                        f -> f.getDeptStatus() == true,
                        30));
    }
}
