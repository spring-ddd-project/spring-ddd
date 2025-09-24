package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRolePageQuery;
import com.springddd.application.service.role.dto.SysRoleQuery;
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
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysRoleQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysRoleViewMapStruct sysRoleViewMapStruct;

    @Mock
    private SysRoleRepository sysRoleRepository;

    private SysRoleQueryService sysRoleQueryService;

    @BeforeEach
    void setUp() {
        sysRoleQueryService = new SysRoleQueryService(r2dbcEntityTemplate, sysRoleViewMapStruct, sysRoleRepository);
    }

    @Test
    void index_shouldReturnPageResponse_whenDataExists() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDeleteStatus(false);

        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("Test Role");

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleName("Test Role");

        List<SysRoleView> views = Collections.singletonList(view);

        when(r2dbcEntityTemplate.select(SysRoleEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysRoleEntity.class)))
                .thenReturn(Mono.just(1L));
        when(sysRoleViewMapStruct.toViewList(any())).thenReturn(views);

        Mono<PageResponse<SysRoleView>> result = sysRoleQueryService.index(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 1 && response.getItems().size() == 1)
                .verifyComplete();
    }

    @Test
    void index_shouldReturnPageResponse_withRoleNameFilter() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDeleteStatus(false);
        query.setRoleName("Test");

        when(r2dbcEntityTemplate.select(SysRoleEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.empty());
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysRoleEntity.class)))
                .thenReturn(Mono.just(0L));
        when(sysRoleViewMapStruct.toViewList(any())).thenReturn(Collections.emptyList());

        Mono<PageResponse<SysRoleView>> result = sysRoleQueryService.index(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 0)
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse_withDeletedItems() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDeleteStatus(true);

        when(r2dbcEntityTemplate.select(SysRoleEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.empty());
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysRoleEntity.class)))
                .thenReturn(Mono.just(0L));
        when(sysRoleViewMapStruct.toViewList(any())).thenReturn(Collections.emptyList());

        Mono<PageResponse<SysRoleView>> result = sysRoleQueryService.recycle(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 0)
                .verifyComplete();
    }

    @Test
    void getById_shouldReturnView_whenRoleExists() {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("Test Role");
        entity.setDeleteStatus(false);

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleName("Test Role");

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysRoleEntity.class)))
                .thenReturn(Mono.just(entity));
        when(sysRoleViewMapStruct.toView(entity)).thenReturn(view);

        Mono<SysRoleView> result = sysRoleQueryService.getById(1L);

        StepVerifier.create(result)
                .expectNextMatches(role -> role.getId() == 1L)
                .verifyComplete();
    }

    @Test
    void getByCode_shouldReturnView_whenRoleExists() {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleCode("TEST_ROLE");
        entity.setDeleteStatus(false);

        SysRoleView view = new SysRoleView();
        view.setId(1L);
        view.setRoleCode("TEST_ROLE");

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysRoleEntity.class)))
                .thenReturn(Mono.just(entity));
        when(sysRoleViewMapStruct.toView(entity)).thenReturn(view);

        Mono<SysRoleView> result = sysRoleQueryService.getByCode("TEST_ROLE");

        StepVerifier.create(result)
                .expectNextMatches(role -> "TEST_ROLE".equals(role.getRoleCode()))
                .verifyComplete();
    }

    @Test
    void getAllRole_shouldReturnAllRoles() {
        SysRoleEntity entity1 = new SysRoleEntity();
        entity1.setId(1L);
        entity1.setRoleName("Role 1");

        SysRoleEntity entity2 = new SysRoleEntity();
        entity2.setId(2L);
        entity2.setRoleName("Role 2");

        List<SysRoleView> views = Arrays.asList(
                new SysRoleView() {{ setId(1L); setRoleName("Role 1"); }},
                new SysRoleView() {{ setId(2L); setRoleName("Role 2"); }}
        );

        when(sysRoleRepository.findAll()).thenReturn(Flux.just(entity1, entity2));
        when(sysRoleViewMapStruct.toViewList(any())).thenReturn(views);

        Mono<List<SysRoleView>> result = sysRoleQueryService.getAllRole();

        StepVerifier.create(result)
                .expectNextMatches(roleList -> roleList.size() == 2)
                .verifyComplete();
    }
}
