package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenAggregatePageQuery;
import com.springddd.application.service.gen.dto.GenAggregateView;
import com.springddd.application.service.gen.dto.GenAggregateViewMapStruct;
import com.springddd.application.service.permission.DataScopeCriteriaBuilder;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
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
class GenAggregateQueryServiceTest {

    @Mock
    private GenAggregateViewMapStruct aggregateViewMapStruct;

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private DataScopeCriteriaBuilder dataScopeCriteriaBuilder;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<GenAggregateEntity> selectOp;

    @Mock
    private ReactiveSelectOperation.TerminatingSelect<GenAggregateEntity> terminatingSelect;

    @InjectMocks
    private GenAggregateQueryService service;

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
    @DisplayName("index 当 infoId 为空时应返回 empty")
    void index_whenEmptyInfoId_shouldReturnEmpty() {
        GenAggregatePageQuery query = new GenAggregatePageQuery();
        StepVerifier.create(service.index(query)).verifyComplete();
    }

    @Test
    @DisplayName("index 应返回分页结果")
    void index_shouldReturnPage() {
        GenAggregatePageQuery query = new GenAggregatePageQuery();
        query.setInfoId(1L);
        query.setPageNum(1);
        query.setPageSize(10);

        GenAggregateEntity entity = new GenAggregateEntity();
        entity.setId(1L);

        GenAggregateView view = new GenAggregateView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(GenAggregateEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(aggregateViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(GenAggregateEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> assertThat(page.getList()).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryAggregateByInfoId 应返回聚合列表")
    void queryAggregateByInfoId_shouldReturnList() {
        GenAggregateEntity entity = new GenAggregateEntity();
        entity.setInfoId(1L);

        GenAggregateView view = new GenAggregateView();
        view.setInfoId(1L);

        when(r2dbcEntityTemplate.select(GenAggregateEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(aggregateViewMapStruct.toViews(anyList())).thenReturn(List.of(view));

        StepVerifier.create(service.queryAggregateByInfoId(1L))
                .assertNext(list -> assertThat(list).hasSize(1))
                .verifyComplete();
    }
}
