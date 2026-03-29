package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.*;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysDictEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysDictQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysDictViewMapStruct sysDictViewMapStruct;

    @Mock
    private SysDictItemQueryService sysDictItemQueryService;

    @Mock
    private SysDictItemViewMapStruct sysDictItemViewMapStruct;

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
    void index_shouldReturnPageResponse_whenDataExists() {
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDeleteStatus(false);

        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictName("Test Dict");
        entity.setDictCode("TEST_DICT");

        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictName("Test Dict");
        view.setDictCode("TEST_DICT");

        when(r2dbcEntityTemplate.select(SysDictEntity.class).matching(any(Query.class)).all())
                .thenReturn(reactor.core.publisher.Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), any()))
                .thenReturn(Mono.just(1L));
        when(sysDictViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        Mono<PageResponse<SysDictView>> result = sysDictQueryService.index(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 1 && response.getList().size() == 1)
                .verifyComplete();
    }

    @Test
    void index_shouldReturnEmptyPage_whenNoData() {
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDeleteStatus(false);

        when(r2dbcEntityTemplate.select(SysDictEntity.class).matching(any(Query.class)).all())
                .thenReturn(reactor.core.publisher.Flux.empty());
        when(r2dbcEntityTemplate.count(any(Query.class), any()))
                .thenReturn(Mono.just(0L));
        when(sysDictViewMapStruct.toViews(any())).thenReturn(Collections.emptyList());

        Mono<PageResponse<SysDictView>> result = sysDictQueryService.index(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 0 && response.getList().isEmpty())
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse_withDeletedItems() {
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDeleteStatus(true);

        when(r2dbcEntityTemplate.select(SysDictEntity.class).matching(any(Query.class)).all())
                .thenReturn(reactor.core.publisher.Flux.empty());
        when(r2dbcEntityTemplate.count(any(Query.class), any()))
                .thenReturn(Mono.just(0L));
        when(sysDictViewMapStruct.toViews(any())).thenReturn(Collections.emptyList());

        Mono<PageResponse<SysDictView>> result = sysDictQueryService.recycle(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 0)
                .verifyComplete();
    }

    @Test
    void queryAll_shouldReturnAllDictViews() {
        SysDictEntity entity = new SysDictEntity();
        entity.setId(1L);
        entity.setDictName("Dict 1");
        entity.setDictCode("DICT_1");
        entity.setDeleteStatus(false);

        SysDictView view = new SysDictView();
        view.setId(1L);
        view.setDictName("Dict 1");
        view.setDictCode("DICT_1");

        when(r2dbcEntityTemplate.select(SysDictEntity.class).matching(any(Query.class)).all())
                .thenReturn(reactor.core.publisher.Flux.just(entity));
        when(sysDictViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        Mono<List<SysDictView>> result = sysDictQueryService.queryAll();

        StepVerifier.create(result)
                .expectNextMatches(views -> views.size() == 1)
                .verifyComplete();
    }
}
