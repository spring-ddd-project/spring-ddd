package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenProjectInfoPageQuery;
import com.springddd.application.service.gen.dto.GenProjectInfoQuery;
import com.springddd.application.service.gen.dto.GenProjectInfoView;
import com.springddd.application.service.gen.dto.GenProjectInfoViewMapStruct;
import com.springddd.application.service.permission.DataScopeCriteriaBuilder;
import com.springddd.domain.gen.exception.TableNameNullException;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveSelectOperation;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class GenProjectInfoQueryServiceTest {

    @Mock
    private GenProjectInfoViewMapStruct genProjectInfoViewMapStruct;

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private DataScopeCriteriaBuilder dataScopeCriteriaBuilder;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<GenProjectInfoEntity> selectOp;

    @Mock
    private ReactiveSelectOperation.TerminatingSelect<GenProjectInfoEntity> terminatingSelect;

    @InjectMocks
    private GenProjectInfoQueryService service;

    @BeforeEach
    void setUp() throws Exception {
        Field qfField = service.getClass().getSuperclass().getDeclaredField("queryFactory");
        qfField.setAccessible(true);
        qfField.set(service, queryFactory);

        Field dscbField = service.getClass().getSuperclass().getDeclaredField("dataScopeCriteriaBuilder");
        dscbField.setAccessible(true);
        dscbField.set(service, dataScopeCriteriaBuilder);

        when(queryFactory.getR2dbcEntityTemplate()).thenReturn(r2dbcEntityTemplate);
        when(dataScopeCriteriaBuilder.apply(any(Criteria.class), any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
    }

    @Test
    @DisplayName("index 应返回分页结果")
    void index_shouldReturnPage() {
        GenProjectInfoQuery query = new GenProjectInfoQuery();
        query.setTableName("sys_user");

        GenProjectInfoEntity entity = new GenProjectInfoEntity();
        entity.setId(1L);
        entity.setTableName("sys_user");

        GenProjectInfoView view = new GenProjectInfoView();
        view.setId(1L);
        view.setTableName("sys_user");

        when(r2dbcEntityTemplate.select(GenProjectInfoEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(genProjectInfoViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(GenProjectInfoEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> assertThat(page.getList()).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryGenInfoByTableName 当表名为空时应抛异常")
    void queryGenInfoByTableName_whenEmpty_shouldThrow() {
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> service.queryGenInfoByTableName(""))
                .isInstanceOf(TableNameNullException.class);
    }

    @Test
    @DisplayName("queryGenInfoByTableName 应返回项目信息")
    void queryGenInfoByTableName_shouldReturnInfo() {
        GenProjectInfoEntity entity = new GenProjectInfoEntity();
        entity.setTableName("sys_user");

        GenProjectInfoView view = new GenProjectInfoView();
        view.setTableName("sys_user");

        when(r2dbcEntityTemplate.select(GenProjectInfoEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.one()).thenReturn(Mono.just(entity));
        when(genProjectInfoViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(service.queryGenInfoByTableName("sys_user"))
                .assertNext(result -> assertThat(result.getTableName()).isEqualTo("sys_user"))
                .verifyComplete();
    }
}
