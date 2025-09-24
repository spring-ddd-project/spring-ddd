package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuQuery;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.application.service.menu.dto.SysMenuViewMapStruct;
import com.springddd.application.service.role.SysRoleQueryService;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
class SysMenuQueryServiceTest {

    @Mock
    private SysMenuRepository sysMenuRepository;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysMenuViewMapStruct sysMenuViewMapStruct;

    @Mock
    private ReactiveRedisCacheHelper reactiveRedisCacheHelper;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private SysRoleQueryService sysRoleQueryService;

    private SysMenuQueryService sysMenuQueryService;

    @BeforeEach
    void setUp() {
        sysMenuQueryService = new SysMenuQueryService(
                sysMenuRepository,
                r2dbcEntityTemplate,
                sysMenuViewMapStruct,
                reactiveRedisCacheHelper,
                objectMapper,
                sysRoleQueryService
        );
    }

    @Test
    void index_shouldReturnPageResponse_whenDataExists() {
        SysMenuQuery query = new SysMenuQuery();
        query.setDeleteStatus(false);

        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setName("Test Menu");

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setName("Test Menu");

        List<SysMenuView> views = Collections.singletonList(view);

        when(r2dbcEntityTemplate.select(SysMenuEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysMenuEntity.class)))
                .thenReturn(Mono.just(1L));
        when(sysMenuViewMapStruct.toViewList(any())).thenReturn(views);

        Mono<PageResponse<SysMenuView>> result = sysMenuQueryService.index(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 1 && response.getItems().size() == 1)
                .verifyComplete();
    }

    @Test
    void index_shouldReturnEmptyPage_whenNoData() {
        SysMenuQuery query = new SysMenuQuery();
        query.setDeleteStatus(false);

        when(r2dbcEntityTemplate.select(SysMenuEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.empty());
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysMenuEntity.class)))
                .thenReturn(Mono.just(0L));
        when(sysMenuViewMapStruct.toViewList(any())).thenReturn(Collections.emptyList());

        Mono<PageResponse<SysMenuView>> result = sysMenuQueryService.index(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 0 && response.getItems().isEmpty())
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse_withDeletedItems() {
        SysMenuQuery query = new SysMenuQuery();
        query.setDeleteStatus(true);

        when(r2dbcEntityTemplate.select(SysMenuEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.empty());
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysMenuEntity.class)))
                .thenReturn(Mono.just(0L));
        when(sysMenuViewMapStruct.toViewList(any())).thenReturn(Collections.emptyList());

        Mono<PageResponse<SysMenuView>> result = sysMenuQueryService.recycle(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 0)
                .verifyComplete();
    }

    @Test
    void queryByMenuId_shouldReturnView_whenMenuExists() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setName("Test Menu");
        entity.setDeleteStatus(false);

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setName("Test Menu");

        when(sysMenuRepository.findById(1L)).thenReturn(Mono.just(entity));
        when(sysMenuViewMapStruct.toView(entity)).thenReturn(view);

        Mono<SysMenuView> result = sysMenuQueryService.queryByMenuId(1L);

        StepVerifier.create(result)
                .expectNextMatches(menu -> menu.getId() == 1L)
                .verifyComplete();
    }

    @Test
    void queryByMenuId_shouldReturnEmpty_whenMenuDeleted() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setDeleteStatus(true);

        when(sysMenuRepository.findById(1L)).thenReturn(Mono.just(entity));

        Mono<SysMenuView> result = sysMenuQueryService.queryByMenuId(1L);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void queryByApi_shouldReturnView_whenApiExists() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setApi("/api/test");

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setApi("/api/test");

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysMenuEntity.class)))
                .thenReturn(Mono.just(entity));
        when(sysMenuViewMapStruct.toView(entity)).thenReturn(view);

        Mono<SysMenuView> result = sysMenuQueryService.queryByApi("/api/test");

        StepVerifier.create(result)
                .expectNextMatches(menu -> "/api/test".equals(menu.getApi()))
                .verifyComplete();
    }

    @Test
    void queryAllMenu_shouldReturnAllMenus() {
        SysMenuEntity entity1 = new SysMenuEntity();
        entity1.setId(1L);
        entity1.setName("Menu 1");

        SysMenuEntity entity2 = new SysMenuEntity();
        entity2.setId(2L);
        entity2.setName("Menu 2");

        List<SysMenuView> views = Arrays.asList(
                new SysMenuView() {{ setId(1L); setName("Menu 1"); }},
                new SysMenuView() {{ setId(2L); setName("Menu 2"); }}
        );

        when(sysMenuRepository.findAll()).thenReturn(Flux.just(entity1, entity2));
        when(sysMenuViewMapStruct.toViewList(any())).thenReturn(views);

        Mono<List<SysMenuView>> result = sysMenuQueryService.queryAllMenu();

        StepVerifier.create(result)
                .expectNextMatches(menuList -> menuList.size() == 2)
                .verifyComplete();
    }
}
