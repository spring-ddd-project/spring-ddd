package com.springddd.application.service.gen;

import com.springddd.application.service.dict.SysDictQueryService;
import com.springddd.application.service.gen.dto.GenColumnBindView;
import com.springddd.application.service.gen.dto.GenColumnsView;
import com.springddd.application.service.gen.dto.GenColumnsViewMapStruct;
import com.springddd.application.service.gen.dto.GenProjectInfoView;
import com.springddd.application.service.gen.dto.GenProjectInfoViewMapStruct;
import com.springddd.application.service.permission.DataScopeCriteriaBuilder;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveSelectOperation;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.RowsFetchSpec;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class GenColumnsQueryServiceTest {

    @Mock
    private GenColumnsViewMapStruct genColumnsViewMapStruct;

    @Mock
    private GenProjectInfoViewMapStruct genProjectInfoViewMapStruct;

    @Mock
    private GenColumnBindQueryService genColumnBindQueryService;

    @Mock
    private SysDictQueryService sysDictQueryService;

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private DataScopeCriteriaBuilder dataScopeCriteriaBuilder;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private DatabaseClient databaseClient;

    @Mock
    private DatabaseClient.GenericExecuteSpec executeSpec;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<GenColumnsEntity> selectOp;

    @Mock
    private ReactiveSelectOperation.TerminatingSelect<GenColumnsEntity> terminatingSelect;

    @InjectMocks
    private GenColumnsQueryService service;

    @BeforeEach
    void setUp() throws Exception {
        Field qfField = service.getClass().getSuperclass().getDeclaredField("queryFactory");
        qfField.setAccessible(true);
        qfField.set(service, queryFactory);

        Field dscbField = service.getClass().getSuperclass().getDeclaredField("dataScopeCriteriaBuilder");
        dscbField.setAccessible(true);
        dscbField.set(service, dataScopeCriteriaBuilder);

        when(queryFactory.getR2dbcEntityTemplate()).thenReturn(r2dbcEntityTemplate);
        when(queryFactory.getDatabaseClient()).thenReturn(databaseClient);
    }

    @Test
    @DisplayName("queryColumnsByGenInfoId 当 databaseName 为空时应返回 empty")
    void queryColumnsByGenInfoId_whenEmptyDbName_shouldReturnEmpty() {
        StepVerifier.create(service.queryColumnsByGenInfoId(1L, ""))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryColumnsByGenInfoId 应合并数据库列与信息模式列")
    void queryColumnsByGenInfoId_shouldMergeDbAndCoreColumns() {
        Long infoId = 1L;
        String databaseName = "test_db";

        // Project info entity/view
        GenProjectInfoEntity projEntity = new GenProjectInfoEntity();
        projEntity.setId(infoId);
        projEntity.setTableName("sys_user");

        GenProjectInfoView projView = new GenProjectInfoView();
        projView.setId(infoId);
        projView.setTableName("sys_user");

        // Core columns from information_schema
        GenColumnsView coreColMatched = new GenColumnsView("PRI", "user_name", "varchar", "用户名");
        GenColumnsView coreColUnmatched = new GenColumnsView("", "email", "varchar", "邮箱");

        // DB columns from gen_columns table
        GenColumnsEntity dbEntity = new GenColumnsEntity();
        dbEntity.setId(10L);
        dbEntity.setInfoId(infoId);
        dbEntity.setPropColumnName("user_name");
        dbEntity.setPropJavaType("String");
        dbEntity.setTableVisible(true);
        dbEntity.setFormVisible(true);
        dbEntity.setFormComponent((byte) 2);
        dbEntity.setTypescriptType((byte) 3);
        dbEntity.setEn("User Name");
        dbEntity.setLocale("用户名");

        GenColumnsView dbView = new GenColumnsView();
        dbView.setId(10L);
        dbView.setInfoId(infoId);
        dbView.setPropColumnName("user_name");
        dbView.setPropJavaType("String");
        dbView.setTableVisible(true);
        dbView.setFormVisible(true);
        dbView.setFormComponent((byte) 2);
        dbView.setTypescriptType((byte) 3);
        dbView.setEn("User Name");
        dbView.setLocale("用户名");

        // Bind view for unmatched columns
        GenColumnBindView bindView = new GenColumnBindView();
        bindView.setEntityType("String");
        bindView.setTypescriptType((byte) 1);
        bindView.setComponentType((byte) 1);

        // Mock R2dbcEntityTemplate for project info
        @SuppressWarnings({"unchecked", "rawtypes"})
        ReactiveSelectOperation.ReactiveSelect rawSelectOp = selectOp;
        doReturn(rawSelectOp).when(r2dbcEntityTemplate).select(GenProjectInfoEntity.class);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        @SuppressWarnings({"unchecked", "rawtypes"})
        ReactiveSelectOperation.TerminatingSelect rawTerminatingSelect = terminatingSelect;
        when(rawTerminatingSelect.one()).thenReturn(Mono.just(projEntity));
        when(genProjectInfoViewMapStruct.toView(projEntity)).thenReturn(projView);

        // Mock DatabaseClient for information_schema query
        when(databaseClient.sql(anyString())).thenReturn(executeSpec);
        when(executeSpec.bind(anyString(), any())).thenReturn(executeSpec);
        @SuppressWarnings("unchecked")
        RowsFetchSpec<GenColumnsView> rowsFetchSpec = mock(RowsFetchSpec.class);
        when(executeSpec.map(any(BiFunction.class))).thenReturn(rowsFetchSpec);
        when(rowsFetchSpec.all()).thenReturn(Flux.just(coreColMatched, coreColUnmatched));

        // Mock R2dbcEntityTemplate for gen_columns query
        when(r2dbcEntityTemplate.select(GenColumnsEntity.class)).thenReturn(selectOp);
        when(terminatingSelect.all()).thenReturn(Flux.just(dbEntity));
        when(genColumnsViewMapStruct.toViews(anyList())).thenReturn(List.of(dbView));

        // Mock column bind service
        when(genColumnBindQueryService.queryByColumnType(anyString())).thenReturn(Mono.just(bindView));

        StepVerifier.create(service.queryColumnsByGenInfoId(infoId, databaseName))
                .assertNext(page -> {
                    assertThat(page.getList()).hasSize(2);

                    // Matched column should have DB properties
                    GenColumnsView matched = page.getList().stream()
                            .filter(c -> "user_name".equals(c.getPropColumnName()))
                            .findFirst().orElse(null);
                    assertThat(matched).isNotNull();
                    assertThat(matched.getId()).isEqualTo(10L);
                    assertThat(matched.getInfoId()).isEqualTo(infoId);
                    assertThat(matched.getPropJavaType()).isEqualTo("String");
                    assertThat(matched.getPropJavaEntity()).isEqualTo("userName");
                    assertThat(matched.getEn()).isEqualTo("User Name");
                    assertThat(matched.getLocale()).isEqualTo("用户名");

                    // Unmatched column should have default properties from bind
                    GenColumnsView unmatched = page.getList().stream()
                            .filter(c -> "email".equals(c.getPropColumnName()))
                            .findFirst().orElse(null);
                    assertThat(unmatched).isNotNull();
                    assertThat(unmatched.getId()).isNull();
                    assertThat(unmatched.getPropJavaType()).isEqualTo("String");
                    assertThat(unmatched.getPropJavaEntity()).isEqualTo("email");
                    assertThat(unmatched.getEn()).isEqualTo("Email");
                    assertThat(unmatched.getLocale()).isEqualTo("邮箱");
                    assertThat(unmatched.getTableVisible()).isTrue();
                    assertThat(unmatched.getFormVisible()).isTrue();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("queryColumnsByGenInfoId 应执行 map lambda 并合并数据库列")
    void queryColumnsByGenInfoId_shouldExecuteMapLambdaAndMergeColumns() {
        Long infoId = 1L;
        String databaseName = "test_db";

        GenProjectInfoEntity projEntity = new GenProjectInfoEntity();
        projEntity.setId(infoId);
        projEntity.setTableName("sys_user");

        GenProjectInfoView projView = new GenProjectInfoView();
        projView.setId(infoId);
        projView.setTableName("sys_user");

        GenColumnsEntity dbEntity = new GenColumnsEntity();
        dbEntity.setId(10L);
        dbEntity.setInfoId(infoId);
        dbEntity.setPropColumnName("user_name");
        dbEntity.setPropJavaType("String");
        dbEntity.setTableVisible(true);
        dbEntity.setFormVisible(true);
        dbEntity.setFormComponent((byte) 2);
        dbEntity.setTypescriptType((byte) 3);
        dbEntity.setEn("User Name");
        dbEntity.setLocale("用户名");

        GenColumnsView dbView = new GenColumnsView();
        dbView.setId(10L);
        dbView.setInfoId(infoId);
        dbView.setPropColumnName("user_name");
        dbView.setPropJavaType("String");
        dbView.setTableVisible(true);
        dbView.setFormVisible(true);
        dbView.setFormComponent((byte) 2);
        dbView.setTypescriptType((byte) 3);
        dbView.setEn("User Name");
        dbView.setLocale("用户名");

        GenColumnBindView bindView = new GenColumnBindView();
        bindView.setEntityType("String");
        bindView.setTypescriptType((byte) 1);
        bindView.setComponentType((byte) 1);

        AtomicReference<BiFunction<Row, RowMetadata, GenColumnsView>> mapperRef = new AtomicReference<>();

        // Mock project info query
        @SuppressWarnings({"unchecked", "rawtypes"})
        ReactiveSelectOperation.ReactiveSelect rawSelectOp = selectOp;
        doReturn(rawSelectOp).when(r2dbcEntityTemplate).select(GenProjectInfoEntity.class);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        @SuppressWarnings({"unchecked", "rawtypes"})
        ReactiveSelectOperation.TerminatingSelect rawTerminatingSelect = terminatingSelect;
        when(rawTerminatingSelect.one()).thenReturn(Mono.just(projEntity));
        when(genProjectInfoViewMapStruct.toView(projEntity)).thenReturn(projView);

        // Mock DatabaseClient for information_schema query with lambda capture
        when(databaseClient.sql(anyString())).thenReturn(executeSpec);
        when(executeSpec.bind(anyString(), any())).thenReturn(executeSpec);
        @SuppressWarnings("unchecked")
        RowsFetchSpec<GenColumnsView> rowsFetchSpec = mock(RowsFetchSpec.class);
        doAnswer(inv -> {
            @SuppressWarnings("unchecked")
            BiFunction<Row, RowMetadata, GenColumnsView> mapper = inv.getArgument(0);
            mapperRef.set(mapper);
            return rowsFetchSpec;
        }).when(executeSpec).map(any(BiFunction.class));

        Row mockRow = mock(Row.class);
        RowMetadata mockMeta = mock(RowMetadata.class);
        when(mockRow.get("propColumnKey", String.class)).thenReturn("PRI");
        when(mockRow.get("propColumnName", String.class)).thenReturn("user_name");
        when(mockRow.get("propColumnType", String.class)).thenReturn("varchar");
        when(mockRow.get("propColumnComment", String.class)).thenReturn("用户名");

        when(rowsFetchSpec.all()).thenAnswer(inv -> {
            GenColumnsView view = mapperRef.get().apply(mockRow, mockMeta);
            return Flux.just(view);
        });

        // Mock R2dbcEntityTemplate for gen_columns query
        when(r2dbcEntityTemplate.select(GenColumnsEntity.class)).thenReturn(selectOp);
        when(terminatingSelect.all()).thenReturn(Flux.just(dbEntity));
        when(genColumnsViewMapStruct.toViews(anyList())).thenReturn(List.of(dbView));

        // Mock column bind service
        when(genColumnBindQueryService.queryByColumnType(anyString())).thenReturn(Mono.just(bindView));

        StepVerifier.create(service.queryColumnsByGenInfoId(infoId, databaseName))
                .assertNext(page -> {
                    assertThat(page.getList()).hasSize(1);
                    GenColumnsView matched = page.getList().get(0);
                    assertThat(matched.getPropColumnName()).isEqualTo("user_name");
                    assertThat(matched.getPropColumnKey()).isEqualTo("PRI");
                    assertThat(matched.getPropColumnType()).isEqualTo("varchar");
                    assertThat(matched.getPropColumnComment()).isEqualTo("用户名");
                    assertThat(matched.getId()).isEqualTo(10L);
                    assertThat(matched.getPropJavaType()).isEqualTo("String");
                    assertThat(matched.getPropJavaEntity()).isEqualTo("userName");
                    assertThat(matched.getEn()).isEqualTo("User Name");
                    assertThat(matched.getLocale()).isEqualTo("用户名");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("queryJavaEntityInfoByInfoId 应返回列列表")
    void queryJavaEntityInfoByInfoId_shouldReturnList() {
        GenColumnsEntity entity = new GenColumnsEntity();
        entity.setId(1L);
        entity.setInfoId(1L);

        GenColumnsView view = new GenColumnsView();
        view.setId(1L);
        view.setInfoId(1L);
        view.setTypescriptType((byte) 1);
        view.setFormComponent((byte) 1);
        view.setPropDictId(0L);

        when(r2dbcEntityTemplate.select(GenColumnsEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(genColumnsViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(sysDictQueryService.queryItemLabelByDictCode(anyString(), any())).thenReturn(Mono.just("label"));
        when(sysDictQueryService.queryDictNameById(anyLong())).thenReturn(Mono.just("dict"));

        StepVerifier.create(service.queryJavaEntityInfoByInfoId(1L))
                .assertNext(list -> assertThat(list).hasSize(1))
                .verifyComplete();
    }
}
