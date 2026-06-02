package com.springddd.application.service.gen;

import com.springddd.application.service.dict.SysDictQueryService;
import com.springddd.application.service.gen.dto.*;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveSelectOperation;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GenColumnsQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private GenColumnsViewMapStruct genColumnsViewMapStruct;

    @Mock
    private GenProjectInfoViewMapStruct genProjectInfoViewMapStruct;

    @Mock
    private DatabaseClient databaseClient;

    @Mock
    private GenColumnBindQueryService genColumnBindQueryService;

    @Mock
    private SysDictQueryService sysDictQueryService;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<GenProjectInfoEntity> reactiveSelectInfo;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<GenColumnsEntity> reactiveSelectColumns;

    @Mock
    private DatabaseClient.GenericExecuteSpec genericExecuteSpec;

    @Mock
    private DatabaseClient.GenericExecuteSpec boundExecuteSpec;

    @Mock
    @SuppressWarnings("rawtypes")
    private RowsFetchSpec rowsFetchSpec;

    private GenColumnsQueryService service;

    @BeforeEach
    void setUp() {
        service = new GenColumnsQueryService(
                r2dbcEntityTemplate,
                genColumnsViewMapStruct,
                genProjectInfoViewMapStruct,
                databaseClient,
                genColumnBindQueryService,
                sysDictQueryService
        );
    }

    @Test
    void queryColumnsByGenInfoId_shouldReturnEmpty_whenDatabaseNameEmpty() {
        StepVerifier.create(service.queryColumnsByGenInfoId(1L, ""))
                .verifyComplete();
    }

    @Test
    void queryColumnsByGenInfoId_shouldReturnPageResponse() {
        GenProjectInfoEntity infoEntity = new GenProjectInfoEntity();
        infoEntity.setId(1L);
        infoEntity.setTableName("sys_user");

        GenProjectInfoView infoView = new GenProjectInfoView();
        infoView.setId(1L);
        infoView.setTableName("sys_user");

        GenColumnsEntity dbEntity = new GenColumnsEntity();
        dbEntity.setId(1L);
        dbEntity.setInfoId(1L);
        dbEntity.setPropColumnName("id");

        GenColumnsView dbView = new GenColumnsView();
        dbView.setId(1L);
        dbView.setInfoId(1L);
        dbView.setPropColumnName("id");
        dbView.setPropJavaType("Long");

        GenColumnsView coreView = new GenColumnsView();
        coreView.setPropColumnKey("id");
        coreView.setPropColumnName("id");
        coreView.setPropColumnType("bigint");
        coreView.setPropColumnComment("Primary Key");

        GenColumnBindView bindView = new GenColumnBindView();
        bindView.setEntityType("Long");
        bindView.setTypescriptType((byte) 1);
        bindView.setComponentType((byte) 1);

        when(r2dbcEntityTemplate.select(GenProjectInfoEntity.class)).thenReturn(reactiveSelectInfo);
        when(reactiveSelectInfo.matching(any(Query.class))).thenReturn(reactiveSelectInfo);
        when(reactiveSelectInfo.one()).thenReturn(Mono.just(infoEntity));
        when(genProjectInfoViewMapStruct.toView(infoEntity)).thenReturn(infoView);

        when(databaseClient.sql(anyString())).thenReturn(genericExecuteSpec);
        when(genericExecuteSpec.bind(anyString(), any())).thenReturn(boundExecuteSpec);
        when(boundExecuteSpec.bind(anyString(), any())).thenReturn(boundExecuteSpec);
        when(boundExecuteSpec.map(any(java.util.function.BiFunction.class))).thenReturn(rowsFetchSpec);
        when(rowsFetchSpec.all()).thenReturn(Flux.just(coreView));

        when(r2dbcEntityTemplate.select(GenColumnsEntity.class)).thenReturn(reactiveSelectColumns);
        when(reactiveSelectColumns.matching(any(Query.class))).thenReturn(reactiveSelectColumns);
        when(reactiveSelectColumns.all()).thenReturn(Flux.just(dbEntity));
        when(genColumnsViewMapStruct.toViews(anyList())).thenReturn(List.of(dbView));

        when(genColumnBindQueryService.queryByColumnType(anyString())).thenReturn(Mono.just(bindView));

        StepVerifier.create(service.queryColumnsByGenInfoId(1L, "spring_ddd"))
                .assertNext(page -> {
                    assertNotNull(page);
                    assertNotNull(page.getItems());
                })
                .verifyComplete();
    }

    @Test
    void queryJavaEntityInfoByInfoId_shouldReturnColumns() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setId(1L);
        entity.setInfoId(1L);
        entity.setTypescriptType((byte) 1);
        entity.setFormComponent((byte) 1);
        entity.setPropDictId(1L);

        GenColumnsView view = new GenColumnsView();
        view.setId(1L);
        view.setInfoId(1L);
        view.setTypescriptType((byte) 1);
        view.setFormComponent((byte) 1);
        view.setPropDictId(1L);

        when(r2dbcEntityTemplate.select(GenColumnsEntity.class)).thenReturn(reactiveSelectColumns);
        when(reactiveSelectColumns.matching(any(Query.class))).thenReturn(reactiveSelectColumns);
        when(reactiveSelectColumns.all()).thenReturn(Flux.just(entity));
        when(genColumnsViewMapStruct.toViews(anyList())).thenReturn(List.of(view));

        when(sysDictQueryService.queryItemLabelByDictCode(anyString(), any())).thenReturn(Mono.just("label"));
        when(sysDictQueryService.queryDictNameById(anyLong())).thenReturn(Mono.just("dictName"));

        StepVerifier.create(service.queryJavaEntityInfoByInfoId(1L))
                .assertNext(columns -> {
                    assertNotNull(columns);
                    assertEquals(1, columns.size());
                })
                .verifyComplete();
    }

    @Test
    void queryColumnsByGenInfoId_shouldHandleUnmatchedDbColumn() {
        GenProjectInfoEntity infoEntity = new GenProjectInfoEntity();
        infoEntity.setId(1L);
        infoEntity.setTableName("sys_user");

        GenProjectInfoView infoView = new GenProjectInfoView();
        infoView.setId(1L);
        infoView.setTableName("sys_user");

        GenColumnsView coreView = new GenColumnsView();
        coreView.setPropColumnKey("id");
        coreView.setPropColumnName("id");
        coreView.setPropColumnType("bigint");
        coreView.setPropColumnComment("Primary Key");

        GenColumnBindView bindView = new GenColumnBindView();
        bindView.setEntityType("Long");
        bindView.setTypescriptType((byte) 1);
        bindView.setComponentType((byte) 1);

        when(r2dbcEntityTemplate.select(GenProjectInfoEntity.class)).thenReturn(reactiveSelectInfo);
        when(reactiveSelectInfo.matching(any(Query.class))).thenReturn(reactiveSelectInfo);
        when(reactiveSelectInfo.one()).thenReturn(Mono.just(infoEntity));
        when(genProjectInfoViewMapStruct.toView(infoEntity)).thenReturn(infoView);

        when(databaseClient.sql(anyString())).thenReturn(genericExecuteSpec);
        when(genericExecuteSpec.bind(anyString(), any())).thenReturn(boundExecuteSpec);
        when(boundExecuteSpec.bind(anyString(), any())).thenReturn(boundExecuteSpec);
        when(boundExecuteSpec.map(any(java.util.function.BiFunction.class))).thenReturn(rowsFetchSpec);
        when(rowsFetchSpec.all()).thenReturn(Flux.just(coreView));

        when(r2dbcEntityTemplate.select(GenColumnsEntity.class)).thenReturn(reactiveSelectColumns);
        when(reactiveSelectColumns.matching(any(Query.class))).thenReturn(reactiveSelectColumns);
        when(reactiveSelectColumns.all()).thenReturn(Flux.empty());
        when(genColumnsViewMapStruct.toViews(anyList())).thenReturn(List.of());

        when(genColumnBindQueryService.queryByColumnType(anyString())).thenReturn(Mono.just(bindView));

        StepVerifier.create(service.queryColumnsByGenInfoId(1L, "spring_ddd"))
                .assertNext(page -> {
                    assertNotNull(page);
                    assertEquals(1, page.getItems().size());
                    GenColumnsView result = page.getItems().get(0);
                    assertEquals("Long", result.getPropJavaType());
                    assertNotNull(result.getEn());
                })
                .verifyComplete();
    }

    @Test
    @SuppressWarnings("unchecked")
    void queryColumnsByGenInfoId_shouldExecuteRowMapperLambda() {
        GenProjectInfoEntity infoEntity = new GenProjectInfoEntity();
        infoEntity.setId(1L);
        infoEntity.setTableName("sys_user");

        GenProjectInfoView infoView = new GenProjectInfoView();
        infoView.setId(1L);
        infoView.setTableName("sys_user");

        io.r2dbc.spi.Row row = mock(io.r2dbc.spi.Row.class);
        when(row.get("propColumnKey", String.class)).thenReturn("PRI");
        when(row.get("propColumnName", String.class)).thenReturn("id");
        when(row.get("propColumnType", String.class)).thenReturn("bigint");
        when(row.get("propColumnComment", String.class)).thenReturn("Primary Key");

        when(r2dbcEntityTemplate.select(GenProjectInfoEntity.class)).thenReturn(reactiveSelectInfo);
        when(reactiveSelectInfo.matching(any(Query.class))).thenReturn(reactiveSelectInfo);
        when(reactiveSelectInfo.one()).thenReturn(Mono.just(infoEntity));
        when(genProjectInfoViewMapStruct.toView(infoEntity)).thenReturn(infoView);

        when(databaseClient.sql(anyString())).thenReturn(genericExecuteSpec);
        when(genericExecuteSpec.bind(anyString(), any())).thenReturn(boundExecuteSpec);
        when(boundExecuteSpec.bind(anyString(), any())).thenReturn(boundExecuteSpec);

        when(boundExecuteSpec.map(any(java.util.function.BiFunction.class))).thenAnswer(invocation -> {
            java.util.function.BiFunction mapper = invocation.getArgument(0);
            Object mapped = mapper.apply(row, null);
            RowsFetchSpec fetchSpec = mock(RowsFetchSpec.class);
            when(fetchSpec.all()).thenReturn(Flux.just(mapped));
            return fetchSpec;
        });

        when(r2dbcEntityTemplate.select(GenColumnsEntity.class)).thenReturn(reactiveSelectColumns);
        when(reactiveSelectColumns.matching(any(Query.class))).thenReturn(reactiveSelectColumns);
        when(reactiveSelectColumns.all()).thenReturn(Flux.empty());
        when(genColumnsViewMapStruct.toViews(anyList())).thenReturn(List.of());

        GenColumnBindView bindView = new GenColumnBindView();
        bindView.setEntityType("Long");
        bindView.setTypescriptType((byte) 1);
        bindView.setComponentType((byte) 1);
        when(genColumnBindQueryService.queryByColumnType(anyString())).thenReturn(Mono.just(bindView));

        StepVerifier.create(service.queryColumnsByGenInfoId(1L, "spring_ddd"))
                .assertNext(page -> {
                    assertNotNull(page);
                    assertEquals(1, page.getItems().size());
                    assertEquals("id", page.getItems().get(0).getPropColumnName());
                })
                .verifyComplete();
    }
}
