package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.*;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysDictEntity;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysDictQueryService {

    private final QueryFactory queryFactory;

    private final SysDictViewMapStruct sysDictViewMapStruct;

    private final SysDictItemQueryService sysDictItemQueryService;

    private final SysDictItemViewMapStruct sysDictItemViewMapStruct;

    public Mono<PageResponse<SysDictView>> index(SysDictPageQuery query) {
        Criteria criteria = Criteria.where(SysDictQuery.Fields.deleteStatus).is(false);
        if (!ObjectUtils.isEmpty(query.getDictName())) {
            criteria = criteria.and(SysDictQuery.Fields.dictName).like("%" + query.getDictName() + "%");
        }
        if (!ObjectUtils.isEmpty(query.getDictCode())) {
            criteria = criteria.and(SysDictQuery.Fields.dictCode).like("%" + query.getDictCode() + "%");
        }
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<SysDictView>> list = queryFactory.getR2dbcEntityTemplate().select(SysDictEntity.class).matching(qry).all().collectList().map(sysDictViewMapStruct::toViews);
        Mono<Long> count = queryFactory.getR2dbcEntityTemplate().count(Query.query(criteria), SysDictEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<List<SysDictView>> queryAll() {
        return queryFactory.getR2dbcEntityTemplate().select(SysDictEntity.class).matching(Query.query(Criteria.where(SysDictQuery.Fields.deleteStatus).is(false))).all().collectList().map(sysDictViewMapStruct::toViews);
    }

    public Mono<PageResponse<SysDictView>> recycle(SysDictPageQuery query) {
        Criteria criteria = Criteria.where(SysDictQuery.Fields.deleteStatus).is(true);
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<SysDictView>> list = queryFactory.getR2dbcEntityTemplate().select(SysDictEntity.class).matching(qry).all().collectList().map(sysDictViewMapStruct::toViews);
        Mono<Long> count = queryFactory.getR2dbcEntityTemplate().count(Query.query(criteria), SysDictEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<String> queryItemLabelByDictCode(String dictCode, Integer itemValue) {
        Criteria criteria = Criteria
                .where(SysDictQuery.Fields.deleteStatus).is(false)
                .and(SysDictQuery.Fields.dictCode).is(dictCode);
        Query qry = Query.query(criteria);
        return queryFactory.getR2dbcEntityTemplate().select(SysDictEntity.class).matching(qry).one().map(sysDictViewMapStruct::toView)
                .flatMap(sysDictView -> sysDictItemQueryService.queryItemLabelByItemValueAndDictId(sysDictView.getId(), itemValue)
                        .map(SysDictItemView::getItemLabel));
    }

    public Mono<List<SysDictItemView>> queryDictByCode(String code) {
        Criteria criteria = Criteria
                .where(SysDictQuery.Fields.deleteStatus).is(false)
                .and(SysDictQuery.Fields.dictCode).is(code);
        Query qry = Query.query(criteria);
        return queryFactory.getR2dbcEntityTemplate().select(SysDictEntity.class).matching(qry).one().map(sysDictViewMapStruct::toView)
                .flatMap(view -> sysDictItemQueryService.queryItemLabelByDictId(view.getId()));
    }

    public Mono<String> queryDictNameById(Long id) {
        if (ObjectUtils.isEmpty(id)) {
            return Mono.empty();
        }
        return queryFactory.getR2dbcEntityTemplate().select(SysDictEntity.class)
                .matching(Query.query(Criteria
                        .where(SysDictQuery.Fields.id).is(id)
                        .and(SysDictQuery.Fields.deleteStatus).is(false)))
                .one()
                .flatMap(entity -> {
                    SysDictView view = sysDictViewMapStruct.toView(entity);
                    return view != null ? Mono.just(view.getDictCode()) : Mono.empty();
                });
    }
}
}








































