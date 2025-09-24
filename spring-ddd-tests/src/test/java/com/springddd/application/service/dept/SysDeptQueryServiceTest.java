package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptQuery;
import com.springddd.application.service.dept.dto.SysDeptView;
import com.springddd.application.service.dept.dto.SysDeptViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysDeptQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysDeptViewMapStruct sysDeptViewMapStruct;

    private SysDeptQueryService sysDeptQueryService;

    @BeforeEach
    void setUp() {
        sysDeptQueryService = new SysDeptQueryService(r2dbcEntityTemplate, sysDeptViewMapStruct);
    }

    @Test
    void index_shouldReturnPageResponse_whenDataExists() {
        SysDeptQuery query = new SysDeptQuery();
        query.setDeleteStatus(false);

        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);
        entity.setDeptName("Test Dept");

        SysDeptView view = new SysDeptView();
        view.setId(1L);
        view.setDeptName("Test Dept");

        List<SysDeptView> views = Collections.singletonList(view);

        when(r2dbcEntityTemplate.select(SysDeptEntity.class).matching(any(Query.class)).all())
                .thenReturn(reactor.core.publisher.Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), any()))
                .thenReturn(Mono.just(1L));
        when(sysDeptViewMapStruct.toViews(any())).thenReturn(views);

        Mono<PageResponse<SysDeptView>> result = sysDeptQueryService.index(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 1 && response.getList().size() == 1)
                .verifyComplete();
    }

    @Test
    void index_shouldReturnEmptyPage_whenNoData() {
        SysDeptQuery query = new SysDeptQuery();
        query.setDeleteStatus(false);

        when(r2dbcEntityTemplate.select(SysDeptEntity.class).matching(any(Query.class)).all())
                .thenReturn(reactor.core.publisher.Flux.empty());
        when(r2dbcEntityTemplate.count(any(Query.class), any()))
                .thenReturn(Mono.just(0L));
        when(sysDeptViewMapStruct.toViews(any())).thenReturn(Collections.emptyList());

        Mono<PageResponse<SysDeptView>> result = sysDeptQueryService.index(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 0 && response.getList().isEmpty())
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse_withDeletedItems() {
        SysDeptQuery query = new SysDeptQuery();
        query.setDeleteStatus(true);

        when(r2dbcEntityTemplate.select(SysDeptEntity.class).matching(any(Query.class)).all())
                .thenReturn(reactor.core.publisher.Flux.empty());
        when(r2dbcEntityTemplate.count(any(Query.class), any()))
                .thenReturn(Mono.just(0L));
        when(sysDeptViewMapStruct.toViews(any())).thenReturn(Collections.emptyList());

        Mono<PageResponse<SysDeptView>> result = sysDeptQueryService.recycle(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 0)
                .verifyComplete();
    }

    @Test
    void deptTree_shouldReturnTreeStructure() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);
        entity.setDeptName("Root Dept");
        entity.setParentId(0L);
        entity.setSortOrder(1);
        entity.setDeptStatus(true);
        entity.setDeleteStatus(false);

        SysDeptView view = new SysDeptView();
        view.setId(1L);
        view.setDeptName("Root Dept");
        view.setParentId(null);
        view.setSortOrder(1);
        view.setDeptStatus(true);
        view.setDeleteStatus(false);

        List<SysDeptView> views = Collections.singletonList(view);

        when(r2dbcEntityTemplate.select(SysDeptEntity.class).matching(any(Query.class)).all())
                .thenReturn(reactor.core.publisher.Flux.just(entity));
        when(sysDeptViewMapStruct.toViews(any())).thenReturn(views);

        Mono<List<SysDeptView>> result = sysDeptQueryService.deptTree();

        StepVerifier.create(result)
                .expectNextMatches(tree -> !tree.isEmpty())
                .verifyComplete();
    }

    @Test
    void queryAllDept_shouldReturnAllDepartments() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);
        entity.setDeptName("Dept 1");

        SysDeptView view = new SysDeptView();
        view.setId(1L);
        view.setDeptName("Dept 1");

        List<SysDeptView> views = Collections.singletonList(view);

        when(r2dbcEntityTemplate.select(SysDeptEntity.class).all())
                .thenReturn(reactor.core.publisher.Flux.just(entity));
        when(sysDeptViewMapStruct.toViews(any())).thenReturn(views);

        Mono<List<SysDeptView>> result = sysDeptQueryService.queryAllDept();

        StepVerifier.create(result)
                .expectNextMatches(deptList -> deptList.size() == 1)
                .verifyComplete();
    }
}
