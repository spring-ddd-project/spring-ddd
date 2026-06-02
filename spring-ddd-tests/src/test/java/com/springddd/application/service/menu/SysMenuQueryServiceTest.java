package com.springddd.application.service.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.menu.dto.SysMenuQuery;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.application.service.menu.dto.SysMenuViewMapStruct;
import com.springddd.application.service.role.SysRoleQueryService;
import com.springddd.application.service.role.dto.SysRoleView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysMenuEntity> reactiveSelect;

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
    void index_shouldReturnPageResponse_whenEntitiesExist() {
        SysMenuQuery query = new SysMenuQuery();

        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setDeleteStatus(false);

        SysMenuView view = new SysMenuView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(SysMenuEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysMenuEntity.class))).thenReturn(Mono.just(1L));
        when(sysMenuViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysMenuQueryService.index(query))
                .assertNext(pageResponse -> {
                    assertNotNull(pageResponse);
                    assertNotNull(pageResponse.getItems());
                })
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse_whenDeletedEntitiesExist() {
        SysMenuQuery query = new SysMenuQuery();

        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setDeleteStatus(true);

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setDeleteStatus(true);

        when(r2dbcEntityTemplate.select(SysMenuEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysMenuEntity.class))).thenReturn(Mono.just(1L));
        when(sysMenuViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysMenuQueryService.recycle(query))
                .assertNext(pageResponse -> assertNotNull(pageResponse))
                .verifyComplete();
    }

    @Test
    void queryByMenuId_shouldReturnView_whenEntityExistsAndNotDeleted() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setDeleteStatus(false);

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setDeleteStatus(false);

        when(sysMenuRepository.findById(1L)).thenReturn(Mono.just(entity));
        when(sysMenuViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(sysMenuQueryService.queryByMenuId(1L))
                .expectNext(view)
                .verifyComplete();
    }

    @Test
    void queryByMenuId_shouldReturnEmpty_whenEntityIsDeleted() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setDeleteStatus(true);

        when(sysMenuRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(sysMenuQueryService.queryByMenuId(1L))
                .verifyComplete();
    }

    @Test
    void queryByApi_shouldReturnView_whenEntityExists() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setApi("/api/test");

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setApi("/api/test");

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysMenuEntity.class))).thenReturn(Mono.just(entity));
        when(sysMenuViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(sysMenuQueryService.queryByApi("/api/test"))
                .expectNext(view)
                .verifyComplete();
    }

    @Test
    void queryAllMenu_shouldReturnAllViews() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setName("menu1");

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setName("menu1");

        when(sysMenuRepository.findAll()).thenReturn(Flux.just(entity));
        when(sysMenuViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysMenuQueryService.queryAllMenu())
                .expectNext(Collections.singletonList(view))
                .verifyComplete();
    }

    @Test
    void queryByPermissions_shouldBuildTreeAndCache() {
        SecurityUtils.setUserId(1L);
        SecurityUtils.setMenuIds(List.of(1L));

        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setParentId(null);
        entity.setDeleteStatus(false);
        entity.setSortOrder(1);

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setParentId(null);
        view.setDeleteStatus(false);
        view.setMenuType(1);
        view.setMenuStatus(true);
        SysMenuView.Meta meta = new SysMenuView.Meta();
        meta.setOrder(1);
        view.setMeta(meta);
        view.setChildren(new ArrayList<>());

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysMenuEntity.class)))
                .thenReturn(Mono.just(entity));
        when(sysMenuViewMapStruct.toView(entity)).thenReturn(view);
        when(r2dbcEntityTemplate.select(SysMenuEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(sysMenuViewMapStruct.toViewList(any())).thenReturn(List.of(view));
        when(reactiveRedisCacheHelper.setCache(any(), any(), any())).thenReturn(Mono.just(true));

        StepVerifier.create(sysMenuQueryService.queryByPermissions())
                .assertNext(result -> assertNotNull(result))
                .verifyComplete();
    }

    @Test
    void getMenuTreeWithoutPermission_shouldReturnCachedMenus() {
        SecurityUtils.setUserId(1L);

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setName("menu");

        List<SysMenuView> cachedList = List.of(view);

        when(reactiveRedisCacheHelper.getCache(CacheKeys.MENU_WITHOUT_PERMISSIONS.buildKey(1L), List.class))
                .thenReturn(Mono.just(cachedList));
        when(objectMapper.convertValue(eq(cachedList), any(TypeReference.class)))
                .thenReturn(cachedList);

        StepVerifier.create(sysMenuQueryService.getMenuTreeWithoutPermission())
                .assertNext(result -> {
                    assertNotNull(result);
                    assertEquals(1, result.size());
                })
                .verifyComplete();
    }

    @Test
    void getMenuTreeWithoutPermission_shouldError_whenCacheMiss() {
        SecurityUtils.setUserId(1L);

        when(reactiveRedisCacheHelper.getCache(CacheKeys.MENU_WITHOUT_PERMISSIONS.buildKey(1L), List.class))
                .thenReturn(Mono.empty());

        StepVerifier.create(sysMenuQueryService.getMenuTreeWithoutPermission())
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void getMenuTreeWithPermission_shouldReturnAllMenus_whenOwnerRoleExists() {
        SecurityUtils.setRoles(List.of("ROLE_ADMIN"));

        SysRoleView roleView = new SysRoleView();
        roleView.setRoleCode("ROLE_ADMIN");
        roleView.setOwnerStatus(true);

        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setParentId(null);
        entity.setDeleteStatus(false);
        entity.setSortOrder(1);

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setParentId(null);
        view.setDeleteStatus(false);
        view.setMenuType(1);
        view.setMenuStatus(true);
        view.setMenuStatus(true);
        SysMenuView.Meta meta = new SysMenuView.Meta();
        meta.setOrder(1);
        view.setMeta(meta);
        view.setChildren(new ArrayList<>());

        when(sysRoleQueryService.getByCode("ROLE_ADMIN")).thenReturn(Mono.just(roleView));
        when(r2dbcEntityTemplate.select(SysMenuEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(sysMenuViewMapStruct.toViewList(any())).thenReturn(List.of(view));

        StepVerifier.create(sysMenuQueryService.getMenuTreeWithPermission())
                .assertNext(result -> assertNotNull(result))
                .verifyComplete();
    }

    @Test
    void getMenuTreeWithPermission_shouldReturnCachedMenus_whenNoOwnerRole() {
        SecurityUtils.setRoles(List.of("ROLE_USER"));

        SysRoleView roleView = new SysRoleView();
        roleView.setRoleCode("ROLE_USER");
        roleView.setOwnerStatus(false);

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setName("menu");

        List<SysMenuView> cachedList = List.of(view);

        when(sysRoleQueryService.getByCode("ROLE_USER")).thenReturn(Mono.just(roleView));
        when(reactiveRedisCacheHelper.getCache(any(), eq(List.class)))
                .thenReturn(Mono.just(cachedList));
        when(objectMapper.convertValue(eq(cachedList), any(TypeReference.class)))
                .thenReturn(cachedList);

        StepVerifier.create(sysMenuQueryService.getMenuTreeWithPermission())
                .assertNext(result -> {
                    assertNotNull(result);
                    assertEquals(1, result.size());
                })
                .verifyComplete();
    }

    @Test
    void getMenuTreeWithPermission_shouldReturnEmpty_whenRolesEmpty() {
        SecurityUtils.setRoles(Collections.emptyList());

        StepVerifier.create(sysMenuQueryService.getMenuTreeWithPermission())
                .verifyComplete();
    }

    @Test
    void getMenuTreeWithPermission_shouldError_whenNoCachedMenus() {
        SecurityUtils.setRoles(List.of("ROLE_USER"));

        SysRoleView roleView = new SysRoleView();
        roleView.setRoleCode("ROLE_USER");
        roleView.setOwnerStatus(false);

        when(sysRoleQueryService.getByCode("ROLE_USER")).thenReturn(Mono.just(roleView));
        when(reactiveRedisCacheHelper.getCache(any(), eq(List.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(sysMenuQueryService.getMenuTreeWithPermission())
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void getMenuTreeWithoutPermission_shouldError_whenNoCachedMenus() {
        SecurityUtils.setUserId(1L);

        when(reactiveRedisCacheHelper.getCache(CacheKeys.MENU_WITHOUT_PERMISSIONS.buildKey(1L), List.class))
                .thenReturn(Mono.empty());

        StepVerifier.create(sysMenuQueryService.getMenuTreeWithoutPermission())
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void queryByPermissions_shouldFilterOutButtonMenus() {
        SecurityUtils.setUserId(1L);
        SecurityUtils.setMenuIds(List.of(1L));

        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setParentId(null);
        entity.setDeleteStatus(false);
        entity.setSortOrder(1);
        entity.setMenuType(1);

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setParentId(null);
        view.setDeleteStatus(false);
        view.setMenuType(1);
        view.setMenuStatus(true);
        view.setMenuStatus(true);
        view.setMenuStatus(true);
        SysMenuView.Meta meta = new SysMenuView.Meta();
        meta.setOrder(1);
        view.setMeta(meta);
        view.setChildren(new ArrayList<>());

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysMenuEntity.class)))
                .thenReturn(Mono.just(entity));
        when(sysMenuViewMapStruct.toView(entity)).thenReturn(view);
        when(r2dbcEntityTemplate.select(SysMenuEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(sysMenuViewMapStruct.toViewList(any())).thenReturn(List.of(view));
        when(reactiveRedisCacheHelper.setCache(any(), any(), any())).thenReturn(Mono.just(true));

        StepVerifier.create(sysMenuQueryService.queryByPermissions())
                .assertNext(result -> assertNotNull(result))
                .verifyComplete();
    }

    @Test
    void queryByPermissions_shouldFilterOutMenuTypeThree() {
        SecurityUtils.setUserId(1L);
        SecurityUtils.setMenuIds(List.of(1L));

        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setParentId(null);
        entity.setDeleteStatus(false);
        entity.setSortOrder(1);
        entity.setMenuType(3);

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setParentId(null);
        view.setDeleteStatus(false);
        view.setMenuType(3);
        view.setMenuStatus(true);
        SysMenuView.Meta meta = new SysMenuView.Meta();
        meta.setOrder(1);
        view.setMeta(meta);
        view.setChildren(new ArrayList<>());

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysMenuEntity.class)))
                .thenReturn(Mono.just(entity));
        when(sysMenuViewMapStruct.toView(entity)).thenReturn(view);
        when(r2dbcEntityTemplate.select(SysMenuEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(sysMenuViewMapStruct.toViewList(any())).thenReturn(List.of(view));
        when(reactiveRedisCacheHelper.setCache(any(), any(), any())).thenReturn(Mono.just(true));

        StepVerifier.create(sysMenuQueryService.queryByPermissions())
                .assertNext(result -> assertNotNull(result))
                .verifyComplete();
    }

    @Test
    void queryByPermissions_shouldLoadParentNodes() {
        SecurityUtils.setUserId(1L);
        SecurityUtils.setMenuIds(List.of(2L));

        SysMenuEntity childEntity = new SysMenuEntity();
        childEntity.setId(2L);
        childEntity.setParentId(1L);
        childEntity.setDeleteStatus(false);
        childEntity.setSortOrder(1);
        childEntity.setMenuType(1);

        SysMenuEntity parentEntity = new SysMenuEntity();
        parentEntity.setId(1L);
        parentEntity.setParentId(null);
        parentEntity.setDeleteStatus(false);
        parentEntity.setSortOrder(1);
        parentEntity.setMenuType(1);

        SysMenuView childView = new SysMenuView();
        childView.setId(2L);
        childView.setParentId(1L);
        childView.setDeleteStatus(false);
        childView.setMenuType(1);
        childView.setMenuStatus(true);
        SysMenuView.Meta childMeta = new SysMenuView.Meta();
        childMeta.setOrder(1);
        childView.setMeta(childMeta);
        childView.setChildren(new ArrayList<>());

        SysMenuView parentView = new SysMenuView();
        parentView.setId(1L);
        parentView.setParentId(null);
        parentView.setDeleteStatus(false);
        parentView.setMenuType(1);
        parentView.setMenuStatus(true);
        SysMenuView.Meta parentMeta = new SysMenuView.Meta();
        parentMeta.setOrder(1);
        parentView.setMeta(parentMeta);
        parentView.setChildren(new ArrayList<>());

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysMenuEntity.class)))
                .thenReturn(Mono.just(childEntity))
                .thenReturn(Mono.just(parentEntity));
        when(sysMenuViewMapStruct.toView(childEntity)).thenReturn(childView);
        when(sysMenuViewMapStruct.toView(parentEntity)).thenReturn(parentView);
        when(sysMenuViewMapStruct.toViewList(any())).thenReturn(List.of(childView));
        when(reactiveRedisCacheHelper.setCache(any(), any(), any())).thenReturn(Mono.just(true));

        StepVerifier.create(sysMenuQueryService.queryByPermissions())
                .assertNext(result -> assertNotNull(result))
                .verifyComplete();
    }

    @Test
    void getMenuTreeWithoutPermission_shouldError_whenDeserializationFails() {
        SecurityUtils.setUserId(1L);

        List<SysMenuView> cachedList = List.of();

        when(reactiveRedisCacheHelper.getCache(CacheKeys.MENU_WITHOUT_PERMISSIONS.buildKey(1L), List.class))
                .thenReturn(Mono.just(cachedList));
        when(objectMapper.convertValue(eq(cachedList), any(TypeReference.class)))
                .thenThrow(new IllegalArgumentException("bad data"));

        StepVerifier.create(sysMenuQueryService.getMenuTreeWithoutPermission())
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void getMenuTreeWithPermission_shouldError_whenDeserializationFails() {
        SecurityUtils.setRoles(List.of("ROLE_USER"));

        SysRoleView roleView = new SysRoleView();
        roleView.setRoleCode("ROLE_USER");
        roleView.setOwnerStatus(false);

        List<SysMenuView> cachedList = List.of();

        when(sysRoleQueryService.getByCode("ROLE_USER")).thenReturn(Mono.just(roleView));
        when(reactiveRedisCacheHelper.getCache(any(), eq(List.class)))
                .thenReturn(Mono.just(cachedList));
        when(objectMapper.convertValue(eq(cachedList), any(TypeReference.class)))
                .thenThrow(new IllegalArgumentException("bad data"));

        StepVerifier.create(sysMenuQueryService.getMenuTreeWithPermission())
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void filterOutInvalidMenusRecursively_shouldReturnNull_whenMenuTypeIsThree() throws Exception {
        Method method = SysMenuQueryService.class.getDeclaredMethod("filterOutInvalidMenusRecursively", SysMenuView.class);
        method.setAccessible(true);

        SysMenuView menu = new SysMenuView();
        menu.setMenuType(3);

        Object result = method.invoke(sysMenuQueryService, menu);
        assertNull(result);
    }

    @Test
    void filterOutInvalidMenusRecursively_shouldReturnCopy_whenMenuTypeIsNotThree() throws Exception {
        Method method = SysMenuQueryService.class.getDeclaredMethod("filterOutInvalidMenusRecursively", SysMenuView.class);
        method.setAccessible(true);

        SysMenuView child = new SysMenuView();
        child.setMenuType(1);
        child.setId(2L);

        SysMenuView menu = new SysMenuView();
        menu.setMenuType(1);
        menu.setId(1L);
        menu.setChildren(List.of(child));
        SysMenuView.Meta meta = new SysMenuView.Meta();
        meta.setOrder(1);
        menu.setMeta(meta);

        Object result = method.invoke(sysMenuQueryService, menu);
        assertNotNull(result);
        SysMenuView copy = (SysMenuView) result;
        assertEquals(1L, copy.getId());
        assertNotNull(copy.getChildren());
    }

    @Test
    void copyMenuWithoutChildren_shouldCopyAllFields() throws Exception {
        Method method = SysMenuQueryService.class.getDeclaredMethod("copyMenuWithoutChildren", SysMenuView.class);
        method.setAccessible(true);

        SysMenuView.Meta meta = new SysMenuView.Meta();
        meta.setOrder(1);

        SysMenuView menu = new SysMenuView();
        menu.setMeta(meta);
        menu.setId(1L);
        menu.setParentId(0L);
        menu.setMenuType(1);
        menu.setPath("/path");
        menu.setName("menu");
        menu.setComponent("component");
        menu.setApi("/api");
        menu.setPermission("perm");
        menu.setVisible(true);
        menu.setEmbedded(false);
        menu.setMenuStatus(true);

        Object result = method.invoke(sysMenuQueryService, menu);
        assertNotNull(result);
        SysMenuView copy = (SysMenuView) result;
        assertEquals(menu.getMeta(), copy.getMeta());
        assertEquals(menu.getId(), copy.getId());
        assertEquals(menu.getParentId(), copy.getParentId());
        assertEquals(menu.getMenuType(), copy.getMenuType());
        assertEquals(menu.getPath(), copy.getPath());
        assertEquals(menu.getName(), copy.getName());
        assertEquals(menu.getComponent(), copy.getComponent());
        assertEquals(menu.getApi(), copy.getApi());
        assertEquals(menu.getPermission(), copy.getPermission());
        assertEquals(menu.getVisible(), copy.getVisible());
        assertEquals(menu.getEmbedded(), copy.getEmbedded());
        assertEquals(menu.getMenuStatus(), copy.getMenuStatus());
    }
}
