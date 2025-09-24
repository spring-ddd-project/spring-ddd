package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuQuery;
import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.application.service.role.dto.SysRoleMenuViewMapStruct;
import com.springddd.infrastructure.persistence.entity.SysRoleMenuEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysRoleMenuQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysRoleMenuViewMapStruct sysRoleMenuViewMapStruct;

    private SysRoleMenuQueryService sysRoleMenuQueryService;

    @BeforeEach
    void setUp() {
        sysRoleMenuQueryService = new SysRoleMenuQueryService(r2dbcEntityTemplate, sysRoleMenuViewMapStruct);
    }

    @Test
    void queryLinkRoleAndMenus_shouldReturnMenuViews_whenRoleHasMenus() {
        Long roleId = 1L;

        SysRoleMenuEntity entity1 = new SysRoleMenuEntity();
        entity1.setId(1L);
        entity1.setRoleId(roleId);
        entity1.setMenuId(10L);

        SysRoleMenuEntity entity2 = new SysRoleMenuEntity();
        entity2.setId(2L);
        entity2.setRoleId(roleId);
        entity2.setMenuId(20L);

        List<SysRoleMenuView> views = Arrays.asList(
                new SysRoleMenuView() {{ setId(1L); setRoleId(roleId); setMenuId(10L); }},
                new SysRoleMenuView() {{ setId(2L); setRoleId(roleId); setMenuId(20L); }}
        );

        when(r2dbcEntityTemplate.select(SysRoleMenuEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.just(entity1, entity2));
        when(sysRoleMenuViewMapStruct.toViewList(any())).thenReturn(views);

        reactor.core.publisher.Mono<List<SysRoleMenuView>> result = sysRoleMenuQueryService.queryLinkRoleAndMenus(roleId);

        StepVerifier.create(result)
                .expectNextMatches(menuList -> menuList.size() == 2)
                .verifyComplete();
    }

    @Test
    void queryLinkRoleAndMenus_shouldReturnEmptyList_whenRoleHasNoMenus() {
        Long roleId = 1L;

        when(r2dbcEntityTemplate.select(SysRoleMenuEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.empty());
        when(sysRoleMenuViewMapStruct.toViewList(any())).thenReturn(Collections.emptyList());

        reactor.core.publisher.Mono<List<SysRoleMenuView>> result = sysRoleMenuQueryService.queryLinkRoleAndMenus(roleId);

        StepVerifier.create(result)
                .expectNextMatches(menuList -> menuList.isEmpty())
                .verifyComplete();
    }

    @Test
    void queryLinkRoleAndMenusByMenuId_shouldReturnRoleMenuViews_whenMenuHasRoles() {
        Long menuId = 10L;

        SysRoleMenuEntity entity1 = new SysRoleMenuEntity();
        entity1.setId(1L);
        entity1.setRoleId(1L);
        entity1.setMenuId(menuId);

        List<SysRoleMenuView> views = Collections.singletonList(
                new SysRoleMenuView() {{ setId(1L); setRoleId(1L); setMenuId(menuId); }}
        );

        when(r2dbcEntityTemplate.select(SysRoleMenuEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.just(entity1));
        when(sysRoleMenuViewMapStruct.toViewList(any())).thenReturn(views);

        reactor.core.publisher.Mono<List<SysRoleMenuView>> result = sysRoleMenuQueryService.queryLinkRoleAndMenusByMenuId(menuId);

        StepVerifier.create(result)
                .expectNextMatches(menuList -> menuList.size() == 1)
                .verifyComplete();
    }

    @Test
    void queryLinkRoleAndMenusByMenuId_shouldReturnEmptyList_whenMenuHasNoRoles() {
        Long menuId = 10L;

        when(r2dbcEntityTemplate.select(SysRoleMenuEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.empty());
        when(sysRoleMenuViewMapStruct.toViewList(any())).thenReturn(Collections.emptyList());

        reactor.core.publisher.Mono<List<SysRoleMenuView>> result = sysRoleMenuQueryService.queryLinkRoleAndMenusByMenuId(menuId);

        StepVerifier.create(result)
                .expectNextMatches(menuList -> menuList.isEmpty())
                .verifyComplete();
    }
}
