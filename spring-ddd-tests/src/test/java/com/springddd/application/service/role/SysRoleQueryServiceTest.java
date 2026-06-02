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

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        sysRoleQueryService = new SysRoleQueryService(r2dbcEntityTemplate, sysRoleViewMapStruct, sysRoleRepository);
    }

    @Test
    void index_shouldReturnPageResponse_whenEntitiesExist() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setRoleName("Admin");
        query.setRoleCode("admin");

        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("Admin");
        entity.setRoleCode("admin");
        entity.setDeleteStatus(false);

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleName("Admin");
        view.setRoleCode("admin");

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysRoleEntity.class))).thenReturn(Mono.just(1L));
        when(sysRoleViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysRoleQueryService.index(query))
                .assertNext(pageResponse -> {
                    assertNotNull(pageResponse);
                    assertNotNull(pageResponse.getItems());
                })
                .verifyComplete();
    }

    @Test
    void index_shouldReturnPageResponse_withoutSearchCriteria() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setDeleteStatus(false);

        SysRoleView view = new SysRoleView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysRoleEntity.class))).thenReturn(Mono.just(1L));
        when(sysRoleViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysRoleQueryService.index(query))
                .assertNext(pageResponse -> assertNotNull(pageResponse))
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse_whenDeletedEntitiesExist() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setDeleteStatus(true);

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setDeleteStatus(true);

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysRoleEntity.class))).thenReturn(Mono.just(1L));
        when(sysRoleViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysRoleQueryService.recycle(query))
                .assertNext(pageResponse -> assertNotNull(pageResponse))
                .verifyComplete();
    }

    @Test
    void getById_shouldReturnView_whenEntityExists() {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("Admin");
        entity.setDeleteStatus(false);

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleName("Admin");

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.one()).thenReturn(Mono.just(entity));
        when(sysRoleViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(sysRoleQueryService.getById(1L))
                .expectNext(view)
                .verifyComplete();
    }

    @Test
    void getByCode_shouldReturnView_whenEntityExists() {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleCode("admin");
        entity.setDeleteStatus(false);

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleCode("admin");

        when(r2dbcEntityTemplate.select(SysRoleEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.one()).thenReturn(Mono.just(entity));
        when(sysRoleViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(sysRoleQueryService.getByCode("admin"))
                .expectNext(view)
                .verifyComplete();
    }

    @Test
    void getAllRole_shouldReturnAllViews() {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("Admin");

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleName("Admin");

        when(sysRoleRepository.findAll()).thenReturn(Flux.just(entity));
        when(sysRoleViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysRoleQueryService.getAllRole())
                .expectNext(Collections.singletonList(view))
                .verifyComplete();
    }
}
