package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenColumnBindPageQuery;
import com.springddd.application.service.gen.dto.GenColumnBindView;
import com.springddd.application.service.gen.dto.GenColumnBindViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenColumnBindEntity;
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
class GenColumnBindQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private GenColumnBindViewMapStruct genColumnBindViewMapStruct;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<GenColumnBindEntity> reactiveSelect;

    private GenColumnBindQueryService genColumnBindQueryService;

    @BeforeEach
    void setUp() {
        genColumnBindQueryService = new GenColumnBindQueryService(r2dbcEntityTemplate, genColumnBindViewMapStruct);
    }

    @Test
    void index_shouldReturnPageResponse_whenEntitiesExist() {
        GenColumnBindPageQuery query = new GenColumnBindPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setColumnType("varchar");

        GenColumnBindEntity entity = new GenColumnBindEntity();
        entity.setId(1L);
        entity.setColumnType("varchar");
        entity.setDeleteStatus(false);

        GenColumnBindView view = new GenColumnBindView();
        view.setId(1L);
        view.setColumnType("varchar");

        when(r2dbcEntityTemplate.select(GenColumnBindEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(GenColumnBindEntity.class))).thenReturn(Mono.just(1L));
        when(genColumnBindViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(genColumnBindQueryService.index(query))
                .assertNext(pageResponse -> {
                    assertNotNull(pageResponse);
                    assertNotNull(pageResponse.getItems());
                })
                .verifyComplete();
    }

    @Test
    void index_shouldReturnPageResponse_withoutColumnType() {
        GenColumnBindPageQuery query = new GenColumnBindPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        GenColumnBindEntity entity = new GenColumnBindEntity();
        entity.setId(1L);
        entity.setDeleteStatus(false);

        GenColumnBindView view = new GenColumnBindView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(GenColumnBindEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(GenColumnBindEntity.class))).thenReturn(Mono.just(1L));
        when(genColumnBindViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(genColumnBindQueryService.index(query))
                .assertNext(pageResponse -> assertNotNull(pageResponse))
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse_whenDeletedEntitiesExist() {
        GenColumnBindPageQuery query = new GenColumnBindPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        GenColumnBindEntity entity = new GenColumnBindEntity();
        entity.setId(1L);
        entity.setDeleteStatus(true);

        GenColumnBindView view = new GenColumnBindView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(GenColumnBindEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(GenColumnBindEntity.class))).thenReturn(Mono.just(1L));
        when(genColumnBindViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(genColumnBindQueryService.recycle(query))
                .assertNext(pageResponse -> assertNotNull(pageResponse))
                .verifyComplete();
    }

    @Test
    void queryByColumnType_shouldReturnView_whenEntityExists() {
        GenColumnBindEntity entity = new GenColumnBindEntity();
        entity.setId(1L);
        entity.setColumnType("varchar");

        GenColumnBindView view = new GenColumnBindView();
        view.setId(1L);
        view.setColumnType("varchar");

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(GenColumnBindEntity.class))).thenReturn(Mono.just(entity));
        when(genColumnBindViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(genColumnBindQueryService.queryByColumnType("varchar"))
                .expectNext(view)
                .verifyComplete();
    }
}
