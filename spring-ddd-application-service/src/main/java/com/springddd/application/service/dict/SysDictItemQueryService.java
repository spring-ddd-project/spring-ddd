package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.*;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysDictEntity;
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

    public Mono<PageResponse<SysDictItemView>> index(SysDictItemPageQuery query) {
        Criteria criteria = Criteria
                .where(SysDictItemQuery.Fields.deleteStatus).is(false);
        if (!ObjectUtils.isEmpty(query) && !ObjectUtils.isEmpty(query.getDictId())) {
            criteria = criteria.and(SysDictItemQuery.Fields.dictId).is(query.getDictId());
        }
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<SysDictItemView>> list = r2dbcEntityTemplate.select(SysDictItemEntity.class).matching(qry).all().collectList().map(sysDictItemViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysDictItemEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<PageResponse<SysDictItemView>> recycle(SysDictItemPageQuery query) {
        Criteria criteria = Criteria
                .where(SysDictItemQuery.Fields.deleteStatus).is(true);
        if (!ObjectUtils.isEmpty(query) && !ObjectUtils.isEmpty(query.getDictId())) {
            criteria = criteria.and(SysDictItemQuery.Fields.dictId).is(query.getDictId());
        }
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<SysDictItemView>> list = r2dbcEntityTemplate.select(SysDictItemEntity.class).matching(qry).all().collectList().map(sysDictItemViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysDictItemEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<SysDictItemView> queryItemLabelByItemValueAndDictId(Long dictId, Integer itemValue) {
        Criteria criteria = Criteria
                .where(SysDictItemQuery.Fields.deleteStatus).is(false)
                .and(SysDictItemQuery.Fields.dictId).is(dictId)
                .and(SysDictItemQuery.Fields.itemValue).is(itemValue);
        Query qry = Query.query(criteria);
        return r2dbcEntityTemplate.select(SysDictItemEntity.class).matching(qry).one().map(sysDictItemViewMapStruct::toView);
    }

    public Mono<List<SysDictItemView>> queryItemLabelByDictCode(String dictCode) {
        Criteria criteria = Criteria
                .where(SysDictQuery.Fields.deleteStatus).is(false)
                .and(SysDictQuery.Fields.dictCode).is(dictCode);
        Query qry = Query.query(criteria);
        return r2dbcEntityTemplate.select(SysDictEntity.class).matching(qry).one().flatMap(sysDictEntity -> {
            Criteria cri = Criteria
                    .where(SysDictItemQuery.Fields.deleteStatus).is(false)
                    .and(SysDictItemQuery.Fields.dictId).is(sysDictEntity.getId());
            Query q = Query.query(cri);
            return r2dbcEntityTemplate.select(SysDictItemEntity.class).matching(q).all().collectList().map(sysDictItemViewMapStruct::toViews);
        });
    }

    ;
}
