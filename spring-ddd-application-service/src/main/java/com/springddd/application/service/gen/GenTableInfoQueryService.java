package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenTableInfoPageQuery;
import com.springddd.application.service.gen.dto.GenTableInfoView;
import com.springddd.domain.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenTableInfoQueryService {

    private final DatabaseClient databaseClient;

    public Mono<PageResponse<GenTableInfoView>> index(GenTableInfoPageQuery query) {
        int offset = (query.getPageNum() - 1) * query.getPageSize();
        int limit = query.getPageSize();

        StringBuilder dataSql = new StringBuilder("""
                    SELECT table_schema, table_name, table_comment, create_time, table_collation
                    FROM information_schema.TABLES
                    WHERE table_schema = :db
                """);

        StringBuilder countSql = new StringBuilder("""
                    SELECT COUNT(*) AS total
                    FROM information_schema.TABLES
                    WHERE table_schema = :db
                """);

        if (query.getTableName() != null && !query.getTableName().isEmpty()) {
            dataSql.append(" AND table_name LIKE :tn");
            countSql.append(" AND table_name LIKE :tn");
        }

        dataSql.append(" ORDER BY create_time DESC LIMIT :limit OFFSET :offset");

        DatabaseClient.GenericExecuteSpec dataSpec = databaseClient.sql(dataSql.toString())
                .bind("db", query.getDatabaseName())
                .bind("limit", limit)
                .bind("offset", offset);

        DatabaseClient.GenericExecuteSpec countSpec = databaseClient.sql(countSql.toString())
                .bind("db", query.getDatabaseName());

        if (!ObjectUtils.isEmpty(query.getTableName())) {
            String tableNameLike = "%" + query.getTableName() + "%";
            dataSpec = dataSpec.bind("tn", tableNameLike);
            countSpec = countSpec.bind("tn", tableNameLike);
        }

        Mono<List<GenTableInfoView>> data = dataSpec
                .map((row, meta) -> new GenTableInfoView(
                        row.get("table_schema", String.class),
                        row.get("table_name", String.class),
                        row.get("table_comment", String.class),
                        row.get("create_time", LocalDateTime.class),
                        row.get("table_collation", String.class)
                ))
                .all()
                .collectList();

        Mono<Long> count = countSpec
                .map((row, meta) -> row.get("total", Long.class))
                .one()
                .defaultIfEmpty(0L);

        return Mono.zip(data, count)
                .map(tuple -> new PageResponse<>(
                        tuple.getT1(),
                        tuple.getT2(),
                        query.getPageNum(),
                        query.getPageSize()
                ));
    }
}
