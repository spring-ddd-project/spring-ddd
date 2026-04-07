package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.*;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysDictEntity;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SysDictQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysDictViewMapStruct sysDictViewMapStruct;

    @Mock
    private SysDictItemQueryService sysDictItemQueryService;

    @Mock
    private SysDictItemViewMapStruct sysDictItemViewMapStruct;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysDictEntity> reactiveSelect;

    private SysDictQueryService sysDictQueryService;

    @BeforeEach
    void setUp() {
        sysDictQueryService = new SysDictQueryService(
                r2dbcEntityTemplate,
                sysDictViewMapStruct,
                sysDictItemQueryService,
                sysDictItemViewMapStruct
        );
    }

    @Test
    void index_shouldReturnPageResponse_whenEntitiesExist() {
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDictName("Test");
        query.setDictCode("TEST");

        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictName("Test Dict");
        entity.setDictCode("TEST");
        entity.setDeleteStatus(false);

        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictName("Test Dict");
        view.setDictCode("TEST");

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictEntity.class)))
                .thenReturn(Mono.just(1L));
        when(sysDictViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysDictQueryService.index(query))
                .assertNext(pageResponse -> {
                    assertNotNull(pageResponse);
                    assertNotNull(pageResponse.getItems());
                })
                .verifyComplete();
    }

    @Test
    void queryAll_shouldReturnAllDicts() {
        SysDictEntity entity1 = new SysDictEntity();
        entity1.setId(1L);
        entity1.setDictName("Dict 1");
        entity1.setDictCode("DICT_1");
        entity1.setDeleteStatus(false);

        SysDictEntity entity2 = new SysDictEntity();
        entity2.setId(2L);
        entity2.setDictName("Dict 2");
        entity2.setDictCode("DICT_2");
        entity2.setDeleteStatus(false);

        SysDictView view1 = new SysDictView();
        view1.setId(1L);
        view1.setDictName("Dict 1");
        view1.setDictCode("DICT_1");

        SysDictView view2 = new SysDictView();
        view2.setId(2L);
        view2.setDictName("Dict 2");
        view2.setDictCode("DICT_2");

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity1, entity2));
        when(sysDictViewMapStruct.toViews(any())).thenReturn(Arrays.asList(view1, view2));

        StepVerifier.create(sysDictQueryService.queryAll())
                .expectNext(Arrays.asList(view1, view2))
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse_whenDeletedEntitiesExist() {
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictName("Deleted Dict");
        entity.setDictCode("DELETED");
        entity.setDeleteStatus(true);

        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictName("Deleted Dict");
        view.setDictCode("DELETED");
        view.setDeleteStatus(true);

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDictEntity.class)))
                .thenReturn(Mono.just(1L));
        when(sysDictViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysDictQueryService.recycle(query))
                .assertNext(pageResponse -> {
                    assertNotNull(pageResponse);
                    assertNotNull(pageResponse.getItems());
                })
                .verifyComplete();
    }

    @Test
    void queryDictNameById_shouldReturnDictCode_whenEntityExists() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictName("Test Dict");
        entity.setDictCode("TEST");
        entity.setDeleteStatus(false);

        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictName("Test Dict");
        view.setDictCode("TEST");

        when(r2dbcEntityTemplate.select(SysDictEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.one()).thenReturn(Mono.just(entity));
        when(sysDictViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(sysDictQueryService.queryDictNameById(1L))
                .expectNext("TEST")
                .verifyComplete();
    }

    @Test
    void queryDictNameById_shouldReturnEmpty_whenIdIsNull() {
        StepVerifier.create(sysDictQueryService.queryDictNameById(null))
                .verifyComplete();
    }
}
