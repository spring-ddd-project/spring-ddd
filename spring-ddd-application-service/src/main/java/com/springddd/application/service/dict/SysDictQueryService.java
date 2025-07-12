package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictItemView;
import com.springddd.application.service.dict.dto.SysDictPageQuery;
import com.springddd.application.service.dict.dto.SysDictView;
import com.springddd.application.service.dict.dto.SysDictViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysDictEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysDictQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final SysDictViewMapStruct sysDictViewMapStruct;

    private final SysDictItemQueryService sysDictItemQueryService;

    public Mono<PageResponse<SysDictView>> index(SysDictPageQuery query) {
        Criteria criteria = Criteria.where("delete_status").is(false);
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<SysDictView>> list = r2dbcEntityTemplate.select(SysDictEntity.class).matching(qry).all().collectList().map(sysDictViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysDictEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<String> queryItemLabelByDictCode(String dictCode, Integer itemValue) {
        Criteria criteria = Criteria
                .where("delete_status").is(false)
                .and("dict_code").is(dictCode);
        Query qry = Query.query(criteria);
        return r2dbcEntityTemplate.select(SysDictEntity.class).matching(qry).one().map(sysDictViewMapStruct::toView)
                .flatMap(sysDictView -> sysDictItemQueryService.queryItemLabelByItemValueAndDictId(sysDictView.getId(), itemValue)
                        .map(SysDictItemView::getItemLabel));
    }
}
