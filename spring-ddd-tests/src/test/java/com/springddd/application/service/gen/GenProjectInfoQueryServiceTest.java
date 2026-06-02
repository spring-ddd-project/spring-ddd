package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenProjectInfoQuery;
import com.springddd.application.service.gen.dto.GenProjectInfoView;
import com.springddd.application.service.gen.dto.GenProjectInfoViewMapStruct;
import com.springddd.domain.gen.exception.TableNameNullException;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
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
class GenProjectInfoQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private GenProjectInfoViewMapStruct genProjectInfoViewMapStruct;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<GenProjectInfoEntity> reactiveSelect;

    private GenProjectInfoQueryService genProjectInfoQueryService;

    @BeforeEach
    void setUp() {
        genProjectInfoQueryService = new GenProjectInfoQueryService(r2dbcEntityTemplate, genProjectInfoViewMapStruct);
    }

    @Test
    void index_shouldReturnPageResponse_whenEntitiesExist() {
        GenProjectInfoQuery query = new GenProjectInfoQuery();
        query.setTableName("sys_user");

        GenProjectInfoEntity entity = new GenProjectInfoEntity();
        entity.setId(1L);
        entity.setTableName("sys_user");
        entity.setDeleteStatus(false);

        GenProjectInfoView view = new GenProjectInfoView();
        view.setId(1L);
        view.setTableName("sys_user");

        when(r2dbcEntityTemplate.select(GenProjectInfoEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(GenProjectInfoEntity.class))).thenReturn(Mono.just(1L));
        when(genProjectInfoViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(genProjectInfoQueryService.index(query))
                .assertNext(pageResponse -> {
                    assertNotNull(pageResponse);
                    assertNotNull(pageResponse.getItems());
                })
                .verifyComplete();
    }

    @Test
    void index_shouldReturnPageResponse_withoutTableName() {
        GenProjectInfoQuery query = new GenProjectInfoQuery();

        GenProjectInfoEntity entity = new GenProjectInfoEntity();
        entity.setId(1L);
        entity.setDeleteStatus(false);

        GenProjectInfoView view = new GenProjectInfoView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(GenProjectInfoEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(GenProjectInfoEntity.class))).thenReturn(Mono.just(1L));
        when(genProjectInfoViewMapStruct.toViews(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(genProjectInfoQueryService.index(query))
                .assertNext(pageResponse -> assertNotNull(pageResponse))
                .verifyComplete();
    }

    @Test
    void queryGenInfoByTableName_shouldReturnView_whenEntityExists() {
        GenProjectInfoEntity entity = new GenProjectInfoEntity();
        entity.setId(1L);
        entity.setTableName("sys_user");
        entity.setDeleteStatus(false);

        GenProjectInfoView view = new GenProjectInfoView();
        view.setId(1L);
        view.setTableName("sys_user");

        when(r2dbcEntityTemplate.select(GenProjectInfoEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.one()).thenReturn(Mono.just(entity));
        when(genProjectInfoViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(genProjectInfoQueryService.queryGenInfoByTableName("sys_user"))
                .expectNext(view)
                .verifyComplete();
    }

    @Test
    void queryGenInfoByTableName_shouldThrowException_whenTableNameIsNull() {
        org.junit.jupiter.api.Assertions.assertThrows(TableNameNullException.class, () ->
                genProjectInfoQueryService.queryGenInfoByTableName(null)
        );
    }
}
