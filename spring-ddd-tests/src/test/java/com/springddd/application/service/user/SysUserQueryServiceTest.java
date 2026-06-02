package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserPageQuery;
import com.springddd.application.service.user.dto.SysUserView;
import com.springddd.application.service.user.dto.SysUserViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
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
class SysUserQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysUserViewMapStruct sysUserViewMapStruct;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysUserEntity> reactiveSelect;

    private SysUserQueryService sysUserQueryService;

    @BeforeEach
    void setUp() {
        sysUserQueryService = new SysUserQueryService(r2dbcEntityTemplate, sysUserViewMapStruct);
    }

    @Test
    void page_shouldReturnPageResponse_whenEntitiesExist() {
        SysUserPageQuery query = new SysUserPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setUsername("test");
        query.setPhone("138");

        SysUserEntity entity = new SysUserEntity();
        entity.setId(1L);
        entity.setUsername("testuser");
        entity.setDeleteStatus(false);

        SysUserView view = new SysUserView();
        view.setId(1L);
        view.setUsername("testuser");

        when(r2dbcEntityTemplate.select(SysUserEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysUserEntity.class))).thenReturn(Mono.just(1L));
        when(sysUserViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysUserQueryService.page(query))
                .assertNext(pageResponse -> {
                    assertNotNull(pageResponse);
                    assertNotNull(pageResponse.getItems());
                })
                .verifyComplete();
    }

    @Test
    void page_shouldReturnPageResponse_withoutSearchCriteria() {
        SysUserPageQuery query = new SysUserPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysUserEntity entity = new SysUserEntity();
        entity.setId(1L);
        entity.setDeleteStatus(false);

        SysUserView view = new SysUserView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(SysUserEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysUserEntity.class))).thenReturn(Mono.just(1L));
        when(sysUserViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysUserQueryService.page(query))
                .assertNext(pageResponse -> assertNotNull(pageResponse))
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse_whenDeletedEntitiesExist() {
        SysUserPageQuery query = new SysUserPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysUserEntity entity = new SysUserEntity();
        entity.setId(1L);
        entity.setDeleteStatus(true);

        SysUserView view = new SysUserView();
        view.setId(1L);
        view.setDeleteStatus(true);

        when(r2dbcEntityTemplate.select(SysUserEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysUserEntity.class))).thenReturn(Mono.just(1L));
        when(sysUserViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysUserQueryService.recycle(query))
                .assertNext(pageResponse -> assertNotNull(pageResponse))
                .verifyComplete();
    }

    @Test
    void queryUserByUsername_shouldReturnView_whenEntityExists() {
        SysUserEntity entity = new SysUserEntity();
        entity.setId(1L);
        entity.setUsername("testuser");
        entity.setDeleteStatus(false);

        SysUserView view = new SysUserView();
        view.setId(1L);
        view.setUsername("testuser");

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysUserEntity.class))).thenReturn(Mono.just(entity));
        when(sysUserViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(sysUserQueryService.queryUserByUsername("testuser"))
                .expectNext(view)
                .verifyComplete();
    }
}
