package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictItemView;
import com.springddd.application.service.dict.dto.SysDictPageQuery;
import com.springddd.application.service.dict.dto.SysDictView;
import com.springddd.application.service.dict.dto.SysDictViewMapStruct;
import com.springddd.application.service.permission.DataScopeCriteriaBuilder;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysDictEntity;
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
class SysDictQueryServiceTest {

    @Mock
    private SysDictViewMapStruct sysDictViewMapStruct;

    @Mock
    private SysDictItemQueryService sysDictItemQueryService;

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private DataScopeCriteriaBuilder dataScopeCriteriaBuilder;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysDictEntity> selectOp;

    @Mock
    private ReactiveSelectOperation.TerminatingSelect<SysDictEntity> terminatingSelect;

    @InjectMocks
    private SysDictQueryService service;

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
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDictName("test");

        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictName("test");

        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictName("test");

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysDictViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> {
                    assertThat(page.getList()).hasSize(1);
                    assertThat(page.getList().get(0).getDictName()).isEqualTo("test");
                    assertThat(page.getTotal()).isEqualTo(1L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("queryAll 应返回所有字典列表")
    void queryAll_shouldReturnList() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictCode("gender");

        SysDictView view = new SysDictView();
        view.setDictCode("gender");

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysDictViewMapStruct.toViews(anyList())).thenReturn(List.of(view));

        StepVerifier.create(service.queryAll())
                .assertNext(list -> assertThat(list).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("recycle 应返回回收站分页结果")
    void recycle_shouldReturnRecyclePage() {
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);

        SysDictView view = new SysDictView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysDictViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.recycle(query))
                .assertNext(page -> assertThat(page.getList()).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryDictNameById 当 id 为空时应返回 empty")
    void queryDictNameById_whenEmptyId_shouldReturnEmpty() {
        StepVerifier.create(service.queryDictNameById(null))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryDictNameById 应返回字典编码")
    void queryDictNameById_shouldReturnDictCode() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictCode("gender");

        SysDictView view = new SysDictView();
        view.setDictCode("gender");

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.one()).thenReturn(Mono.just(entity));
        when(sysDictViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(service.queryDictNameById(1L))
                .assertNext(code -> assertThat(code).isEqualTo("gender"))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryItemLabelByDictCode 应返回字典项标签")
    void queryItemLabelByDictCode_shouldReturnLabel() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictCode("gender");

        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictCode("gender");

        SysDictItemView itemView = new SysDictItemView();
        itemView.setItemLabel("Male");

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.one()).thenReturn(Mono.just(entity));
        when(sysDictViewMapStruct.toView(entity)).thenReturn(view);
        when(sysDictItemQueryService.queryItemLabelByItemValueAndDictId(1L, 1)).thenReturn(Mono.just(itemView));

        StepVerifier.create(service.queryItemLabelByDictCode("gender", 1))
                .assertNext(label -> assertThat(label).isEqualTo("Male"))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryItemLabelByDictCode 当字典不存在时应返回 empty")
    void queryItemLabelByDictCode_whenDictNotFound_shouldReturnEmpty() {
        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.one()).thenReturn(Mono.empty());

        StepVerifier.create(service.queryItemLabelByDictCode("gender", 1))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryItemLabelByDictCode 当字典项不存在时应返回 empty")
    void queryItemLabelByDictCode_whenItemNotFound_shouldReturnEmpty() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictCode("gender");

        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictCode("gender");

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.one()).thenReturn(Mono.just(entity));
        when(sysDictViewMapStruct.toView(entity)).thenReturn(view);
        when(sysDictItemQueryService.queryItemLabelByItemValueAndDictId(1L, 1)).thenReturn(Mono.empty());

        StepVerifier.create(service.queryItemLabelByDictCode("gender", 1))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryDictByCode 应返回字典项列表")
    void queryDictByCode_shouldReturnItems() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictCode("gender");

        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictCode("gender");

        SysDictItemView itemView = new SysDictItemView();
        itemView.setItemLabel("Male");

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.one()).thenReturn(Mono.just(entity));
        when(sysDictViewMapStruct.toView(entity)).thenReturn(view);
        when(sysDictItemQueryService.queryItemLabelByDictId(1L)).thenReturn(Mono.just(List.of(itemView)));

        StepVerifier.create(service.queryDictByCode("gender"))
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    assertThat(list.get(0).getItemLabel()).isEqualTo("Male");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("queryDictByCode 当字典不存在时应返回 empty")
    void queryDictByCode_whenDictNotFound_shouldReturnEmpty() {
        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.one()).thenReturn(Mono.empty());

        StepVerifier.create(service.queryDictByCode("gender"))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryDictByCode 当字典项为空时应返回空列表")
    void queryDictByCode_whenItemsEmpty_shouldReturnEmptyList() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictCode("gender");

        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictCode("gender");

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.one()).thenReturn(Mono.just(entity));
        when(sysDictViewMapStruct.toView(entity)).thenReturn(view);
        when(sysDictItemQueryService.queryItemLabelByDictId(1L)).thenReturn(Mono.just(List.of()));

        StepVerifier.create(service.queryDictByCode("gender"))
                .assertNext(list -> assertThat(list).isEmpty())
                .verifyComplete();
    }

    @Test
    @DisplayName("index 同时传 dictName 和 dictCode 时应返回分页结果")
    void index_withDictNameAndDictCode_shouldReturnPage() {
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDictName("test");
        query.setDictCode("gender");

        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictName("test");
        entity.setDictCode("gender");

        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictName("test");
        view.setDictCode("gender");

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysDictViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> {
                    assertThat(page.getList()).hasSize(1);
                    assertThat(page.getList().get(0).getDictName()).isEqualTo("test");
                    assertThat(page.getList().get(0).getDictCode()).isEqualTo("gender");
                    assertThat(page.getTotal()).isEqualTo(1L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("index 仅传 dictCode 时应返回分页结果")
    void index_withDictCodeOnly_shouldReturnPage() {
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDictCode("gender");

        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictCode("gender");

        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictCode("gender");

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysDictViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> {
                    assertThat(page.getList()).hasSize(1);
                    assertThat(page.getList().get(0).getDictCode()).isEqualTo("gender");
                    assertThat(page.getTotal()).isEqualTo(1L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("index 不传任何查询条件时应返回分页结果")
    void index_withNoCriteria_shouldReturnPage() {
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictCode("status");

        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictCode("status");

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysDictViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> {
                    assertThat(page.getList()).hasSize(1);
                    assertThat(page.getTotal()).isEqualTo(1L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("index 当结果为空时应返回空分页")
    void index_withEmptyResults_shouldReturnEmptyPage() {
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.empty());
        when(sysDictViewMapStruct.toViews(anyList())).thenReturn(List.of());
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictEntity.class))).thenReturn(Mono.just(0L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> {
                    assertThat(page.getList()).isEmpty();
                    assertThat(page.getTotal()).isEqualTo(0L);
                })
                .verifyComplete();
    }
}
