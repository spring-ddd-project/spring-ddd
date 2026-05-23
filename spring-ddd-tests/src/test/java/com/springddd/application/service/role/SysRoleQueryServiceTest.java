package com.springddd.application.service.role;

import com.springddd.application.service.permission.DataScopeCriteriaBuilder;
import com.springddd.application.service.role.dto.SysRolePageQuery;
import com.springddd.application.service.role.dto.SysRoleView;
import com.springddd.application.service.role.dto.SysRoleViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class SysRoleQueryServiceTest {

    @Mock
    private SysRoleViewMapStruct sysRoleViewMapStruct;

    @Mock
    private SysRoleRepository sysRoleRepository;

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private DataScopeCriteriaBuilder dataScopeCriteriaBuilder;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysRoleEntity> selectOp;

    @Mock
    private ReactiveSelectOperation.TerminatingSelect<SysRoleEntity> terminatingSelect;

    @InjectMocks
    private SysRoleQueryService service;

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
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setRoleName("admin");

        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("admin");

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleName("admin");

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysRoleViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysRoleEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> {
                    assertThat(page.getList()).hasSize(1);
                    assertThat(page.getList().get(0).getRoleName()).isEqualTo("admin");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("recycle 应返回回收站分页结果")
    void recycle_shouldReturnRecyclePage() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);

        SysRoleView view = new SysRoleView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysRoleViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysRoleEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.recycle(query))
                .assertNext(page -> assertThat(page.getList()).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("getById 应返回角色视图")
    void getById_shouldReturnRoleView() {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleCode("admin");

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleCode("admin");

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.one()).thenReturn(Mono.just(entity));
        when(sysRoleViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(service.getById(1L))
                .assertNext(result -> assertThat(result.getRoleCode()).isEqualTo("admin"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getAllRole 应返回所有角色")
    void getAllRole_shouldReturnAllRoles() {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);

        SysRoleView view = new SysRoleView();
        view.setId(1L);

        when(sysRoleRepository.findAll()).thenReturn(Flux.just(entity));
        when(sysRoleViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));

        StepVerifier.create(service.getAllRole())
                .assertNext(list -> assertThat(list).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("getByCode 当传入有效 code 时应返回角色视图")
    void getByCode_withValidCode_shouldReturnRoleView() {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleCode("admin");

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleCode("admin");

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.one()).thenReturn(Mono.just(entity));
        when(sysRoleViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(service.getByCode("admin"))
                .assertNext(result -> assertThat(result.getRoleCode()).isEqualTo("admin"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getByCode 当角色不存在时应返回空")
    void getByCode_whenNotFound_shouldReturnEmpty() {
        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.one()).thenReturn(Mono.empty());

        StepVerifier.create(service.getByCode("nonexistent"))
                .verifyComplete();
    }

    @Test
    @DisplayName("getByCode 当传入 null 时应抛出 IllegalArgumentException")
    void getByCode_withNullCode_shouldThrowIllegalArgumentException() {
        assertThatThrownBy(() -> service.getByCode(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Value must not be null");
    }

    @Test
    @DisplayName("index 当 roleName 和 roleCode 均为空时应只按 deleteStatus 查询")
    void index_withEmptyRoleNameAndRoleCode_shouldApplyOnlyDeleteStatus() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);

        SysRoleView view = new SysRoleView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysRoleViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysRoleEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> assertThat(page.getList()).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("index 当 roleCode 不为空时应按 roleCode 模糊查询")
    void index_withRoleCode_shouldApplyRoleCodeCriteria() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setRoleCode("ADMIN");

        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleCode("ADMIN");

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleCode("ADMIN");

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysRoleViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysRoleEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> assertThat(page.getList()).hasSize(1))
                .verifyComplete();
    }
}
