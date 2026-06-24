package com.springddd.application.service.gen;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.gen.dto.*;
import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenTableInfoQueryService {

    private final DatabaseClient databaseClient;

    private final GenProjectInfoQueryService projectInfoQueryService;

    private final GenColumnsQueryService columnsQueryService;

    private final GenAggregateQueryService aggregateQueryService;

    private final ReactiveRedisCacheHelper cacheHelper;

    private final ObjectMapper objectMapper;

    @Value("${spring.r2dbc.url:}")
    private String r2dbcUrl;

    public Mono<GenTableInfoPageResponse> index(GenTableInfoPageQuery query) {
        String databaseName = ObjectUtils.isEmpty(query.getDatabaseName())
                ? extractDatabaseName(r2dbcUrl)
                : query.getDatabaseName();

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
                .bind("db", databaseName)
                .bind("limit", limit)
                .bind("offset", offset);

        DatabaseClient.GenericExecuteSpec countSpec = databaseClient.sql(countSql.toString())
                .bind("db", databaseName);

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
                .map(tuple -> new GenTableInfoPageResponse(
                        tuple.getT1(),
                        tuple.getT2(),
                        query.getPageNum(),
                        query.getPageSize(),
                        databaseName
                ));
    }

    private String extractDatabaseName(String url) {
        if (!StringUtils.hasText(url)) {
            return "";
        }
        int lastSlash = url.lastIndexOf('/');
        if (lastSlash == -1 || lastSlash == url.length() - 1) {
            return "";
        }
        String databaseName = url.substring(lastSlash + 1);
        int queryStart = databaseName.indexOf('?');
        if (queryStart != -1) {
            databaseName = databaseName.substring(0, queryStart);
        }
        return databaseName;
    }

    public Mono<Map<String, Object>> buildData(String tableName) {
        GenView view = new GenView();
        return projectInfoQueryService.queryGenInfoByTableName(tableName)
                .flatMap(projectInfo -> {
                    view.setProjectInfoView(projectInfo);
                    return columnsQueryService.queryJavaEntityInfoByInfoId(projectInfo.getId())
                            .flatMap(columns -> {
                                view.setColumnsViews(columns);
                                return aggregateQueryService.queryAggregateByInfoId(projectInfo.getId())
                                        .flatMap(aggregates -> {
                                            view.setAggregateViews(aggregates);
                                            return buildTemplateContent(view);
                                        });
                            });
                });
    }

    private Mono<Map<String, Object>> buildTemplateContent(GenView genView) {
        GenProjectInfoView projectInfoView = genView.getProjectInfoView();
        List<GenColumnsView> columnsViews = genView.getColumnsViews();
        List<GenAggregateView> aggregateViews = genView.getAggregateViews();

        Map<String, Object> context = new HashMap<>();
        context.put("packageName", projectInfoView.getPackageName());
        context.put("tableName", projectInfoView.getTableName());
        context.put("className", projectInfoView.getClassName());
        context.put("requestName", projectInfoView.getRequestName());
        context.put("moduleName", projectInfoView.getModuleName());
        context.put("projectName", projectInfoView.getProjectName());
        context.put("columnsViews", columnsViews);
        context.put("aggregateViews", aggregateViews);

        return Mono.just(context);
    }

    public Mono<List<ProjectTreeView>> preview() {
        return ReactiveSecurityUtils.getCurrentUserId()
                .flatMap(userId -> cacheHelper.getCache(
                        CacheKeys.GEN_FILES.buildKey(userId),
                        List.class
                ))
                .flatMap(list -> {
                    try {
                        List<ProjectTreeView> treeViewList = objectMapper.convertValue(list, new TypeReference<>() {});
                        return Mono.just(treeViewList);
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error deserializing ProjectTreeView list"));
                    }
                });
    }


}
