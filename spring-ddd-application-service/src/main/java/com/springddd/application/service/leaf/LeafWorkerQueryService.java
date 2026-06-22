package com.springddd.application.service.leaf;

import com.springddd.application.service.leaf.dto.*;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.LeafWorkerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeafWorkerQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final LeafWorkerViewMapStruct mapStruct;

    public Mono<PageResponse<LeafWorkerView>> index(LeafWorkerPageQuery query) {
        Criteria criteria = Criteria.where("delete_status").is(false);
        if (query.getWorkerId() != null) {
            criteria = criteria.and(Criteria.where("worker_id").is(query.getWorkerId()));
        }
        if (query.getDatacenterId() != null) {
            criteria = criteria.and(Criteria.where("datacenter_id").is(query.getDatacenterId()));
        }
        if (query.getIp() != null && !query.getIp().isEmpty()) {
            criteria = criteria.and(Criteria.where("ip").is(query.getIp()));
        }
        if (query.getLastTimestamp() != null) {
            criteria = criteria.and(Criteria.where("last_timestamp").is(query.getLastTimestamp()));
        }
        if (query.getPort() != null) {
            criteria = criteria.and(Criteria.where("port").is(query.getPort()));
        }
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<LeafWorkerView>> list = r2dbcEntityTemplate.select(LeafWorkerEntity.class).matching(qry).all().collectList().map(mapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), LeafWorkerEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<PageResponse<LeafWorkerView>> recycle(LeafWorkerPageQuery query) {
        Criteria criteria = Criteria.where("delete_status").is(true);
        if (query.getWorkerId() != null) {
            criteria = criteria.and(Criteria.where("worker_id").is(query.getWorkerId()));
        }
        if (query.getDatacenterId() != null) {
            criteria = criteria.and(Criteria.where("datacenter_id").is(query.getDatacenterId()));
        }
        if (query.getIp() != null && !query.getIp().isEmpty()) {
            criteria = criteria.and(Criteria.where("ip").is(query.getIp()));
        }
        if (query.getLastTimestamp() != null) {
            criteria = criteria.and(Criteria.where("last_timestamp").is(query.getLastTimestamp()));
        }
        if (query.getPort() != null) {
            criteria = criteria.and(Criteria.where("port").is(query.getPort()));
        }
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<LeafWorkerView>> list = r2dbcEntityTemplate.select(LeafWorkerEntity.class).matching(qry).all().collectList().map(mapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), LeafWorkerEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }
}
