package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenColumnBindPageQuery;
import com.springddd.application.service.gen.dto.GenColumnBindView;
import com.springddd.application.service.gen.dto.GenColumnBindViewMapStruct;
import com.springddd.application.service.permission.DataScopeCriteriaBuilder;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenColumnBindEntity;
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
class GenColumnBindQueryServiceTest {

    @Mock
    private GenColumnBindViewMapStruct genColumnBindViewMapStruct;

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private DataScopeCriteriaBuilder dataScopeCriteriaBuilder;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<GenColumnBindEntity> selectOp;

    @Mock
    private ReactiveSelectOperation.TerminatingSelect<GenColumnBindEntity> terminatingSelect;

    @InjectMocks
    private GenColumnBindQueryService service;

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
        GenColumnBindPageQuery query = new GenColumnBindPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setColumnType("varchar");

        GenColumnBindEntity entity = new GenColumnBindEntity();
        entity.setId(1L);

        GenColumnBindView view = new GenColumnBindView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(GenColumnBindEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(genColumnBindViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(GenColumnBindEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> assertThat(page.getList()).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("recycle 应返回回收站分页结果")
    void recycle_shouldReturnRecyclePage() {
        GenColumnBindPageQuery query = new GenColumnBindPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        GenColumnBindEntity entity = new GenColumnBindEntity();
        entity.setId(1L);

        GenColumnBindView view = new GenColumnBindView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(GenColumnBindEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(genColumnBindViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(GenColumnBindEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.recycle(query))
                .assertNext(page -> assertThat(page.getList()).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryByColumnType 应返回列绑定视图")
    void queryByColumnType_shouldReturnView() {
        GenColumnBindEntity entity = new GenColumnBindEntity();
        entity.setColumnType("varchar");

        GenColumnBindView view = new GenColumnBindView();
        view.setColumnType("varchar");

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(GenColumnBindEntity.class))).thenReturn(Mono.just(entity));
        when(genColumnBindViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(service.queryByColumnType("varchar"))
                .assertNext(result -> assertThat(result.getColumnType()).isEqualTo("varchar"))
                .verifyComplete();
    }
}
