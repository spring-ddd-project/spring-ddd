package com.springddd.application.service.post;

import com.springddd.application.service.common.DataScopeQueryFilter;
import com.springddd.application.service.post.dto.SysPostQuery;
import com.springddd.application.service.post.dto.SysPostView;
import com.springddd.application.service.post.dto.SysPostViewMapStruct;
import com.springddd.infrastructure.persistence.entity.SysPostEntity;
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

class SysPostQueryServiceTest {

    private R2dbcEntityTemplate r2dbcEntityTemplate;
    private SysPostViewMapStruct sysPostViewMapStruct;
    private DataScopeQueryFilter dataScopeQueryFilter;
    private SysPostQueryService service;

    @BeforeEach
    void setUp() {
        r2dbcEntityTemplate = Mockito.mock(R2dbcEntityTemplate.class, Mockito.RETURNS_DEEP_STUBS);
        sysPostViewMapStruct = Mockito.mock(SysPostViewMapStruct.class);
        dataScopeQueryFilter = Mockito.mock(DataScopeQueryFilter.class);
        service = new SysPostQueryService(r2dbcEntityTemplate, sysPostViewMapStruct, dataScopeQueryFilter);
    }

    @Test
    void indexWithAllScopeShouldReturnPage() {
        SysPostQuery query = new SysPostQuery();
        SysPostEntity entity = new SysPostEntity();
        entity.setId(1L);
        SysPostView view = new SysPostView();
        view.setId(1L);

        Mockito.when(dataScopeQueryFilter.apply(1L)).thenReturn(Mono.just(com.springddd.application.service.common.DataScopeResult.all()));
        Mockito.when(r2dbcEntityTemplate.select(SysPostEntity.class).matching(any(Query.class)).all().collectList()).thenReturn(Mono.just(List.of(entity)));
        Mockito.when(r2dbcEntityTemplate.count(any(Query.class), eq(SysPostEntity.class))).thenReturn(Mono.just(1L));
        Mockito.when(sysPostViewMapStruct.toViews(List.of(entity))).thenReturn(List.of(view));

        StepVerifier.create(service.index(1L, query))
                .assertNext(page -> {
                    org.junit.jupiter.api.Assertions.assertEquals(1, page.getItems().size());
                    org.junit.jupiter.api.Assertions.assertEquals(1L, page.getTotal());
                })
                .verifyComplete();
    }

    @Test
    void indexWithPersonalScopeShouldFilterByCreateBy() {
        SysPostQuery query = new SysPostQuery();
        SysPostEntity entity = new SysPostEntity();
        entity.setId(1L);
        SysPostView view = new SysPostView();
        view.setId(1L);

        Mockito.when(dataScopeQueryFilter.apply(1L))
                .thenReturn(Mono.just(new com.springddd.application.service.common.DataScopeResult(Set.of("zhangsan"))));
        Mockito.when(r2dbcEntityTemplate.select(SysPostEntity.class).matching(any(Query.class)).all().collectList()).thenReturn(Mono.just(List.of(entity)));
        Mockito.when(r2dbcEntityTemplate.count(any(Query.class), eq(SysPostEntity.class))).thenReturn(Mono.just(1L));
        Mockito.when(sysPostViewMapStruct.toViews(List.of(entity))).thenReturn(List.of(view));

        StepVerifier.create(service.index(1L, query))
                .assertNext(page -> org.junit.jupiter.api.Assertions.assertEquals(1, page.getItems().size()))
                .verifyComplete();
    }

    @Test
    void recycleShouldReturnDeletedPage() {
        SysPostQuery query = new SysPostQuery();
        SysPostEntity entity = new SysPostEntity();
        entity.setId(1L);
        SysPostView view = new SysPostView();
        view.setId(1L);

        Mockito.when(dataScopeQueryFilter.apply(1L)).thenReturn(Mono.just(com.springddd.application.service.common.DataScopeResult.all()));
        Mockito.when(r2dbcEntityTemplate.select(SysPostEntity.class).matching(any(Query.class)).all().collectList()).thenReturn(Mono.just(List.of(entity)));
        Mockito.when(r2dbcEntityTemplate.count(any(Query.class), eq(SysPostEntity.class))).thenReturn(Mono.just(1L));
        Mockito.when(sysPostViewMapStruct.toViews(List.of(entity))).thenReturn(List.of(view));

        StepVerifier.create(service.recycle(1L, query))
                .assertNext(page -> org.junit.jupiter.api.Assertions.assertEquals(1, page.getTotal()))
                .verifyComplete();
    }

    @Test
    void postTreeShouldBuildHierarchy() {
        SysPostEntity root = new SysPostEntity();
        root.setId(1L);
        root.setParentId(null);
        root.setSortOrder(1);
        root.setPostStatus(true);
        root.setDeleteStatus(false);
        SysPostEntity child = new SysPostEntity();
        child.setId(2L);
        child.setParentId(1L);
        child.setSortOrder(1);
        child.setPostStatus(true);
        child.setDeleteStatus(false);

        SysPostView rootView = new SysPostView();
        rootView.setId(1L);
        rootView.setParentId(null);
        rootView.setSortOrder(1);
        rootView.setPostStatus(true);
        rootView.setDeleteStatus(false);
        SysPostView childView = new SysPostView();
        childView.setId(2L);
        childView.setParentId(1L);
        childView.setSortOrder(1);
        childView.setPostStatus(true);
        childView.setDeleteStatus(false);

        Mockito.when(r2dbcEntityTemplate.select(SysPostEntity.class).matching(any(Query.class)).all().collectList())
                .thenReturn(Mono.just(List.of(root, child)));
        Mockito.when(sysPostViewMapStruct.toViews(List.of(root, child))).thenReturn(List.of(rootView, childView));

        StepVerifier.create(service.postTree())
                .assertNext(tree -> {
                    org.junit.jupiter.api.Assertions.assertEquals(1, tree.size());
                    org.junit.jupiter.api.Assertions.assertEquals(1L, tree.get(0).getId());
                    org.junit.jupiter.api.Assertions.assertEquals(1, tree.get(0).getChildren().size());
                    org.junit.jupiter.api.Assertions.assertEquals(2L, tree.get(0).getChildren().get(0).getId());
                })
                .verifyComplete();
    }

    @Test
    void queryAllPostShouldReturnViews() {
        SysPostEntity entity = new SysPostEntity();
        entity.setId(1L);
        SysPostView view = new SysPostView();
        view.setId(1L);

        Mockito.when(r2dbcEntityTemplate.select(SysPostEntity.class).all().collectList()).thenReturn(Mono.just(List.of(entity)));
        Mockito.when(sysPostViewMapStruct.toViews(List.of(entity))).thenReturn(List.of(view));

        StepVerifier.create(service.queryAllPost())
                .assertNext(result -> {
                    org.junit.jupiter.api.Assertions.assertEquals(1, result.size());
                    org.junit.jupiter.api.Assertions.assertEquals(1L, result.get(0).getId());
                })
                .verifyComplete();
    }
}
