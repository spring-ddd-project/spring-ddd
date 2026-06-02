package com.springddd.application.service.gen;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.gen.dto.*;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GenTableInfoQueryServiceTest {

    @Mock
    private DatabaseClient databaseClient;

    @Mock
    private GenProjectInfoQueryService projectInfoQueryService;

    @Mock
    private GenColumnsQueryService columnsQueryService;

    @Mock
    private GenAggregateQueryService aggregateQueryService;

    @Mock
    private ReactiveRedisCacheHelper cacheHelper;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private DatabaseClient.GenericExecuteSpec genericExecuteSpec;

    @Mock
    private DatabaseClient.GenericExecuteSpec boundExecuteSpec;

    @Mock
    @SuppressWarnings("rawtypes")
    private RowsFetchSpec rowsFetchSpec;

    private GenTableInfoQueryService service;

    @BeforeEach
    void setUp() {
        service = new GenTableInfoQueryService(
                databaseClient,
                projectInfoQueryService,
                columnsQueryService,
                aggregateQueryService,
                cacheHelper,
                objectMapper
        );
    }

    @Test
    void index_shouldReturnEmpty_whenDatabaseNameEmpty() {
        GenTableInfoPageQuery query = new GenTableInfoPageQuery();
        query.setDatabaseName("");

        StepVerifier.create(service.index(query))
                .verifyComplete();
    }

    @Test
    void index_shouldReturnPageResponse() {
        GenTableInfoPageQuery query = new GenTableInfoPageQuery();
        query.setDatabaseName("spring_ddd");
        query.setPageNum(1);
        query.setPageSize(10);

        GenTableInfoView view = new GenTableInfoView();
        view.setTableSchema("spring_ddd");
        view.setTableName("sys_user");
        view.setTableComment("User Table");
        view.setCreateTime(LocalDateTime.now());
        view.setTableCollation("utf8mb4");

        when(databaseClient.sql(anyString())).thenReturn(genericExecuteSpec);
        when(genericExecuteSpec.bind(anyString(), any())).thenReturn(boundExecuteSpec);
        when(boundExecuteSpec.bind(anyString(), any())).thenReturn(boundExecuteSpec);
        when(boundExecuteSpec.map(any(java.util.function.BiFunction.class))).thenReturn(rowsFetchSpec);
        when(rowsFetchSpec.all()).thenReturn(Flux.just(view));
        when(rowsFetchSpec.one()).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> {
                    assertNotNull(page);
                    assertEquals(1, page.getPageNum());
                })
                .verifyComplete();
    }

    @Test
    void buildData_shouldReturnContextMap() {
        GenProjectInfoView projectInfo = new GenProjectInfoView();
        projectInfo.setId(1L);
        projectInfo.setTableName("sys_user");
        projectInfo.setPackageName("com.example");
        projectInfo.setClassName("User");
        projectInfo.setRequestName("user");
        projectInfo.setModuleName("sys");
        projectInfo.setProjectName("test");

        GenColumnsView column = new GenColumnsView();
        column.setPropColumnName("id");

        GenAggregateView aggregate = new GenAggregateView();
        aggregate.setObjectName("UserDomain");

        when(projectInfoQueryService.queryGenInfoByTableName("sys_user")).thenReturn(Mono.just(projectInfo));
        when(columnsQueryService.queryJavaEntityInfoByInfoId(1L)).thenReturn(Mono.just(List.of(column)));
        when(aggregateQueryService.queryAggregateByInfoId(1L)).thenReturn(Mono.just(List.of(aggregate)));

        StepVerifier.create(service.buildData("sys_user"))
                .assertNext(context -> {
                    assertNotNull(context);
                    assertEquals("com.example", context.get("packageName"));
                    assertEquals("sys_user", context.get("tableName"));
                    assertEquals("User", context.get("className"));
                })
                .verifyComplete();
    }

    @Test
    void preview_shouldReturnTreeList() {
        SecurityUtils.setUserId(1L);

        ProjectTreeView tree = new ProjectTreeView();
        tree.setLabel("test");
        tree.setChildren(new ArrayList<>());

        List<ProjectTreeView> treeList = List.of(tree);

        when(cacheHelper.getCache(CacheKeys.GEN_FILES.buildKey(1L), List.class))
                .thenReturn(Mono.just(treeList));
        when(objectMapper.convertValue(eq(treeList), any(TypeReference.class)))
                .thenReturn(treeList);

        StepVerifier.create(service.preview())
                .assertNext(result -> {
                    assertNotNull(result);
                    assertEquals(1, result.size());
                })
                .verifyComplete();
    }

    @Test
    void preview_shouldError_whenDeserializationFails() {
        SecurityUtils.setUserId(1L);

        List<ProjectTreeView> treeList = List.of();

        when(cacheHelper.getCache(CacheKeys.GEN_FILES.buildKey(1L), List.class))
                .thenReturn(Mono.just(treeList));
        when(objectMapper.convertValue(eq(treeList), any(TypeReference.class)))
                .thenThrow(new RuntimeException("deserialize error"));

        StepVerifier.create(service.preview())
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void index_shouldApplyTableNameFilter() {
        GenTableInfoPageQuery query = new GenTableInfoPageQuery();
        query.setDatabaseName("spring_ddd");
        query.setPageNum(1);
        query.setPageSize(10);
        query.setTableName("sys_user");

        GenTableInfoView view = new GenTableInfoView();
        view.setTableSchema("spring_ddd");
        view.setTableName("sys_user");
        view.setTableComment("User Table");
        view.setCreateTime(java.time.LocalDateTime.now());
        view.setTableCollation("utf8mb4");

        when(databaseClient.sql(anyString())).thenReturn(genericExecuteSpec);
        when(genericExecuteSpec.bind(anyString(), any())).thenReturn(boundExecuteSpec);
        when(boundExecuteSpec.bind(anyString(), any())).thenReturn(boundExecuteSpec);
        when(boundExecuteSpec.bind(anyString(), any())).thenReturn(boundExecuteSpec);
        when(boundExecuteSpec.map(any(java.util.function.BiFunction.class))).thenReturn(rowsFetchSpec);
        when(rowsFetchSpec.all()).thenReturn(Flux.just(view));
        when(rowsFetchSpec.one()).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> {
                    assertNotNull(page);
                    assertEquals(1, page.getItems().size());
                })
                .verifyComplete();
    }

    @Test
    @SuppressWarnings("unchecked")
    void index_shouldExecuteRowMapperLambda() {
        GenTableInfoPageQuery query = new GenTableInfoPageQuery();
        query.setDatabaseName("spring_ddd");
        query.setPageNum(1);
        query.setPageSize(10);

        io.r2dbc.spi.Row row = mock(io.r2dbc.spi.Row.class);
        when(row.get("table_schema", String.class)).thenReturn("spring_ddd");
        when(row.get("table_name", String.class)).thenReturn("sys_user");
        when(row.get("table_comment", String.class)).thenReturn("User Table");
        when(row.get("create_time", LocalDateTime.class)).thenReturn(LocalDateTime.now());
        when(row.get("table_collation", String.class)).thenReturn("utf8mb4");

        when(databaseClient.sql(anyString())).thenReturn(genericExecuteSpec);
        when(genericExecuteSpec.bind(anyString(), any())).thenReturn(boundExecuteSpec);
        when(boundExecuteSpec.bind(anyString(), any())).thenReturn(boundExecuteSpec);

        when(boundExecuteSpec.map(any(java.util.function.BiFunction.class))).thenAnswer(invocation -> {
            java.util.function.BiFunction mapper = invocation.getArgument(0);
            Object mapped = mapper.apply(row, null);
            if (mapped instanceof GenTableInfoView) {
                RowsFetchSpec<GenTableInfoView> fetchSpec = mock(RowsFetchSpec.class);
                when(fetchSpec.all()).thenReturn(Flux.just((GenTableInfoView) mapped));
                return fetchSpec;
            } else {
                RowsFetchSpec fetchSpec = mock(RowsFetchSpec.class);
                when(fetchSpec.one()).thenReturn(Mono.just(1L));
                return fetchSpec;
            }
        });

        StepVerifier.create(service.index(query))
                .assertNext(page -> {
                    assertNotNull(page);
                    assertEquals(1, page.getItems().size());
                    assertEquals("sys_user", page.getItems().get(0).getTableName());
                })
                .verifyComplete();
    }
}
