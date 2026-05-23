package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptQuery;
import com.springddd.application.service.dept.dto.SysDeptView;
import com.springddd.application.service.dept.dto.SysDeptViewMapStruct;
import com.springddd.application.service.permission.DataScopeCriteriaBuilder;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveSelectOperation;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class SysDeptQueryServiceTest {

    @Mock
    private SysDeptViewMapStruct sysDeptViewMapStruct;

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private DataScopeCriteriaBuilder dataScopeCriteriaBuilder;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysDeptEntity> selectOp;

    @Mock
    private ReactiveSelectOperation.TerminatingSelect<SysDeptEntity> terminatingSelect;

    @InjectMocks
    private SysDeptQueryService service;

    @BeforeEach
    void setUp() throws Exception {
        Field qfField = service.getClass().getSuperclass().getDeclaredField("queryFactory");
        qfField.setAccessible(true);
        qfField.set(service, queryFactory);

        Field dscbField = service.getClass().getSuperclass().getDeclaredField("dataScopeCriteriaBuilder");
        dscbField.setAccessible(true);
        dscbField.set(service, dataScopeCriteriaBuilder);

        when(queryFactory.getR2dbcEntityTemplate()).thenReturn(r2dbcEntityTemplate);
        when(dataScopeCriteriaBuilder.apply(any(Criteria.class), any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
    }

    @Test
    @DisplayName("index 应返回分页结果")
    void index_shouldReturnPage() {
        SysDeptQuery query = new SysDeptQuery();
        query.setDeptName("test");

        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);
        entity.setDeptName("test");

        SysDeptView view = new SysDeptView();
        view.setId(1L);
        view.setDeptName("test");

        when(r2dbcEntityTemplate.select(SysDeptEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysDeptViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDeptEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> {
                    assertThat(page.getList()).hasSize(1);
                    assertThat(page.getList().get(0).getDeptName()).isEqualTo("test");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("recycle 应返回回收站分页结果")
    void recycle_shouldReturnRecyclePage() {
        SysDeptQuery query = new SysDeptQuery();

        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);

        SysDeptView view = new SysDeptView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(SysDeptEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysDeptViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysDeptEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.recycle(query))
                .assertNext(page -> assertThat(page.getList()).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("deptTree 应返回部门树")
    void deptTree_shouldReturnTree() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);
        entity.setParentId(null);
        entity.setSortOrder(1);
        entity.setDeptStatus(true);
        entity.setDeleteStatus(false);

        SysDeptView view = new SysDeptView();
        view.setId(1L);
        view.setParentId(null);
        view.setSortOrder(1);
        view.setDeptStatus(true);
        view.setDeleteStatus(false);
        view.setChildren(List.of());

        when(r2dbcEntityTemplate.select(SysDeptEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysDeptViewMapStruct.toViews(anyList())).thenReturn(List.of(view));

        StepVerifier.create(service.deptTree())
                .assertNext(list -> assertThat(list).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryAllDept 应返回所有部门")
    void queryAllDept_shouldReturnAllDepts() {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setId(1L);

        SysDeptView view = new SysDeptView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(SysDeptEntity.class)).thenReturn(selectOp);
        when(selectOp.all()).thenReturn(Flux.just(entity));
        when(sysDeptViewMapStruct.toViews(anyList())).thenReturn(List.of(view));

        StepVerifier.create(service.queryAllDept())
                .assertNext(list -> assertThat(list).hasSize(1))
                .verifyComplete();
    }
}
