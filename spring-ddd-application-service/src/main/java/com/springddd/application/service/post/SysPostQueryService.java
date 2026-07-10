package com.springddd.application.service.post;

import com.springddd.application.service.post.dto.SysPostPageQuery;
import com.springddd.application.service.post.dto.SysPostQuery;
import com.springddd.application.service.post.dto.SysPostView;
import com.springddd.application.service.post.dto.SysPostViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.domain.util.ReactiveTreeUtils;
import com.springddd.infrastructure.persistence.entity.SysPostEntity;
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
public class SysPostQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final SysPostViewMapStruct sysPostViewMapStruct;

    public Mono<PageResponse<SysPostView>> index(SysPostQuery query) {
        Criteria criteria = Criteria.where(SysPostQuery.Fields.deleteStatus).is(false);
        Query qry = Query.query(criteria);
        Mono<List<SysPostView>> list = r2dbcEntityTemplate.select(SysPostEntity.class).matching(qry).all().collectList().map(sysPostViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysPostEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), 0, 0));
    }

    public Mono<PageResponse<SysPostView>> recycle(SysPostQuery query) {
        Criteria criteria = Criteria.where(SysPostQuery.Fields.deleteStatus).is(true);
        Query qry = Query.query(criteria);
        Mono<List<SysPostView>> list = r2dbcEntityTemplate.select(SysPostEntity.class).matching(qry).all().collectList().map(sysPostViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysPostEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), 0, 0));
    }

    public Mono<List<SysPostView>> postTree() {
        return r2dbcEntityTemplate.select(SysPostEntity.class).matching(Query.query(Criteria.where(SysPostQuery.Fields.deleteStatus).is(false)))
                .all().collectList().map(sysPostViewMapStruct::toViews)
                .flatMap(views -> ReactiveTreeUtils.buildTree(
                        views,
                        SysPostView::getId,
                        SysPostView::getParentId,
                        SysPostView::setChildren,
                        v -> v.getParentId() == null,
                        Comparator.comparing(SysPostView::getSortOrder),
                        SysPostView::getPostStatus,
                        30,
                        SysPostView::getDeleteStatus));
    }

    public Mono<List<SysPostView>> queryAllPost() {
        return r2dbcEntityTemplate.select(SysPostEntity.class).all().collectList().map(sysPostViewMapStruct::toViews);
    }
}
