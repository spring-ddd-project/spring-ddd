package com.springddd.application.service.dict;

import com.springddd.application.service.common.DataScopeQueryFilter;
import com.springddd.application.service.dict.dto.*;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
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
public class SysDictItemQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final SysDictItemViewMapStruct sysDictItemViewMapStruct;

    private final DataScopeQueryFilter dataScopeQueryFilter;

    public Mono<PageResponse<SysDictItemView>> index(Long menuId, SysDictItemPageQuery query) {
        return dataScopeQueryFilter.apply(menuId)
                .flatMap(scopeResult -> {
                    Criteria criteria = Criteria
                            .where(SysDictItemQuery.Fields.deleteStatus).is(false);
                    if (!ObjectUtils.isEmpty(query) && !ObjectUtils.isEmpty(query.getDictId())) {
                        criteria = criteria.and(SysDictItemQuery.Fields.dictId).is(query.getDictId());
                    }
                    if (!scopeResult.isAll()) {
                        criteria = criteria.and(DataScopeQueryFilter.createByInCriteria(SysDictItemQuery.Fields.createBy, scopeResult.getVisibleUsernames()));
                    }
                    Query qry = Query.query(criteria)
                            .limit(query.getPageSize())
                            .offset((long) (query.getPageNum() - 1) * query.getPageSize());
                    Mono<List<SysDictItemView>> list = r2dbcEntityTemplate.select(SysDictItemEntity.class).matching(qry).all().collectList().map(sysDictItemViewMapStruct::toViews);
                    Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysDictItemEntity.class);
                    return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
                });
    }

    public Mono<PageResponse<SysDictItemView>> recycle(Long menuId, SysDictItemPageQuery query) {
        return dataScopeQueryFilter.apply(menuId)
                .flatMap(scopeResult -> {
                    Criteria criteria = Criteria
                            .where(SysDictItemQuery.Fields.deleteStatus).is(true);
                    if (!ObjectUtils.isEmpty(query) && !ObjectUtils.isEmpty(query.getDictId())) {
                        criteria = criteria.and(SysDictItemQuery.Fields.dictId).is(query.getDictId());
                    }
                    if (!scopeResult.isAll()) {
                        criteria = criteria.and(DataScopeQueryFilter.createByInCriteria(SysDictItemQuery.Fields.createBy, scopeResult.getVisibleUsernames()));
                    }
                    Query qry = Query.query(criteria)
                            .limit(query.getPageSize())
                            .offset((long) (query.getPageNum() - 1) * query.getPageSize());
                    Mono<List<SysDictItemView>> list = r2dbcEntityTemplate.select(SysDictItemEntity.class).matching(qry).all().collectList().map(sysDictItemViewMapStruct::toViews);
                    Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysDictItemEntity.class);
                    return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
                });
    }

    public Mono<SysDictItemView> queryItemLabelByItemValueAndDictId(Long dictId, Integer itemValue) {
        Criteria criteria = Criteria
                .where(SysDictItemQuery.Fields.deleteStatus).is(false)
                .and(SysDictItemQuery.Fields.dictId).is(dictId)
                .and(SysDictItemQuery.Fields.itemValue).is(itemValue);
        Query qry = Query.query(criteria);
        return r2dbcEntityTemplate.select(SysDictItemEntity.class).matching(qry).one().map(sysDictItemViewMapStruct::toView);
    }

    public Mono<List<SysDictItemView>> queryItemLabelByDictId(Long dictId) {
        Criteria criteria = Criteria
                .where(SysDictItemQuery.Fields.deleteStatus).is(false)
                .and(SysDictItemQuery.Fields.dictId).is(dictId);
        Query qry = Query.query(criteria);
        return r2dbcEntityTemplate.select(SysDictItemEntity.class).matching(qry).all().collectList().map(sysDictItemViewMapStruct::toViews);
    }
}
