package com.springddd.application.service.leaf;

import com.springddd.application.service.leaf.dto.LeafAllocPageQuery;
import com.springddd.application.service.leaf.dto.LeafAllocView;
import com.springddd.application.service.leaf.dto.LeafAllocViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
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
public class LeafAllocQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final LeafAllocViewMapStruct leafAllocViewMapStruct;

    public Mono<PageResponse<LeafAllocView>> page(LeafAllocPageQuery query) {
        Criteria criteria = Criteria.where(LeafAllocPageQuery.Fields.deleteStatus).is(false);
        if (!ObjectUtils.isEmpty(query.getBizTag())) {
            criteria = criteria.and(LeafAllocPageQuery.Fields.bizTag).like("%" + query.getBizTag() + "%");
        }
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());

        Mono<List<LeafAllocView>> list = r2dbcEntityTemplate.select(LeafAllocEntity.class).matching(qry).all()
                .collectList().map(leafAllocViewMapStruct::toViewList);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), LeafAllocEntity.class);
        return Mono.zip(list, count)
                .map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<PageResponse<LeafAllocView>> recycle(LeafAllocPageQuery query) {
        Criteria criteria = Criteria.where(LeafAllocPageQuery.Fields.deleteStatus).is(true);
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<LeafAllocView>> list = r2dbcEntityTemplate.select(LeafAllocEntity.class).matching(qry).all()
                .collectList().map(leafAllocViewMapStruct::toViewList);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), LeafAllocEntity.class);
        return Mono.zip(list, count)
                .map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }
}
