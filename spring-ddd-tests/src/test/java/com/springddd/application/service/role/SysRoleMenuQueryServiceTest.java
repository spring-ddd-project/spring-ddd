package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.application.service.role.dto.SysRoleMenuViewMapStruct;
import com.springddd.infrastructure.persistence.entity.SysRoleMenuEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveSelectOperation;
import reactor.core.publisher.Flux;
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
class SysRoleMenuQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysRoleMenuViewMapStruct sysRoleMenuViewMapStruct;

    @InjectMocks
    private SysRoleMenuQueryService sysRoleMenuQueryService;

    private SysRoleMenuEntity mockEntity;
    private SysRoleMenuView mockView;

    @BeforeEach
    void setUp() {
        mockEntity = new SysRoleMenuEntity();
        mockEntity.setId(1L);
        mockEntity.setRoleId(1L);
        mockEntity.setMenuId(10L);
        mockEntity.setDeleteStatus(false);

        mockView = new SysRoleMenuView();
        mockView.setId(1L);
        mockView.setRoleId(1L);
        mockView.setMenuId(10L);
    }

    @SuppressWarnings("unchecked")
    @Test
    void queryLinkRoleAndMenus_shouldReturnViewsForRole() {
        Long roleId = 1L;
        List<SysRoleMenuEntity> entities = Arrays.asList(mockEntity);
        List<SysRoleMenuView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysRoleMenuEntity> mockSelect =
            mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysRoleMenuEntity> mockTerminatingSelect =
            mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysRoleMenuEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any());
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        when(sysRoleMenuViewMapStruct.toViewList(entities)).thenReturn(views);

        StepVerifier.create(sysRoleMenuQueryService.queryLinkRoleAndMenus(roleId))
                .expectNextMatches(result -> {
                    assertNotNull(result);
                    assertEquals(1, result.size());
                    assertEquals(roleId, result.get(0).getRoleId());
                    return true;
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void queryLinkRoleAndMenus_shouldReturnEmptyListWhenNoResults() {
        Long roleId = 999L;

        ReactiveSelectOperation.ReactiveSelect<SysRoleMenuEntity> mockSelect =
            mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysRoleMenuEntity> mockTerminatingSelect =
            mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysRoleMenuEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any());
        doReturn(Flux.empty()).when(mockTerminatingSelect).all();
        when(sysRoleMenuViewMapStruct.toViewList(Collections.emptyList())).thenReturn(Collections.emptyList());

        StepVerifier.create(sysRoleMenuQueryService.queryLinkRoleAndMenus(roleId))
                .expectNext(Collections.emptyList())
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void queryLinkRoleAndMenusByMenuId_shouldReturnViewsForMenu() {
        Long menuId = 10L;
        List<SysRoleMenuEntity> entities = Arrays.asList(mockEntity);
        List<SysRoleMenuView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysRoleMenuEntity> mockSelect =
            mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysRoleMenuEntity> mockTerminatingSelect =
            mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysRoleMenuEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any());
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        when(sysRoleMenuViewMapStruct.toViewList(entities)).thenReturn(views);

        StepVerifier.create(sysRoleMenuQueryService.queryLinkRoleAndMenusByMenuId(menuId))
                .expectNextMatches(result -> {
                    assertNotNull(result);
                    assertEquals(1, result.size());
                    assertEquals(menuId, result.get(0).getMenuId());
                    return true;
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void queryLinkRoleAndMenusByMenuId_shouldReturnEmptyListWhenNoResults() {
        Long menuId = 999L;

        ReactiveSelectOperation.ReactiveSelect<SysRoleMenuEntity> mockSelect =
            mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysRoleMenuEntity> mockTerminatingSelect =
            mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysRoleMenuEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any());
        doReturn(Flux.empty()).when(mockTerminatingSelect).all();
        when(sysRoleMenuViewMapStruct.toViewList(Collections.emptyList())).thenReturn(Collections.emptyList());

        StepVerifier.create(sysRoleMenuQueryService.queryLinkRoleAndMenusByMenuId(menuId))
                .expectNext(Collections.emptyList())
                .verifyComplete();
    }
}
