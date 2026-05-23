package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictItemPageQuery;
import com.springddd.application.service.dict.dto.SysDictItemView;
import com.springddd.application.service.dict.dto.SysDictItemViewMapStruct;
import com.springddd.application.service.permission.DataScopeCriteriaBuilder;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
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
class SysDictItemQueryServiceTest {

    @Mock
    private SysDictItemViewMapStruct sysDictItemViewMapStruct;

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private DataScopeCriteriaBuilder dataScopeCriteriaBuilder;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysDictItemEntity> selectOp;

    @Mock
    private ReactiveSelectOperation.TerminatingSelect<SysDictItemEntity> terminatingSelect;

    @InjectMocks
    private SysDictItemQueryService service;

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
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDictId(1L);

        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDictId(1L);

        SysDictItemView view = new SysDictItemView();
        view.setId(1L);
        view.setDictId(1L);

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysDictItemViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictItemEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> assertThat(page.getList()).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("recycle 应返回回收站分页结果")
    void recycle_shouldReturnRecyclePage() {
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDictId(1L);

        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);

        SysDictItemView view = new SysDictItemView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysDictItemViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictItemEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.recycle(query))
                .assertNext(page -> assertThat(page.getList()).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryItemLabelByItemValueAndDictId 应返回字典项视图")
    void queryItemLabelByItemValueAndDictId_shouldReturnView() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setItemLabel("Male");

        SysDictItemView view = new SysDictItemView();
        view.setId(1L);
        view.setItemLabel("Male");

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.one()).thenReturn(Mono.just(entity));
        when(sysDictItemViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(service.queryItemLabelByItemValueAndDictId(1L, 1))
                .assertNext(result -> assertThat(result.getItemLabel()).isEqualTo("Male"))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryItemLabelByDictId 应返回字典项列表")
    void queryItemLabelByDictId_shouldReturnList() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setDictId(1L);

        SysDictItemView view = new SysDictItemView();
        view.setDictId(1L);

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysDictItemViewMapStruct.toViews(anyList())).thenReturn(List.of(view));

        StepVerifier.create(service.queryItemLabelByDictId(1L))
                .assertNext(list -> assertThat(list).hasSize(1))
                .verifyComplete();
    }
}
