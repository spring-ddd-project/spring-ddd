package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.*;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.entity.GenInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenColumnsQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final GenColumnsViewMapStruct genColumnsViewMapStruct;

    private final GenInfoViewMapStruct genInfoViewMapStruct;

    private final DatabaseClient databaseClient;

    public Mono<List<GenColumnsView>> queryColumnsByGenInfoId(Long infoId) {

        String sql = """
                SELECT
                  column_key AS "propColumnKey",
                  column_name AS "propColumnName",
                  data_type AS "propColumnType",
                  column_comment AS "propColumnComment"
                FROM
                  information_schema.COLUMNS
                WHERE
                  table_name = :tn
                """;

        Mono<List<GenColumnsView>> coreColumns = r2dbcEntityTemplate.select(GenInfoEntity.class).matching(Query.query(Criteria.where("id").is(infoId))).one().map(genInfoViewMapStruct::toView)
                .flatMap(genInfo -> {
                    DatabaseClient.GenericExecuteSpec dataSpec = databaseClient.sql(sql)
                            .bind("tn", genInfo.getTableName());

                    return dataSpec
                            .map((row, meta) -> new GenColumnsView(
                                    row.get("propColumnKey", String.class),
                                    row.get("propColumnName", String.class),
                                    row.get("propColumnType", String.class),
                                    row.get("propColumnComment", String.class)
                            ))
                            .all()
                            .collectList();
                });

        Criteria criteria = Criteria
                .where(GenColumnsQuery.Fields.deleteStatus).is(false)
                .and(GenColumnsQuery.Fields.infoId).is(infoId);
        Query qry = Query.query(criteria);
        Mono<List<GenColumnsView>> columns = r2dbcEntityTemplate.select(GenColumnsEntity.class).matching(qry).all().collectList().map(genColumnsViewMapStruct::toViews).defaultIfEmpty(new ArrayList<>());
        return Mono.zip(columns, coreColumns)
                .map(tuple -> {
                    List<GenColumnsView> columnList = tuple.getT1();
                    List<GenColumnsView> coreColumnList = tuple.getT2();
                    for (GenColumnsView column : columnList) {
                        // TODO
                    }
                    return coreColumnList;
                });
    }
}
