package com.springddd.application.service.user;

import com.springddd.application.service.common.DataScopeQueryFilter;
import com.springddd.application.service.user.dto.SysUserPostPageQuery;
import com.springddd.application.service.user.dto.SysUserPostView;
import com.springddd.application.service.user.dto.SysUserPostViewMapStruct;
import com.springddd.infrastructure.persistence.entity.SysUserPostEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysUserPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class SysUserPostQueryServiceTest {

    private SysUserPostRepository sysUserPostRepository;
    private SysUserPostViewMapStruct sysUserPostViewMapStruct;
    private R2dbcEntityTemplate r2dbcEntityTemplate;
    private DataScopeQueryFilter dataScopeQueryFilter;
    private SysUserPostQueryService service;

    @BeforeEach
    void setUp() {
        sysUserPostRepository = Mockito.mock(SysUserPostRepository.class);
        sysUserPostViewMapStruct = Mockito.mock(SysUserPostViewMapStruct.class);
        r2dbcEntityTemplate = Mockito.mock(R2dbcEntityTemplate.class, Mockito.RETURNS_DEEP_STUBS);
        dataScopeQueryFilter = Mockito.mock(DataScopeQueryFilter.class);
        service = new SysUserPostQueryService(sysUserPostRepository, sysUserPostViewMapStruct, r2dbcEntityTemplate, dataScopeQueryFilter);
    }

    @Test
    void listByUserIdShouldReturnViews() {
        SysUserPostEntity entity = new SysUserPostEntity();
        entity.setId(1L);
        entity.setUserId(1L);
        entity.setPostId(10L);
        SysUserPostView view = new SysUserPostView();
        view.setId(1L);
        view.setUserId(1L);
        view.setPostId(10L);

        Mockito.when(sysUserPostRepository.findByUserIdAndDeleteStatusFalse(1L)).thenReturn(Flux.just(entity));
        Mockito.when(sysUserPostViewMapStruct.toViews(List.of(entity))).thenReturn(List.of(view));

        StepVerifier.create(service.listByUserId(1L))
                .assertNext(result -> {
                    org.junit.jupiter.api.Assertions.assertEquals(1, result.size());
                    org.junit.jupiter.api.Assertions.assertEquals(10L, result.get(0).getPostId());
                })
                .verifyComplete();
    }

    @Test
    void indexWithAllScopeShouldReturnPage() {
        SysUserPostPageQuery query = new SysUserPostPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysUserPostEntity entity = new SysUserPostEntity();
        entity.setId(1L);
        SysUserPostView view = new SysUserPostView();
        view.setId(1L);

        Mockito.when(dataScopeQueryFilter.apply(1L)).thenReturn(Mono.just(com.springddd.application.service.common.DataScopeResult.all()));
        Mockito.when(r2dbcEntityTemplate.select(SysUserPostEntity.class).matching(any(Query.class)).all().collectList()).thenReturn(Mono.just(List.of(entity)));
        Mockito.when(r2dbcEntityTemplate.count(any(Query.class), eq(SysUserPostEntity.class))).thenReturn(Mono.just(1L));
        Mockito.when(sysUserPostViewMapStruct.toViews(List.of(entity))).thenReturn(List.of(view));

        StepVerifier.create(service.index(1L, query))
                .assertNext(page -> {
                    org.junit.jupiter.api.Assertions.assertEquals(1, page.getItems().size());
                    org.junit.jupiter.api.Assertions.assertEquals(1L, page.getTotal());
                    org.junit.jupiter.api.Assertions.assertEquals(1, page.getPageNum());
                    org.junit.jupiter.api.Assertions.assertEquals(10, page.getPageSize());
                })
                .verifyComplete();
    }

    @Test
    void indexWithFiltersShouldApplyCriteria() {
        SysUserPostPageQuery query = new SysUserPostPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setUserId(1L);
        query.setPostId(10L);

        SysUserPostEntity entity = new SysUserPostEntity();
        entity.setId(1L);
        SysUserPostView view = new SysUserPostView();
        view.setId(1L);

        Mockito.when(dataScopeQueryFilter.apply(1L))
                .thenReturn(Mono.just(new com.springddd.application.service.common.DataScopeResult(Set.of("zhangsan"))));
        Mockito.when(r2dbcEntityTemplate.select(SysUserPostEntity.class).matching(any(Query.class)).all().collectList()).thenReturn(Mono.just(List.of(entity)));
        Mockito.when(r2dbcEntityTemplate.count(any(Query.class), eq(SysUserPostEntity.class))).thenReturn(Mono.just(1L));
        Mockito.when(sysUserPostViewMapStruct.toViews(List.of(entity))).thenReturn(List.of(view));

        StepVerifier.create(service.index(1L, query))
                .assertNext(page -> org.junit.jupiter.api.Assertions.assertEquals(1, page.getItems().size()))
                .verifyComplete();
    }

    @Test
    void recycleShouldReturnDeletedPage() {
        SysUserPostPageQuery query = new SysUserPostPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysUserPostEntity entity = new SysUserPostEntity();
        entity.setId(1L);
        SysUserPostView view = new SysUserPostView();
        view.setId(1L);

        Mockito.when(dataScopeQueryFilter.apply(1L)).thenReturn(Mono.just(com.springddd.application.service.common.DataScopeResult.all()));
        Mockito.when(r2dbcEntityTemplate.select(SysUserPostEntity.class).matching(any(Query.class)).all().collectList()).thenReturn(Mono.just(List.of(entity)));
        Mockito.when(r2dbcEntityTemplate.count(any(Query.class), eq(SysUserPostEntity.class))).thenReturn(Mono.just(1L));
        Mockito.when(sysUserPostViewMapStruct.toViews(List.of(entity))).thenReturn(List.of(view));

        StepVerifier.create(service.recycle(1L, query))
                .assertNext(page -> org.junit.jupiter.api.Assertions.assertEquals(1, page.getTotal()))
                .verifyComplete();
    }
}
