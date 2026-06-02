package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenProjectInfoPageQuery;
import com.springddd.application.service.gen.dto.GenProjectInfoQuery;
import com.springddd.application.service.gen.dto.GenProjectInfoView;
import com.springddd.application.service.gen.dto.GenProjectInfoViewMapStruct;
import com.springddd.domain.gen.exception.TableNameNullException;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
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
public class GenProjectInfoQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final GenProjectInfoViewMapStruct genProjectInfoViewMapStruct;

    public Mono<PageResponse<GenProjectInfoView>> index(GenProjectInfoQuery query) {
        Criteria criteria = Criteria.where(GenProjectInfoPageQuery.Fields.deleteStatus).is(false);
        if (!ObjectUtils.isEmpty(query.getTableName())) {
            criteria = criteria.and(GenProjectInfoPageQuery.Fields.tableName).is(query.getTableName());
        }
        Query qry = Query.query(criteria)
                .limit(Integer.MAX_VALUE)
                .offset(0);
        Mono<List<GenProjectInfoView>> list = r2dbcEntityTemplate.select(GenProjectInfoEntity.class).matching(qry).all().collectList().map(genProjectInfoViewMapStruct::toViews);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), GenProjectInfoEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), 0, 0));
    }

    public Mono<GenProjectInfoView> queryGenInfoByTableName(String tableName) {

        if (ObjectUtils.isEmpty(tableName)) {
            throw new TableNameNullException();
        }

        Criteria criteria = Criteria
                .where(GenProjectInfoQuery.Fields.deleteStatus).is(false)
                .and(GenProjectInfoQuery.Fields.tableName).is(tableName);
        Query qry = Query.query(criteria);
        return r2dbcEntityTemplate.select(GenProjectInfoEntity.class).matching(qry).one().map(genProjectInfoViewMapStruct::toView);
    }
}
