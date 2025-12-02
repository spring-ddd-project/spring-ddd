package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptQuery;
import com.springddd.application.service.dept.dto.SysDeptView;
import com.springddd.application.service.dept.dto.SysDeptViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.domain.util.ReactiveTreeUtils;
import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SysDeptQueryService {

    private final QueryFactory queryFactory;

    private final SysDeptViewMapStruct sysDeptViewMapStruct;

    public Mono<PageResponse<SysDeptView>> index(SysDeptQuery query) {
        Criteria criteria = Criteria.where(SysDeptQuery.Fields.deleteStatus).is(false);
        Query qry = Query.query(criteria);
        Mono<List<SysDeptView>> list = queryFactory.getR2dbcEntityTemplate().select(SysDeptEntity.class).matching(qry).all().collectList().map(sysDeptViewMapStruct::toViews);
        Mono<Long> count = queryFactory.getR2dbcEntityTemplate().count(Query.query(criteria), SysDeptEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), 0, 0));
    }

    public Mono<PageResponse<SysDeptView>> recycle(SysDeptQuery query) {
        Criteria criteria = Criteria.where(SysDeptQuery.Fields.deleteStatus).is(true);
        Query qry = Query.query(criteria);
        Mono<List<SysDeptView>> list = queryFactory.getR2dbcEntityTemplate().select(SysDeptEntity.class).matching(qry).all().collectList().map(sysDeptViewMapStruct::toViews);
        Mono<Long> count = queryFactory.getR2dbcEntityTemplate().count(Query.query(criteria), SysDeptEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), 0, 0));
    }

    public Mono<List<SysDeptView>> deptTree() {
        return queryFactory.getR2dbcEntityTemplate().select(SysDeptEntity.class).matching(Query.query(Criteria.where(SysDeptQuery.Fields.deleteStatus).is(false)))
                .all().collectList().map(sysDeptViewMapStruct::toViews)
                .flatMap(views -> ReactiveTreeUtils.buildTree(
                        views,
                        SysDeptView::getId,
                        SysDeptView::getParentId,
                        SysDeptView::setChildren,
                        v -> v.getParentId() == null,
                        Comparator.comparing(SysDeptView::getSortOrder),
                        SysDeptView::getDeptStatus,
                        30,
                        d -> !d.getDeleteStatus()));
    }
    public Mono<List<SysDeptView>> queryAllDept() {
        return queryFactory.getR2dbcEntityTemplate().select(SysDeptEntity.class).all().collectList().map(sysDeptViewMapStruct::toViews);
    }
}









