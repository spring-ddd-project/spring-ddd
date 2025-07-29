package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenInfoPageQuery;
import com.springddd.application.service.gen.dto.GenInfoQuery;
import com.springddd.application.service.gen.dto.GenInfoView;
import com.springddd.application.service.gen.dto.GenInfoViewMapStruct;
import com.springddd.domain.gen.exception.TableNameNullException;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenInfoQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final GenInfoViewMapStruct genInfoViewMapStruct;

    private final DatabaseClient databaseClient;

    public Mono<PageResponse<GenInfoView>> index(GenInfoPageQuery query) {
        Criteria criteria = Criteria.where(GenInfoPageQuery.Fields.deleteStatus).is(false);
        if (!ObjectUtils.isEmpty(query.getTableName())) {
            criteria = criteria.and(GenInfoPageQuery.Fields.tableName).is(query.getTableName());
        }
        Query qry = Query.query(criteria);
        Mono<List<GenInfoView>> list = r2dbcEntityTemplate.select(GenInfoEntity.class).matching(qry).all().collectList().map(genInfoViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), GenInfoEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<GenInfoView> queryGenInfoByTableName(String tableName) {

        if (ObjectUtils.isEmpty(tableName)) {
            throw new TableNameNullException();
        }


        Criteria criteria = Criteria
                .where(GenInfoQuery.Fields.deleteStatus).is(false)
                .and(GenInfoQuery.Fields.tableName).is(tableName);
        Query qry = Query.query(criteria);
        Mono<GenInfoView> genInfo = r2dbcEntityTemplate.select(GenInfoEntity.class).matching(qry).one().map(genInfoViewMapStruct::toView).defaultIfEmpty(new GenInfoView());
        return Mono.zip(genInfo, columnData)
                .map(tuple -> {
                    GenInfoView genInfoView = tuple.getT1();
                    List<GenInfoView> t2 = tuple.getT2();
                    if (!CollectionUtils.isEmpty(t2)) {
                        genInfoView.setPropColumnName(t2.getFirst().getPropColumnName());
                        genInfoView.setPropColumnType(t2.getFirst().getPropColumnType());
                        genInfoView.setPropColumnComment(t2.getFirst().getPropColumnComment());
                        genInfoView.setPropColumnType(t2.getFirst().getPropColumnType());
                    }
                    return genInfoView;
                });
    }
}
