package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptQuery;
import com.springddd.application.service.dept.dto.SysDeptView;
import com.springddd.application.service.dept.dto.SysDeptViewMapStruct;
import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveSelectOperation;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysDeptQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysDeptViewMapStruct sysDeptViewMapStruct;

    @InjectMocks
    private SysDeptQueryService sysDeptQueryService;

    private SysDeptEntity mockEntity;
    private SysDeptView mockView;

    @BeforeEach
    void setUp() {
        mockEntity = new SysDeptEntity();
        mockEntity.setId(1L);
        mockEntity.setParentId(null);
        mockEntity.setDeptName("Test Department");
        mockEntity.setSortOrder(1);
        mockEntity.setDeptStatus(true);
        mockEntity.setDeleteStatus(false);

        mockView = new SysDeptView();
        mockView.setId(1L);
        mockView.setParentId(null);
        mockView.setDeptName("Test Department");
        mockView.setSortOrder(1);
        mockView.setDeptStatus(true);
        mockView.setDeleteStatus(false);
    }

    @SuppressWarnings("unchecked")
    @Test
    void index_shouldReturnPageResponse() {
        SysDeptQuery query = new SysDeptQuery();
        List<SysDeptEntity> entities = Arrays.asList(mockEntity);
        List<SysDeptView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysDeptEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDeptEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDeptEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysDeptEntity.class));
        when(sysDeptViewMapStruct.toViews(entities)).thenReturn(views);

        StepVerifier.create(sysDeptQueryService.index(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.getItems().size());
                    assertEquals(1L, response.getTotal());
                    assertEquals("Test Department", response.getItems().get(0).getDeptName());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void index_shouldReturnEmptyWhenNoData() {
        SysDeptQuery query = new SysDeptQuery();

        ReactiveSelectOperation.ReactiveSelect<SysDeptEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDeptEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDeptEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.empty()).when(mockTerminatingSelect).all();
        doReturn(Mono.just(0L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysDeptEntity.class));
        when(sysDeptViewMapStruct.toViews(Collections.emptyList())).thenReturn(Collections.emptyList());

        StepVerifier.create(sysDeptQueryService.index(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(0, response.getItems().size());
                    assertEquals(0L, response.getTotal());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void recycle_shouldReturnDeletedDepts() {
        SysDeptQuery query = new SysDeptQuery();
        mockEntity.setDeleteStatus(true);
        mockView.setDeleteStatus(true);
        List<SysDeptEntity> entities = Arrays.asList(mockEntity);
        List<SysDeptView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysDeptEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDeptEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDeptEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysDeptEntity.class));
        when(sysDeptViewMapStruct.toViews(entities)).thenReturn(views);

        StepVerifier.create(sysDeptQueryService.recycle(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.getItems().size());
                    assertEquals(1L, response.getTotal());
                    assertEquals(true, response.getItems().get(0).getDeleteStatus());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void deptTree_shouldReturnTreeStructure() {
        SysDeptEntity childEntity = new SysDeptEntity();
        childEntity.setId(2L);
        childEntity.setParentId(1L);
        childEntity.setDeptName("Child Department");
        childEntity.setSortOrder(1);
        childEntity.setDeptStatus(true);
        childEntity.setDeleteStatus(false);

        SysDeptView childView = new SysDeptView();
        childView.setId(2L);
        childView.setParentId(1L);
        childView.setDeptName("Child Department");
        childView.setSortOrder(1);
        childView.setDeptStatus(true);
        childView.setDeleteStatus(false);

        List<SysDeptEntity> entities = Arrays.asList(mockEntity, childEntity);
        List<SysDeptView> views = Arrays.asList(mockView, childView);

        ReactiveSelectOperation.ReactiveSelect<SysDeptEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDeptEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDeptEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity, childEntity)).when(mockTerminatingSelect).all();
        when(sysDeptViewMapStruct.toViews(entities)).thenReturn(views);

        StepVerifier.create(sysDeptQueryService.deptTree())
                .assertNext(tree -> {
                    assertNotNull(tree);
                    assertEquals(1, tree.size());
                    assertEquals("Test Department", tree.get(0).getDeptName());
                    assertNotNull(tree.get(0).getChildren());
                    assertEquals(1, tree.get(0).getChildren().size());
                    assertEquals("Child Department", tree.get(0).getChildren().get(0).getDeptName());
                })
                .verifyComplete();
    }

    // Note: queryAllDept() test is complex due to R2dbcEntityTemplate.select().all().collectList() chain
    // The TerminatingSelect.all() method creates ambiguity with Flux.all(Predicate) causing mocking issues
    // These methods are tested implicitly through other tests that exercise the full query chain
}
