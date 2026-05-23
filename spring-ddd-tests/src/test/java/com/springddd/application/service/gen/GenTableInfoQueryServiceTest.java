package com.springddd.application.service.gen;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.gen.dto.GenAggregateView;
import com.springddd.application.service.gen.dto.GenColumnsView;
import com.springddd.application.service.gen.dto.GenProjectInfoView;
import com.springddd.application.service.gen.dto.GenTableInfoPageQuery;
import com.springddd.application.service.gen.dto.GenTableInfoView;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.infrastructure.cache.util.CacheProcessor;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GenTableInfoQueryServiceTest {

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private GenProjectInfoQueryService projectInfoQueryService;

    @Mock
    private GenColumnsQueryService columnsQueryService;

    @Mock
    private GenAggregateQueryService aggregateQueryService;

    @Mock
    private CacheProcessor cacheProcessor;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private DatabaseClient databaseClient;

    @Mock
    private DatabaseClient.GenericExecuteSpec executeSpec;

    @InjectMocks
    private GenTableInfoQueryService service;

    @BeforeEach
    void setUp() {
        SecurityUtils.setUserId(1L);
        when(queryFactory.getDatabaseClient()).thenReturn(databaseClient);
    }

    @Test
    @DisplayName("index 当 databaseName 为空时应返回 empty")
    void index_whenEmptyDbName_shouldReturnEmpty() {
        GenTableInfoPageQuery query = new GenTableInfoPageQuery();
        StepVerifier.create(service.index(query)).verifyComplete();
    }

    @Test
    @DisplayName("index 当 tableName 过滤条件存在时应返回分页结果")
    void index_withTableNameFilter_shouldReturnPage() {
        GenTableInfoPageQuery query = new GenTableInfoPageQuery();
        query.setDatabaseName("test_db");
        query.setTableName("user");
        query.setPageNum(1);
        query.setPageSize(10);

        GenTableInfoView view = new GenTableInfoView("test_db", "sys_user", "用户表", LocalDateTime.now(), "utf8mb4");

        when(databaseClient.sql(anyString())).thenReturn(executeSpec);
        when(executeSpec.bind(anyString(), any())).thenReturn(executeSpec);

        @SuppressWarnings("unchecked")
        RowsFetchSpec<GenTableInfoView> rowsFetchSpecData = mock(RowsFetchSpec.class);
        @SuppressWarnings("unchecked")
        RowsFetchSpec<Long> rowsFetchSpecCount = mock(RowsFetchSpec.class);

        when(executeSpec.map(any(BiFunction.class)))
                .thenReturn(rowsFetchSpecData)
                .thenReturn(rowsFetchSpecCount);

        when(rowsFetchSpecData.all()).thenReturn(Flux.just(view));
        when(rowsFetchSpecCount.one()).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> {
                    assertThat(page.getList()).hasSize(1);
                    assertThat(page.getTotal()).isEqualTo(1L);
                    assertThat(page.getPageNum()).isEqualTo(1);
                    assertThat(page.getPageSize()).isEqualTo(10);
                    assertThat(page.getList().get(0).getTableName()).isEqualTo("sys_user");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("index 当 tableName 过滤条件不存在时应返回分页结果")
    void index_withoutTableNameFilter_shouldReturnPage() {
        GenTableInfoPageQuery query = new GenTableInfoPageQuery();
        query.setDatabaseName("test_db");
        query.setPageNum(1);
        query.setPageSize(10);

        GenTableInfoView view = new GenTableInfoView("test_db", "sys_role", "角色表", LocalDateTime.now(), "utf8mb4");

        when(databaseClient.sql(anyString())).thenReturn(executeSpec);
        when(executeSpec.bind(anyString(), any())).thenReturn(executeSpec);

        @SuppressWarnings("unchecked")
        RowsFetchSpec<GenTableInfoView> rowsFetchSpecData = mock(RowsFetchSpec.class);
        @SuppressWarnings("unchecked")
        RowsFetchSpec<Long> rowsFetchSpecCount = mock(RowsFetchSpec.class);

        when(executeSpec.map(any(BiFunction.class)))
                .thenReturn(rowsFetchSpecData)
                .thenReturn(rowsFetchSpecCount);

        when(rowsFetchSpecData.all()).thenReturn(Flux.just(view));
        when(rowsFetchSpecCount.one()).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> {
                    assertThat(page.getList()).hasSize(1);
                    assertThat(page.getTotal()).isEqualTo(1L);
                    assertThat(page.getList().get(0).getTableName()).isEqualTo("sys_role");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("buildData 应返回构建数据")
    void buildData_shouldReturnMap() {
        GenProjectInfoView projectInfo = new GenProjectInfoView();
        projectInfo.setId(1L);
        projectInfo.setTableName("sys_user");
        projectInfo.setPackageName("com.example");
        projectInfo.setClassName("SysUser");
        projectInfo.setModuleName("system");
        projectInfo.setProjectName("demo");
        projectInfo.setRequestName("SysUserRequest");

        GenColumnsView colView = new GenColumnsView();
        colView.setId(1L);
        colView.setPropColumnName("id");

        GenAggregateView aggView = new GenAggregateView();
        aggView.setId(1L);
        aggView.setObjectName("user");

        when(projectInfoQueryService.queryGenInfoByTableName("sys_user")).thenReturn(Mono.just(projectInfo));
        when(columnsQueryService.queryJavaEntityInfoByInfoId(1L)).thenReturn(Mono.just(List.of(colView)));
        when(aggregateQueryService.queryAggregateByInfoId(1L)).thenReturn(Mono.just(List.of(aggView)));

        StepVerifier.create(service.buildData("sys_user"))
                .assertNext(result -> {
                    assertThat(result).containsKey("packageName");
                    assertThat(result).containsKey("tableName");
                    assertThat(result).containsKey("className");
                    assertThat(result).containsKey("requestName");
                    assertThat(result).containsKey("moduleName");
                    assertThat(result).containsKey("projectName");
                    assertThat(result).containsKey("columnsViews");
                    assertThat(result).containsKey("aggregateViews");
                    assertThat(result.get("packageName")).isEqualTo("com.example");
                    assertThat(result.get("tableName")).isEqualTo("sys_user");
                    assertThat(result.get("className")).isEqualTo("SysUser");
                    assertThat(result.get("requestName")).isEqualTo("SysUserRequest");
                    assertThat(result.get("moduleName")).isEqualTo("system");
                    assertThat(result.get("projectName")).isEqualTo("demo");
                    assertThat(result.get("columnsViews")).isEqualTo(List.of(colView));
                    assertThat(result.get("aggregateViews")).isEqualTo(List.of(aggView));
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("preview 缓存命中时应返回项目树")
    void preview_cacheHit_shouldReturnTree() {
        List<Map<String, Object>> cached = List.of(Map.of("label", "root"));
        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.just(cached));
        when(objectMapper.convertValue(any(), any(TypeReference.class))).thenReturn(List.of(new ProjectTreeView()));

        StepVerifier.create(service.preview())
                .assertNext(list -> assertThat(list).isNotNull())
                .verifyComplete();
    }

    @Test
    @DisplayName("preview 缓存未命中时应返回 empty")
    void preview_cacheMiss_shouldReturnEmpty() {
        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.empty());

        StepVerifier.create(service.preview())
                .verifyComplete();
    }

    @Test
    @DisplayName("preview 缓存反序列化异常时应返回 error")
    void preview_deserializeError_shouldReturnError() {
        List<Map<String, Object>> cached = List.of(Map.of("label", "root"));
        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.just(cached));
        when(objectMapper.convertValue(any(), any(TypeReference.class))).thenThrow(new RuntimeException("deserialize error"));

        StepVerifier.create(service.preview())
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    @DisplayName("index 应执行 map lambda 并返回分页结果")
    void index_shouldExecuteMapLambdas() {
        GenTableInfoPageQuery query = new GenTableInfoPageQuery();
        query.setDatabaseName("test_db");
        query.setTableName("user");
        query.setPageNum(1);
        query.setPageSize(10);

        AtomicReference<BiFunction<Row, RowMetadata, GenTableInfoView>> dataMapperRef = new AtomicReference<>();
        AtomicReference<BiFunction<Row, RowMetadata, Long>> countMapperRef = new AtomicReference<>();

        when(databaseClient.sql(anyString())).thenReturn(executeSpec);
        when(executeSpec.bind(anyString(), any())).thenReturn(executeSpec);

        @SuppressWarnings("unchecked")
        RowsFetchSpec<GenTableInfoView> rowsFetchSpecData = mock(RowsFetchSpec.class);
        @SuppressWarnings("unchecked")
        RowsFetchSpec<Long> rowsFetchSpecCount = mock(RowsFetchSpec.class);

        doAnswer(inv -> {
            BiFunction<?, ?, ?> mapper = inv.getArgument(0);
            if (dataMapperRef.get() == null) {
                @SuppressWarnings("unchecked")
                BiFunction<Row, RowMetadata, GenTableInfoView> typed = (BiFunction<Row, RowMetadata, GenTableInfoView>) mapper;
                dataMapperRef.set(typed);
                return rowsFetchSpecData;
            } else {
                @SuppressWarnings("unchecked")
                BiFunction<Row, RowMetadata, Long> typed = (BiFunction<Row, RowMetadata, Long>) mapper;
                countMapperRef.set(typed);
                return rowsFetchSpecCount;
            }
        }).when(executeSpec).map(any(BiFunction.class));

        Row mockRow = mock(Row.class);
        RowMetadata mockMeta = mock(RowMetadata.class);
        when(mockRow.get("table_schema", String.class)).thenReturn("test_db");
        when(mockRow.get("table_name", String.class)).thenReturn("sys_user");
        when(mockRow.get("table_comment", String.class)).thenReturn("用户表");
        when(mockRow.get("create_time", LocalDateTime.class)).thenReturn(LocalDateTime.now());
        when(mockRow.get("table_collation", String.class)).thenReturn("utf8mb4");
        when(mockRow.get("total", Long.class)).thenReturn(1L);

        when(rowsFetchSpecData.all()).thenAnswer(inv -> {
            GenTableInfoView view = dataMapperRef.get().apply(mockRow, mockMeta);
            return Flux.just(view);
        });
        when(rowsFetchSpecCount.one()).thenAnswer(inv -> {
            Long count = countMapperRef.get().apply(mockRow, mockMeta);
            return Mono.just(count);
        });

        StepVerifier.create(service.index(query))
                .assertNext(page -> {
                    assertThat(page.getList()).hasSize(1);
                    assertThat(page.getTotal()).isEqualTo(1L);
                    assertThat(page.getList().get(0).getTableName()).isEqualTo("sys_user");
                    assertThat(page.getList().get(0).getTableSchema()).isEqualTo("test_db");
                    assertThat(page.getList().get(0).getTableComment()).isEqualTo("用户表");
                    assertThat(page.getList().get(0).getTableCollation()).isEqualTo("utf8mb4");
                })
                .verifyComplete();
    }
}
