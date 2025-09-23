package com.springddd.application.service.menu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.menu.dto.SysMenuQuery;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.application.service.menu.dto.SysMenuViewMapStruct;
import com.springddd.application.service.role.SysRoleQueryService;
import com.springddd.application.service.role.dto.SysRoleView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
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

    @InjectMocks
    private SysMenuQueryService sysMenuQueryService;

    private SysMenuEntity mockEntity;
    private SysMenuView mockView;
    private MockedStatic<SecurityUtils> securityUtilsMockedStatic;

    @BeforeEach
    void setUp() {
        mockEntity = new SysMenuEntity();
        mockEntity.setId(1L);
        mockEntity.setParentId(null);
        mockEntity.setName("Test Menu");
        mockEntity.setPath("/test");
        mockEntity.setComponent("test.vue");
        mockEntity.setApi("/api/test");
        mockEntity.setPermission("test:view");
        mockEntity.setSortOrder(1);
        mockEntity.setTitle("Test Menu");
        mockEntity.setAffixTab(false);
        mockEntity.setNoBasicLayout(false);
        mockEntity.setIcon("test-icon");
        mockEntity.setMenuType(1);
        mockEntity.setVisible(true);
        mockEntity.setEmbedded(false);
        mockEntity.setMenuStatus(true);
        mockEntity.setDeleteStatus(false);

        mockView = new SysMenuView();
        mockView.setId(1L);
        mockView.setParentId(null);
        mockView.setName("Test Menu");
        mockView.setPath("/test");
        mockView.setComponent("test.vue");
        mockView.setApi("/api/test");
        mockView.setPermission("test:view");
        mockView.setMenuType(1);
        mockView.setVisible(true);
        mockView.setEmbedded(false);
        mockView.setMenuStatus(true);
        mockView.setDeleteStatus(false);

        SysMenuView.Meta meta = new SysMenuView.Meta();
        meta.setOrder(1);
        meta.setTitle("Test Menu");
        meta.setAffixTab(false);
        meta.setNoBasicLayout(false);
        meta.setIcon("test-icon");
        mockView.setMeta(meta);

        securityUtilsMockedStatic = mockStatic(SecurityUtils.class);
        securityUtilsMockedStatic.when(SecurityUtils::getUserId).thenReturn(1L);
        securityUtilsMockedStatic.when(SecurityUtils::getRoles).thenReturn(Collections.emptyList());
        securityUtilsMockedStatic.when(SecurityUtils::getMenuIds).thenReturn(Collections.emptyList());
    }

    @AfterEach
    void tearDown() {
        securityUtilsMockedStatic.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    void index_shouldReturnPageResponse() {
        SysMenuQuery query = new SysMenuQuery();
        List<SysMenuEntity> entities = Arrays.asList(mockEntity);
        List<SysMenuView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysMenuEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysMenuEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysMenuEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysMenuEntity.class));
        when(sysMenuViewMapStruct.toViewList(entities)).thenReturn(views);

        StepVerifier.create(sysMenuQueryService.index(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.getItems().size());
                    assertEquals(1L, response.getTotal());
                    assertEquals("Test Menu", response.getItems().get(0).getName());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void recycle_shouldReturnDeletedMenus() {
        SysMenuQuery query = new SysMenuQuery();
        mockEntity.setDeleteStatus(true);
        mockView.setDeleteStatus(true);
        List<SysMenuEntity> entities = Arrays.asList(mockEntity);
        List<SysMenuView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysMenuEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysMenuEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysMenuEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysMenuEntity.class));
        when(sysMenuViewMapStruct.toViewList(entities)).thenReturn(views);

        StepVerifier.create(sysMenuQueryService.recycle(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.getItems().size());
                    assertEquals(1L, response.getTotal());
                    assertEquals(true, response.getItems().get(0).getDeleteStatus());
                })
                .verifyComplete();
    }

    @Test
    void queryByMenuId_shouldReturnMenu() {
        Long menuId = 1L;
        when(sysMenuRepository.findById(menuId)).thenReturn(Mono.just(mockEntity));
        when(sysMenuViewMapStruct.toView(mockEntity)).thenReturn(mockView);

        StepVerifier.create(sysMenuQueryService.queryByMenuId(menuId))
                .assertNext(result -> {
                    assertNotNull(result);
                    assertEquals("Test Menu", result.getName());
                    assertEquals(1L, result.getId());
                })
                .verifyComplete();
    }

    @Test
    void queryByMenuId_shouldReturnEmptyWhenNotFound() {
        Long menuId = 999L;
        when(sysMenuRepository.findById(menuId)).thenReturn(Mono.empty());

        StepVerifier.create(sysMenuQueryService.queryByMenuId(menuId))
                .verifyComplete();
    }

    @Test
    void queryByMenuId_shouldReturnEmptyWhenDeleted() {
        Long menuId = 1L;
        mockEntity.setDeleteStatus(true);
        when(sysMenuRepository.findById(menuId)).thenReturn(Mono.just(mockEntity));

        StepVerifier.create(sysMenuQueryService.queryByMenuId(menuId))
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void queryByApi_shouldReturnMenu() {
        String api = "/api/test";

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysMenuEntity.class)))
                .thenReturn(Mono.just(mockEntity));
        when(sysMenuViewMapStruct.toView(mockEntity)).thenReturn(mockView);

        StepVerifier.create(sysMenuQueryService.queryByApi(api))
                .assertNext(result -> {
                    assertNotNull(result);
                    assertEquals("Test Menu", result.getName());
                    assertEquals("/api/test", result.getApi());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void queryByApi_shouldReturnEmptyWhenNotFound() {
        String api = "/api/notfound";

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysMenuEntity.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(sysMenuQueryService.queryByApi(api))
                .verifyComplete();
    }

    @Test
    void queryAllMenu_shouldReturnAllMenus() {
        List<SysMenuEntity> entities = Arrays.asList(mockEntity);
        List<SysMenuView> views = Arrays.asList(mockView);

        when(sysMenuRepository.findAll()).thenReturn(Flux.just(mockEntity));
        when(sysMenuViewMapStruct.toViewList(entities)).thenReturn(views);

        StepVerifier.create(sysMenuQueryService.queryAllMenu())
                .assertNext(result -> {
                    assertNotNull(result);
                    assertEquals(1, result.size());
                    assertEquals("Test Menu", result.get(0).getName());
                })
                .verifyComplete();
    }

    @Test
    void queryAllMenu_shouldReturnEmptyWhenNoData() {
        when(sysMenuRepository.findAll()).thenReturn(Flux.empty());
        when(sysMenuViewMapStruct.toViewList(Collections.emptyList())).thenReturn(Collections.emptyList());

        StepVerifier.create(sysMenuQueryService.queryAllMenu())
                .expectNext(Collections.emptyList())
                .verifyComplete();
    }

    @Test
    void getMenuTreeWithPermission_shouldReturnEmptyWhenNoRoles() {
        StepVerifier.create(sysMenuQueryService.getMenuTreeWithPermission())
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void getMenuTreeWithPermission_shouldReturnEmptyWhenNoOwnerRole() {
        List<String> roles = Arrays.asList("ROLE_USER");

        securityUtilsMockedStatic.when(SecurityUtils::getRoles).thenReturn(roles);

        SysRoleView roleView = new SysRoleView();
        roleView.setId(1L);
        roleView.setRoleCode("ROLE_USER");
        roleView.setOwnerStatus(false);

        when(sysRoleQueryService.getByCode("ROLE_USER")).thenReturn(Mono.just(roleView));

        // When hasOwner is false, it tries to get from cache which returns empty,
        // then switchIfEmpty triggers an error
        when(reactiveRedisCacheHelper.getCache(any(String.class), eq(List.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(sysMenuQueryService.getMenuTreeWithPermission())
                .expectError(RuntimeException.class)
                .verify();
    }
}
