package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRolePageQuery;
import com.springddd.application.service.role.dto.SysRoleView;
import com.springddd.application.service.role.dto.SysRoleViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SysRoleQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysRoleViewMapStruct sysRoleViewMapStruct;

    @Mock
    private SysRoleRepository sysRoleRepository;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysRoleEntity> reactiveSelect;

    private SysRoleQueryService sysRoleQueryService;

    @BeforeEach
    void setUp() {
        sysRoleQueryService = new SysRoleQueryService(
                r2dbcEntityTemplate,
                sysRoleViewMapStruct,
                sysRoleRepository
        );
    }

    @Test
    void index_shouldReturnPageResponse_whenEntitiesExist() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setRoleName("Admin");
        query.setRoleCode("ADMIN");

        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("Admin");
        entity.setRoleCode("ADMIN");
        entity.setDeleteStatus(false);

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleName("Admin");
        view.setRoleCode("ADMIN");

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysRoleEntity.class)))
                .thenReturn(Mono.just(1L));
        when(sysRoleViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysRoleQueryService.index(query))
                .assertNext(pageResponse -> {
                    assertNotNull(pageResponse);
                    assertNotNull(pageResponse.getItems());
                    assertEquals(1, pageResponse.getItems().size());
                })
                .verifyComplete();
    }

    @Test
    void index_shouldReturnPageResponse_withEmptyResult() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.empty());
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysRoleEntity.class)))
                .thenReturn(Mono.just(0L));
        when(sysRoleViewMapStruct.toViewList(any())).thenReturn(Collections.emptyList());

        StepVerifier.create(sysRoleQueryService.index(query))
                .assertNext(pageResponse -> {
                    assertNotNull(pageResponse);
                    assertTrue(pageResponse.getItems().isEmpty());
                    assertEquals(0, pageResponse.getTotal());
                })
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse_whenDeletedEntitiesExist() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("Deleted Role");
        entity.setRoleCode("DELETED");
        entity.setDeleteStatus(true);

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleName("Deleted Role");
        view.setRoleCode("DELETED");
        view.setDeleteStatus(true);

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysRoleEntity.class)))
                .thenReturn(Mono.just(1L));
        when(sysRoleViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysRoleQueryService.recycle(query))
                .assertNext(pageResponse -> {
                    assertNotNull(pageResponse);
                    assertNotNull(pageResponse.getItems());
                    assertEquals(1, pageResponse.getItems().size());
                })
                .verifyComplete();
    }

    @Test
    void getById_shouldReturnView_whenEntityExists() {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("Admin");
        entity.setRoleCode("ADMIN");
        entity.setDeleteStatus(false);

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleName("Admin");
        view.setRoleCode("ADMIN");

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.one()).thenReturn(Mono.just(entity));
        when(sysRoleViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(sysRoleQueryService.getById(1L))
                .assertNext(roleView -> {
                    assertNotNull(roleView);
                    assertEquals(1L, roleView.getId());
                    assertEquals("Admin", roleView.getRoleName());
                })
                .verifyComplete();
    }

    @Test
    void getById_shouldReturnEmpty_whenEntityNotFound() {
        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.one()).thenReturn(Mono.empty());

        StepVerifier.create(sysRoleQueryService.getById(999L))
                .verifyComplete();
    }

    @Test
    void getByCode_shouldReturnView_whenEntityExists() {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("Admin");
        entity.setRoleCode("ADMIN");
        entity.setDeleteStatus(false);

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleName("Admin");
        view.setRoleCode("ADMIN");

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.one()).thenReturn(Mono.just(entity));
        when(sysRoleViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(sysRoleQueryService.getByCode("ADMIN"))
                .assertNext(roleView -> {
                    assertNotNull(roleView);
                    assertEquals("ADMIN", roleView.getRoleCode());
                })
                .verifyComplete();
    }

    @Test
    void getByCode_shouldReturnEmpty_whenCodeNotFound() {
        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.one()).thenReturn(Mono.empty());

        StepVerifier.create(sysRoleQueryService.getByCode("NOT_EXIST"))
                .verifyComplete();
    }

    @Test
    void getAllRole_shouldReturnAllRoles() {
        SysRoleEntity entity1 = new SysRoleEntity();
        entity1.setId(1L);
        entity1.setRoleName("Admin");
        entity1.setRoleCode("ADMIN");
        entity1.setDeleteStatus(false);

        SysRoleEntity entity2 = new SysRoleEntity();
        entity2.setId(2L);
        entity2.setRoleName("User");
        entity2.setRoleCode("USER");
        entity2.setDeleteStatus(false);

        SysRoleView view1 = new SysRoleView();
        view1.setId(1L);
        view1.setRoleName("Admin");
        view1.setRoleCode("ADMIN");

        SysRoleView view2 = new SysRoleView();
        view2.setId(2L);
        view2.setRoleName("User");
        view2.setRoleCode("USER");

        when(sysRoleRepository.findAll()).thenReturn(Flux.just(entity1, entity2));
        when(sysRoleViewMapStruct.toViewList(any())).thenReturn(Arrays.asList(view1, view2));

        StepVerifier.create(sysRoleQueryService.getAllRole())
                .assertNext(roles -> {
                    assertNotNull(roles);
                    assertEquals(2, roles.size());
                })
                .verifyComplete();
    }

    @Test
    void index_shouldHandlePagination() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(2);
        query.setPageSize(5);

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.empty());
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysRoleEntity.class)))
                .thenReturn(Mono.just(10L));
        when(sysRoleViewMapStruct.toViewList(any())).thenReturn(Collections.emptyList());

        StepVerifier.create(sysRoleQueryService.index(query))
                .assertNext(pageResponse -> {
                    assertNotNull(pageResponse);
                    assertEquals(2, pageResponse.getPageNum());
                    assertEquals(5, pageResponse.getPageSize());
                    assertEquals(10L, pageResponse.getTotal());
                })
                .verifyComplete();
    }
}
