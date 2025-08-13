package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.*;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenColumnsQueryService {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final GenColumnsViewMapStruct genColumnsViewMapStruct;

    private final GenProjectInfoViewMapStruct genProjectInfoViewMapStruct;

    private final DatabaseClient databaseClient;

    private final GenColumnBindQueryService genColumnBindQueryService;

    public Mono<PageResponse<GenColumnsView>> queryColumnsByGenInfoId(Long infoId, String databaseName) {

        if (ObjectUtils.isEmpty(databaseName)) {
            return Mono.empty();
        }

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
                AND table_schema = :db
                ORDER BY
                  ordinal_position
                """;

        Mono<List<GenColumnsView>> coreColumns = r2dbcEntityTemplate.select(GenProjectInfoEntity.class).matching(Query.query(Criteria.where("id").is(infoId))).one().map(genProjectInfoViewMapStruct::toView)
                .flatMap(genInfo -> {
                    DatabaseClient.GenericExecuteSpec dataSpec = databaseClient.sql(sql)
                            .bind("tn", genInfo.getTableName())
                            .bind("db", databaseName);

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
                .flatMap(tuple -> {
                    List<GenColumnsView> db = tuple.getT1();
                    List<GenColumnsView> notDb = tuple.getT2();

                    return Flux.fromIterable(notDb)
                            .concatMap(column ->
                                    genColumnBindQueryService.queryByColumnType(column.getPropColumnType())
                                            .map(bind -> {
                                                column.setPropJavaEntity(SnakeToCamelConverter.convertToCamelCase(column.getPropColumnName()));

                                                Optional<GenColumnsView> matchedDbColumn = db.stream()
                                                        .filter(dbColumn -> dbColumn.getPropColumnType().equals(column.getPropColumnType()))
                                                        .findFirst();

                                                if (matchedDbColumn.isPresent()) {
                                                    GenColumnsView dbColumn = matchedDbColumn.get();
                                                    column.setPropJavaType(dbColumn.getPropJavaType());
                                                    column.setFormComponent(dbColumn.getFormComponent());
                                                    column.setTableVisible(dbColumn.getTableVisible());
                                                    column.setTableOrder(dbColumn.getTableOrder());
                                                    column.setTableFilter(dbColumn.getTableFilter());
                                                    column.setTableFilterComponent(dbColumn.getTableFilterComponent());
                                                    column.setTableFilterType(dbColumn.getTableFilterType());
                                                    column.setFormVisible(dbColumn.getFormVisible());
                                                    column.setFormComponent(dbColumn.getFormComponent());
                                                    column.setFormRequired(dbColumn.getFormRequired());
                                                    column.setPropDictId(dbColumn.getPropDictId());
                                                } else {
                                                    column.setPropJavaType(bind.getEntityType());
                                                    column.setFormComponent(bind.getComponentType());
                                                    column.setTableVisible(true);
                                                    column.setFormVisible(true);
                                                }

                                                return column;
                                            })
                            )
                            .collectList()
                            .map(list -> new PageResponse<>(list, 0, 0, 0));
                });

    }
}
