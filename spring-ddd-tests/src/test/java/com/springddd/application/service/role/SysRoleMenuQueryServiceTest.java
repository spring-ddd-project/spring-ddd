package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.application.service.role.dto.SysRoleMenuViewMapStruct;
import com.springddd.infrastructure.persistence.entity.SysRoleMenuEntity;
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
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SysRoleMenuQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysRoleMenuViewMapStruct sysRoleMenuViewMapStruct;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysRoleMenuEntity> reactiveSelect;

    private SysRoleMenuQueryService sysRoleMenuQueryService;

    @BeforeEach
    void setUp() {
        sysRoleMenuQueryService = new SysRoleMenuQueryService(r2dbcEntityTemplate, sysRoleMenuViewMapStruct);
    }

    @Test
    void queryLinkRoleAndMenus_shouldReturnViews_whenEntitiesExist() {
        Long roleId = 1L;
        SysRoleMenuEntity entity = new SysRoleMenuEntity();
        entity.setId(1L);
        entity.setRoleId(roleId);
        entity.setMenuId(2L);

        SysRoleMenuView view = new SysRoleMenuView();
        view.setId(1L);
        view.setRoleId(roleId);
        view.setMenuId(2L);

        when(r2dbcEntityTemplate.select(SysRoleMenuEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(sysRoleMenuViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysRoleMenuQueryService.queryLinkRoleAndMenus(roleId))
                .expectNext(Collections.singletonList(view))
                .verifyComplete();
    }

    @Test
    void queryLinkRoleAndMenusByMenuId_shouldReturnViews_whenEntitiesExist() {
        Long menuId = 2L;
        SysRoleMenuEntity entity = new SysRoleMenuEntity();
        entity.setId(1L);
        entity.setRoleId(1L);
        entity.setMenuId(menuId);

        SysRoleMenuView view = new SysRoleMenuView();
        view.setId(1L);
        view.setRoleId(1L);
        view.setMenuId(menuId);

        when(r2dbcEntityTemplate.select(SysRoleMenuEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(sysRoleMenuViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysRoleMenuQueryService.queryLinkRoleAndMenusByMenuId(menuId))
                .expectNext(Collections.singletonList(view))
                .verifyComplete();
    }
}
