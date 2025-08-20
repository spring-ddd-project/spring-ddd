package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenTableInfoPageQuery;
import com.springddd.application.service.gen.dto.GenTableInfoView;
import com.springddd.domain.util.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
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

        String dataSql = """
                    SELECT table_name, table_comment, create_time, table_collation
                    FROM information_schema.TABLES
                    WHERE table_schema = :db
                    ORDER BY create_time DESC
                    LIMIT :limit OFFSET :offset
                """;

        String countSql = """
                    SELECT COUNT(*) AS total
                    FROM information_schema.TABLES
                    WHERE table_schema = :db
                """;

        Mono<List<GenTableInfoView>> data = databaseClient.sql(dataSql)
                .bind("db", "spring_ddd")
                .bind("limit", limit)
                .bind("offset", offset)
                .map((row, meta) -> new GenTableInfoView(
                        row.get("table_name", String.class),
                        row.get("table_comment", String.class),
                        row.get("create_time", LocalDateTime.class),
                        row.get("table_collation", String.class)
                ))
                .all()
                .collectList();

        Mono<Long> count = databaseClient.sql(countSql)
                .bind("db", "spring_ddd")
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
