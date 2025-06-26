package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictItemPageQuery;
import com.springddd.application.service.dict.dto.SysDictItemView;
import com.springddd.application.service.dict.dto.SysDictItemViewMapStruct;
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

    public Mono<PageResponse<SysDictItemView>> index(SysDictItemPageQuery query) {
        Criteria criteria = Criteria
                .where("delete_status").is(false);
        if (!ObjectUtils.isEmpty(query) && !ObjectUtils.isEmpty(query.getDictId())) {
            criteria.and("dict_id").is(query.getDictId());
        }
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<SysDictItemView>> list = r2dbcEntityTemplate.select(SysDictItemEntity.class).matching(qry).all().collectList().map(sysDictItemViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysDictItemEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }
}
