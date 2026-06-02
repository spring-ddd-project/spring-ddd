package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenTemplatePageQuery;
import com.springddd.application.service.gen.dto.GenTemplateView;
import com.springddd.application.service.gen.dto.GenTemplateViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenTemplateEntity;
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
class GenTemplateQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private GenTemplateViewMapStruct genTemplateViewMapStruct;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<GenTemplateEntity> reactiveSelect;

    private GenTemplateQueryService genTemplateQueryService;

    @BeforeEach
    void setUp() {
        genTemplateQueryService = new GenTemplateQueryService(r2dbcEntityTemplate, genTemplateViewMapStruct);
    }

    @Test
    void index_shouldReturnPageResponse_whenEntitiesExist() {
        GenTemplatePageQuery query = new GenTemplatePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setTemplateName("Test");

        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setId(1L);
        entity.setTemplateName("TestTemplate");
        entity.setDeleteStatus(false);

        GenTemplateView view = new GenTemplateView();
        view.setId(1L);
        view.setTemplateName("TestTemplate");

        when(r2dbcEntityTemplate.select(GenTemplateEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(GenTemplateEntity.class))).thenReturn(Mono.just(1L));
        when(genTemplateViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(genTemplateQueryService.index(query))
                .assertNext(pageResponse -> {
                    assertNotNull(pageResponse);
                    assertNotNull(pageResponse.getItems());
                })
                .verifyComplete();
    }

    @Test
    void index_shouldReturnPageResponse_withoutTemplateName() {
        GenTemplatePageQuery query = new GenTemplatePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setId(1L);
        entity.setDeleteStatus(false);

        GenTemplateView view = new GenTemplateView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(GenTemplateEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(GenTemplateEntity.class))).thenReturn(Mono.just(1L));
        when(genTemplateViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(genTemplateQueryService.index(query))
                .assertNext(pageResponse -> assertNotNull(pageResponse))
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse_whenDeletedEntitiesExist() {
        GenTemplatePageQuery query = new GenTemplatePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setId(1L);
        entity.setDeleteStatus(true);

        GenTemplateView view = new GenTemplateView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(GenTemplateEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(GenTemplateEntity.class))).thenReturn(Mono.just(1L));
        when(genTemplateViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(genTemplateQueryService.recycle(query))
                .assertNext(pageResponse -> assertNotNull(pageResponse))
                .verifyComplete();
    }

    @Test
    void queryAllTemplate_shouldReturnAllViews() {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setId(1L);
        entity.setTemplateName("Template1");
        entity.setDeleteStatus(false);

        GenTemplateView view = new GenTemplateView();
        view.setId(1L);
        view.setTemplateName("Template1");

        when(r2dbcEntityTemplate.select(GenTemplateEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(genTemplateViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(genTemplateQueryService.queryAllTemplate())
                .expectNext(Collections.singletonList(view))
                .verifyComplete();
    }
}
