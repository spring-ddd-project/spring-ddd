package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictItemPageQuery;
import com.springddd.application.service.dict.dto.SysDictItemView;
import com.springddd.application.service.dict.dto.SysDictItemViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
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
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SysDictItemQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysDictItemViewMapStruct sysDictItemViewMapStruct;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysDictItemEntity> reactiveSelect;

    private SysDictItemQueryService sysDictItemQueryService;

    @BeforeEach
    void setUp() {
        sysDictItemQueryService = new SysDictItemQueryService(r2dbcEntityTemplate, sysDictItemViewMapStruct);
    }

    @Test
    void index_shouldReturnPageResponse_whenEntitiesExist() {
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDictId(1L);

        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDictId(1L);
        entity.setDeleteStatus(false);

        SysDictItemView view = new SysDictItemView();
        view.setId(1L);
        view.setDictId(1L);

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictItemEntity.class))).thenReturn(Mono.just(1L));
        when(sysDictItemViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysDictItemQueryService.index(query))
                .assertNext(pageResponse -> {
                    assertNotNull(pageResponse);
                    assertNotNull(pageResponse.getItems());
                })
                .verifyComplete();
    }

    @Test
    void index_shouldReturnPageResponse_withoutDictId() {
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDeleteStatus(false);

        SysDictItemView view = new SysDictItemView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictItemEntity.class))).thenReturn(Mono.just(1L));
        when(sysDictItemViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysDictItemQueryService.index(query))
                .assertNext(pageResponse -> assertNotNull(pageResponse))
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse_whenDeletedEntitiesExist() {
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDictId(1L);

        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDeleteStatus(true);

        SysDictItemView view = new SysDictItemView();
        view.setId(1L);
        view.setDeleteStatus(true);

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictItemEntity.class))).thenReturn(Mono.just(1L));
        when(sysDictItemViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysDictItemQueryService.recycle(query))
                .assertNext(pageResponse -> assertNotNull(pageResponse))
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse_withoutDictId() {
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDeleteStatus(true);

        SysDictItemView view = new SysDictItemView();
        view.setId(1L);
        view.setDeleteStatus(true);

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictItemEntity.class))).thenReturn(Mono.just(1L));
        when(sysDictItemViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysDictItemQueryService.recycle(query))
                .assertNext(pageResponse -> assertNotNull(pageResponse))
                .verifyComplete();
    }

    @Test
    void queryItemLabelByItemValueAndDictId_shouldReturnView_whenEntityExists() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDictId(1L);
        entity.setItemValue(1);
        entity.setDeleteStatus(false);

        SysDictItemView view = new SysDictItemView();
        view.setId(1L);
        view.setItemLabel("Label");

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.one()).thenReturn(Mono.just(entity));
        when(sysDictItemViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(sysDictItemQueryService.queryItemLabelByItemValueAndDictId(1L, 1))
                .expectNext(view)
                .verifyComplete();
    }

    @Test
    void queryItemLabelByDictId_shouldReturnViews_whenEntitiesExist() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDictId(1L);
        entity.setDeleteStatus(false);

        SysDictItemView view = new SysDictItemView();
        view.setId(1L);
        view.setItemLabel("Label");

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(sysDictItemViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysDictItemQueryService.queryItemLabelByDictId(1L))
                .expectNext(Collections.singletonList(view))
                .verifyComplete();
    }

    @Test
    void queryItemLabelByItemValueAndDictId_shouldReturnEmpty_whenEntityNotFound() {
        when(r2dbcEntityTemplate.select(SysDictItemEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.one()).thenReturn(Mono.empty());

        StepVerifier.create(sysDictItemQueryService.queryItemLabelByItemValueAndDictId(1L, 1))
                .verifyComplete();
    }

    @Test
    void queryItemLabelByDictId_shouldReturnEmpty_whenNoEntitiesFound() {
        when(r2dbcEntityTemplate.select(SysDictItemEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.empty());

        StepVerifier.create(sysDictItemQueryService.queryItemLabelByDictId(1L))
                .expectNext(Collections.emptyList())
                .verifyComplete();
    }

    @Test
    void index_shouldHandleEmptyQueryViaObjectUtils() {
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDeleteStatus(false);

        SysDictItemView view = new SysDictItemView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictItemEntity.class))).thenReturn(Mono.just(1L));
        when(sysDictItemViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        try (var mocked = mockStatic(ObjectUtils.class, org.mockito.invocation.InvocationOnMock::callRealMethod)) {
            mocked.when(() -> ObjectUtils.isEmpty(query)).thenReturn(true);

            StepVerifier.create(sysDictItemQueryService.index(query))
                    .assertNext(pageResponse -> assertNotNull(pageResponse))
                    .verifyComplete();
        }
    }

    @Test
    void recycle_shouldHandleEmptyQueryViaObjectUtils() {
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDeleteStatus(true);

        SysDictItemView view = new SysDictItemView();
        view.setId(1L);
        view.setDeleteStatus(true);

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictItemEntity.class))).thenReturn(Mono.just(1L));
        when(sysDictItemViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        try (var mocked = mockStatic(ObjectUtils.class, org.mockito.invocation.InvocationOnMock::callRealMethod)) {
            mocked.when(() -> ObjectUtils.isEmpty(query)).thenReturn(true);

            StepVerifier.create(sysDictItemQueryService.recycle(query))
                    .assertNext(pageResponse -> assertNotNull(pageResponse))
                    .verifyComplete();
        }
    }
}
