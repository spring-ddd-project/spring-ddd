package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenAggregatePageQuery;
import com.springddd.application.service.gen.dto.GenAggregateView;
import com.springddd.application.service.gen.dto.GenAggregateViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GenAggregateQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private GenAggregateViewMapStruct aggregateViewMapStruct;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<GenAggregateEntity> reactiveSelect;

    private GenAggregateQueryService genAggregateQueryService;

    @BeforeEach
    void setUp() {
        genAggregateQueryService = new GenAggregateQueryService(r2dbcEntityTemplate, aggregateViewMapStruct);
    }

    @Test
    void index_shouldReturnEmpty_whenInfoIdIsNull() {
        GenAggregatePageQuery query = new GenAggregatePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        StepVerifier.create(genAggregateQueryService.index(query))
                .verifyComplete();
    }

    @Test
    void index_shouldReturnPageResponse_whenEntitiesExist() {
        GenAggregatePageQuery query = new GenAggregatePageQuery();
        query.setInfoId(1L);
        query.setPageNum(1);
        query.setPageSize(10);

        GenAggregateEntity entity = new GenAggregateEntity();
        entity.setId(1L);
        entity.setInfoId(1L);

        GenAggregateView view = new GenAggregateView();
        view.setId(1L);
        view.setInfoId(1L);

        when(r2dbcEntityTemplate.select(GenAggregateEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(GenAggregateEntity.class))).thenReturn(Mono.just(1L));
        when(aggregateViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(genAggregateQueryService.index(query))
                .assertNext(pageResponse -> {
                    assertNotNull(pageResponse);
                    assertNotNull(pageResponse.getItems());
                })
                .verifyComplete();
    }

    @Test
    void queryAggregateByInfoId_shouldReturnViews_whenEntitiesExist() {
        Long infoId = 1L;
        GenAggregateEntity entity = new GenAggregateEntity();
        entity.setId(1L);
        entity.setInfoId(infoId);

        GenAggregateView view = new GenAggregateView();
        view.setId(1L);
        view.setInfoId(infoId);

        when(r2dbcEntityTemplate.select(GenAggregateEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(aggregateViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(genAggregateQueryService.queryAggregateByInfoId(infoId))
                .expectNext(Collections.singletonList(view))
                .verifyComplete();
    }
}
