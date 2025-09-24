package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.*;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
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
class SysDictItemQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysDictItemViewMapStruct sysDictItemViewMapStruct;

    private SysDictItemQueryService sysDictItemQueryService;

    @BeforeEach
    void setUp() {
        sysDictItemQueryService = new SysDictItemQueryService(r2dbcEntityTemplate, sysDictItemViewMapStruct);
    }

    @Test
    void index_shouldReturnPageResponse_whenDataExists() {
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDeleteStatus(false);
        query.setDictId(1L);

        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDictId(1L);
        entity.setItemLabel("Item 1");
        entity.setItemValue(1);

        SysDictItemView view = new SysDictItemView();
        view.setId(1L);
        view.setDictId(1L);
        view.setItemLabel("Item 1");
        view.setItemValue(1);

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class).matching(any(Query.class)).all())
                .thenReturn(reactor.core.publisher.Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), any()))
                .thenReturn(Mono.just(1L));
        when(sysDictItemViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        Mono<PageResponse<SysDictItemView>> result = sysDictItemQueryService.index(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 1 && response.getList().size() == 1)
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse_withDeletedItems() {
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDeleteStatus(true);
        query.setDictId(1L);

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class).matching(any(Query.class)).all())
                .thenReturn(reactor.core.publisher.Flux.empty());
        when(r2dbcEntityTemplate.count(any(Query.class), any()))
                .thenReturn(Mono.just(0L));
        when(sysDictItemViewMapStruct.toViews(any())).thenReturn(Collections.emptyList());

        Mono<PageResponse<SysDictItemView>> result = sysDictItemQueryService.recycle(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 0)
                .verifyComplete();
    }

    @Test
    void queryItemLabelByItemValueAndDictId_shouldReturnItem() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDictId(1L);
        entity.setItemLabel("Item 1");
        entity.setItemValue(1);

        SysDictItemView view = new SysDictItemView();
        view.setId(1L);
        view.setDictId(1L);
        view.setItemLabel("Item 1");
        view.setItemValue(1);

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class).matching(any(Query.class)).one())
                .thenReturn(Mono.just(entity));
        when(sysDictItemViewMapStruct.toView(any())).thenReturn(view);

        Mono<SysDictItemView> result = sysDictItemQueryService.queryItemLabelByItemValueAndDictId(1L, 1);

        StepVerifier.create(result)
                .expectNextMatches(item -> item.getItemLabel().equals("Item 1"))
                .verifyComplete();
    }

    @Test
    void queryItemLabelByDictId_shouldReturnItemList() {
        SysDictItemEntity entity = new SysDictItemEntity();
        entity.setId(1L);
        entity.setDictId(1L);
        entity.setItemLabel("Item 1");
        entity.setItemValue(1);

        SysDictItemView view = new SysDictItemView();
        view.setId(1L);
        view.setDictId(1L);
        view.setItemLabel("Item 1");
        view.setItemValue(1);

        when(r2dbcEntityTemplate.select(SysDictItemEntity.class).matching(any(Query.class)).all())
                .thenReturn(reactor.core.publisher.Flux.just(entity));
        when(sysDictItemViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        Mono<List<SysDictItemView>> result = sysDictItemQueryService.queryItemLabelByDictId(1L);

        StepVerifier.create(result)
                .expectNextMatches(items -> items.size() == 1)
                .verifyComplete();
    }
}
