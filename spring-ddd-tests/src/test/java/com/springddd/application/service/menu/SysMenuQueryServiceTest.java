package com.springddd.application.service.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.menu.dto.SysMenuQuery;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.application.service.menu.dto.SysMenuViewMapStruct;
import com.springddd.application.service.permission.DataScopeCriteriaBuilder;
import com.springddd.application.service.role.SysRoleQueryService;
import com.springddd.application.service.role.dto.SysRoleView;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.user.UserId;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.cache.util.CacheProcessor;
import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class SysMenuQueryServiceTest {

    @Mock
    private SysMenuViewMapStruct sysMenuViewMapStruct;

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private DataScopeCriteriaBuilder dataScopeCriteriaBuilder;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysMenuEntity> selectOp;

    @Mock
    private ReactiveSelectOperation.TerminatingSelect<SysMenuEntity> terminatingSelect;

    @Mock
    private SysMenuRepository sysMenuRepository;

    @Mock
    private CacheProcessor cacheProcessor;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private SysRoleQueryService sysRoleQueryService;

    @InjectMocks
    private SysMenuQueryService service;

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

    private Context withAuthUser(AuthUser user) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        return ReactiveSecurityContextHolder.withAuthentication(auth);
    }

    private AuthUser createAuthUser(Long userId, List<Long> menuIds, List<String> roles) {
        AuthUser user = new AuthUser();
        user.setUserId(new UserId(userId));
        user.setMenuIds(menuIds);
        user.setRoles(roles);
        return user;
    }

    @Test
    @DisplayName("index 应返回分页结果")
    void index_shouldReturnPage() {
        SysMenuQuery query = new SysMenuQuery();

        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setName("menu");

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setName("menu");

        when(r2dbcEntityTemplate.select(SysMenuEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysMenuViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysMenuEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> {
                    assertThat(page.getList()).hasSize(1);
                    assertThat(page.getList().get(0).getName()).isEqualTo("menu");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("recycle 应返回回收站分页结果")
    void recycle_shouldReturnRecyclePage() {
        SysMenuQuery query = new SysMenuQuery();

        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);

        SysMenuView view = new SysMenuView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(SysMenuEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysMenuViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysMenuEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.recycle(query))
                .assertNext(page -> assertThat(page.getList()).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryByMenuId 应返回菜单视图")
    void queryByMenuId_shouldReturnMenuView() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setName("menu");
        entity.setDeleteStatus(false);

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setName("menu");

        when(sysMenuRepository.findById(1L)).thenReturn(Mono.just(entity));
        when(sysMenuViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(service.queryByMenuId(1L))
                .assertNext(result -> assertThat(result.getName()).isEqualTo("menu"))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryByApi 应返回菜单视图")
    void queryByApi_shouldReturnMenuView() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setApi("/api/test");

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setApi("/api/test");

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysMenuEntity.class))).thenReturn(Mono.just(entity));
        when(sysMenuViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(service.queryByApi("/api/test"))
                .assertNext(result -> assertThat(result.getApi()).isEqualTo("/api/test"))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryAllMenu 应返回所有菜单")
    void queryAllMenu_shouldReturnAllMenus() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setName("menu");

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setName("menu");

        when(sysMenuRepository.findAll()).thenReturn(Flux.just(entity));
        when(sysMenuViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));

        StepVerifier.create(service.queryAllMenu())
                .assertNext(list -> assertThat(list).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryByPermissions 应构建菜单树并缓存过滤后的无权限树")
    void queryByPermissions_shouldBuildTreeAndCacheWithoutPermissionsTree() {
        AuthUser user = createAuthUser(1L, List.of(1L, 2L), List.of("ADMIN"));

        SysMenuEntity entity1 = new SysMenuEntity();
        entity1.setId(1L);
        entity1.setDeleteStatus(false);

        SysMenuEntity entity2 = new SysMenuEntity();
        entity2.setId(2L);
        entity2.setDeleteStatus(false);

        SysMenuView view1 = new SysMenuView();
        view1.setId(1L);
        view1.setParentId(null);
        view1.setDeleteStatus(false);
        view1.setMenuType(1);
        SysMenuView.Meta meta1 = new SysMenuView.Meta();
        meta1.setOrder(1);
        view1.setMeta(meta1);

        SysMenuView view2 = new SysMenuView();
        view2.setId(2L);
        view2.setParentId(null);
        view2.setDeleteStatus(false);
        view2.setMenuType(3);
        SysMenuView.Meta meta2 = new SysMenuView.Meta();
        meta2.setOrder(2);
        view2.setMeta(meta2);

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysMenuEntity.class)))
                .thenReturn(Mono.just(entity1))
                .thenReturn(Mono.just(entity2));
        when(sysMenuViewMapStruct.toView(entity1)).thenReturn(view1);
        when(sysMenuViewMapStruct.toView(entity2)).thenReturn(view2);
        when(cacheProcessor.setCache(anyString(), anyList(), any())).thenReturn(Mono.just(true));

        StepVerifier.create(service.queryByPermissions().contextWrite(withAuthUser(user)))
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    assertThat(list.get(0).getId()).isEqualTo(1L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getMenuTreeWithoutPermission 应从缓存返回菜单")
    void getMenuTreeWithoutPermission_shouldReturnCachedMenus() {
        AuthUser user = createAuthUser(1L, List.of(1L), List.of("ADMIN"));

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setName("menu");

        List<SysMenuView> cachedList = List.of(view);
        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.just(cachedList));
        when(objectMapper.convertValue(eq(cachedList), any(TypeReference.class))).thenReturn(cachedList);

        StepVerifier.create(service.getMenuTreeWithoutPermission().contextWrite(withAuthUser(user)))
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    assertThat(list.get(0).getName()).isEqualTo("menu");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getMenuTreeWithoutPermission 缓存为空时应抛出异常")
    void getMenuTreeWithoutPermission_shouldThrowWhenCacheMiss() {
        AuthUser user = createAuthUser(1L, List.of(1L), List.of("ADMIN"));
        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.empty());

        StepVerifier.create(service.getMenuTreeWithoutPermission().contextWrite(withAuthUser(user)))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("No menus found"))
                .verify();
    }

    @Test
    @DisplayName("getMenuTreeWithoutPermission 反序列化失败时应抛出异常")
    void getMenuTreeWithoutPermission_shouldThrowOnDeserializationError() {
        AuthUser user = createAuthUser(1L, List.of(1L), List.of("ADMIN"));
        List<?> cachedList = List.of("invalid");
        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.just(cachedList));
        when(objectMapper.convertValue(eq(cachedList), any(TypeReference.class)))
                .thenThrow(new IllegalArgumentException("bad data"));

        StepVerifier.create(service.getMenuTreeWithoutPermission().contextWrite(withAuthUser(user)))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("Error deserializing SysMenuView list"))
                .verify();
    }

    @Test
    @DisplayName("getMenuTreeWithPermission 角色为空时应返回空")
    void getMenuTreeWithPermission_whenRolesEmpty_shouldReturnEmpty() {
        AuthUser user = createAuthUser(1L, List.of(1L), List.of());

        StepVerifier.create(service.getMenuTreeWithPermission().contextWrite(withAuthUser(user)))
                .verifyComplete();
    }

    @Test
    @DisplayName("getMenuTreeWithPermission 拥有 ownerStatus 角色时应查询全部菜单并构建树")
    void getMenuTreeWithPermission_whenHasOwner_shouldQueryAndBuildTree() {
        AuthUser user = createAuthUser(1L, List.of(1L), List.of("ADMIN"));

        SysRoleView roleView = new SysRoleView();
        roleView.setRoleCode("ADMIN");
        roleView.setOwnerStatus(true);

        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setDeleteStatus(false);

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setParentId(null);
        view.setDeleteStatus(false);
        view.setMenuType(1);
        SysMenuView.Meta meta = new SysMenuView.Meta();
        meta.setOrder(1);
        view.setMeta(meta);

        when(sysRoleQueryService.getByCode("ADMIN")).thenReturn(Mono.just(roleView));
        when(r2dbcEntityTemplate.select(SysMenuEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysMenuViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));

        StepVerifier.create(service.getMenuTreeWithPermission().contextWrite(withAuthUser(user)))
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    assertThat(list.get(0).getId()).isEqualTo(1L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getMenuTreeWithPermission 无 ownerStatus 角色时应从缓存读取")
    void getMenuTreeWithPermission_whenNoOwner_shouldReturnCachedMenus() {
        AuthUser user = createAuthUser(1L, List.of(1L), List.of("ADMIN"));

        SysRoleView roleView = new SysRoleView();
        roleView.setRoleCode("ADMIN");
        roleView.setOwnerStatus(false);

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setName("cached");

        List<SysMenuView> cachedList = List.of(view);
        when(sysRoleQueryService.getByCode("ADMIN")).thenReturn(Mono.just(roleView));
        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.just(cachedList));
        when(objectMapper.convertValue(eq(cachedList), any(TypeReference.class))).thenReturn(cachedList);

        StepVerifier.create(service.getMenuTreeWithPermission().contextWrite(withAuthUser(user)))
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    assertThat(list.get(0).getName()).isEqualTo("cached");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getMenuTreeWithPermission 无 ownerStatus 且缓存为空时应抛出异常")
    void getMenuTreeWithPermission_whenNoOwnerAndCacheMiss_shouldThrow() {
        AuthUser user = createAuthUser(1L, List.of(1L), List.of("ADMIN"));

        SysRoleView roleView = new SysRoleView();
        roleView.setRoleCode("ADMIN");
        roleView.setOwnerStatus(false);

        when(sysRoleQueryService.getByCode("ADMIN")).thenReturn(Mono.just(roleView));
        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.empty());

        StepVerifier.create(service.getMenuTreeWithPermission().contextWrite(withAuthUser(user)))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("No menus found"))
                .verify();
    }

    @Test
    @DisplayName("getMenuTreeWithPermission 角色查询返回null时应从缓存读取")
    void getMenuTreeWithPermission_whenRoleReturnsNull_shouldReturnCachedMenus() {
        AuthUser user = createAuthUser(1L, List.of(1L), List.of("ADMIN"));

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        List<SysMenuView> cachedList = List.of(view);

        when(sysRoleQueryService.getByCode("ADMIN")).thenReturn(Mono.empty());
        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.just(cachedList));
        when(objectMapper.convertValue(eq(cachedList), any(TypeReference.class))).thenReturn(cachedList);

        StepVerifier.create(service.getMenuTreeWithPermission().contextWrite(withAuthUser(user)))
                .assertNext(list -> assertThat(list).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("buildTree 应加载父节点并构建菜单树")
    void buildTree_shouldLoadParentsAndBuildTree() throws Exception {
        Method method = SysMenuQueryService.class.getDeclaredMethod("buildTree");
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Function<List<SysMenuView>, Mono<? extends List<SysMenuView>>> func =
                (Function<List<SysMenuView>, Mono<? extends List<SysMenuView>>>) method.invoke(service);

        SysMenuEntity parentEntity = new SysMenuEntity();
        parentEntity.setId(1L);
        parentEntity.setDeleteStatus(false);

        SysMenuView parentView = new SysMenuView();
        parentView.setId(1L);
        parentView.setParentId(null);
        parentView.setDeleteStatus(false);
        parentView.setMenuType(1);
        SysMenuView.Meta meta1 = new SysMenuView.Meta();
        meta1.setOrder(1);
        parentView.setMeta(meta1);

        SysMenuView childView = new SysMenuView();
        childView.setId(2L);
        childView.setParentId(1L);
        childView.setDeleteStatus(false);
        childView.setMenuType(1);
        SysMenuView.Meta meta2 = new SysMenuView.Meta();
        meta2.setOrder(1);
        childView.setMeta(meta2);

        SysMenuView deletedView = new SysMenuView();
        deletedView.setId(3L);
        deletedView.setParentId(1L);
        deletedView.setDeleteStatus(true);
        deletedView.setMenuType(1);
        SysMenuView.Meta meta3 = new SysMenuView.Meta();
        meta3.setOrder(1);
        deletedView.setMeta(meta3);

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysMenuEntity.class))).thenReturn(Mono.just(parentEntity));
        when(sysMenuViewMapStruct.toView(parentEntity)).thenReturn(parentView);

        StepVerifier.create(func.apply(List.of(childView, deletedView)))
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    assertThat(list.get(0).getId()).isEqualTo(1L);
                    assertThat(list.get(0).getChildren()).hasSize(1);
                    assertThat(list.get(0).getChildren().get(0).getId()).isEqualTo(2L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("cacheTree 应缓存带权限和不带权限的菜单树")
    void cacheTree_shouldCacheBothTrees() throws Exception {
        Method method = SysMenuQueryService.class.getDeclaredMethod("cacheTree");
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Function<List<SysMenuView>, Mono<? extends List<SysMenuView>>> func =
                (Function<List<SysMenuView>, Mono<? extends List<SysMenuView>>>) method.invoke(service);

        AuthUser user = createAuthUser(1L, List.of(1L), List.of("ADMIN"));

        SysMenuView view1 = new SysMenuView();
        view1.setId(1L);
        view1.setMenuType(1);

        SysMenuView view2 = new SysMenuView();
        view2.setId(2L);
        view2.setMenuType(3);

        when(cacheProcessor.setCache(anyString(), anyList(), any())).thenReturn(Mono.just(true));

        StepVerifier.create(func.apply(List.of(view1, view2)).contextWrite(withAuthUser(user)))
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    assertThat(list.get(0).getId()).isEqualTo(1L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("extractMenuWithoutPermissionsTree 应过滤 menuType=3 的菜单")
    void extractMenuWithoutPermissionsTree_shouldFilterType3Menus() throws Exception {
        Method method = SysMenuQueryService.class.getDeclaredMethod("extractMenuWithoutPermissionsTree", List.class);
        method.setAccessible(true);

        SysMenuView view1 = new SysMenuView();
        view1.setId(1L);
        view1.setMenuType(1);

        SysMenuView view2 = new SysMenuView();
        view2.setId(2L);
        view2.setMenuType(3);

        @SuppressWarnings("unchecked")
        List<SysMenuView> result = (List<SysMenuView>) method.invoke(service, List.of(view1, view2));
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("filterOutInvalidMenusRecursively 应递归过滤 menuType=3 的菜单")
    void filterOutInvalidMenusRecursively_shouldFilterRecursively() throws Exception {
        Method method = SysMenuQueryService.class.getDeclaredMethod("filterOutInvalidMenusRecursively", SysMenuView.class);
        method.setAccessible(true);

        SysMenuView child1 = new SysMenuView();
        child1.setId(2L);
        child1.setMenuType(1);
        child1.setChildren(null);

        SysMenuView child2 = new SysMenuView();
        child2.setId(3L);
        child2.setMenuType(3);
        child2.setChildren(null);

        SysMenuView parent = new SysMenuView();
        parent.setId(1L);
        parent.setMenuType(1);
        parent.setChildren(List.of(child1, child2));

        SysMenuView result = (SysMenuView) method.invoke(service, parent);
        assertThat(result).isNotNull();
        assertThat(result.getChildren()).hasSize(1);
        assertThat(result.getChildren().get(0).getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("filterOutInvalidMenusRecursively 当菜单类型为3时应返回null")
    void filterOutInvalidMenusRecursively_whenMenuType3_shouldReturnNull() throws Exception {
        Method method = SysMenuQueryService.class.getDeclaredMethod("filterOutInvalidMenusRecursively", SysMenuView.class);
        method.setAccessible(true);

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setMenuType(3);

        SysMenuView result = (SysMenuView) method.invoke(service, view);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("cacheMenuWithPermissionsTree 应设置带权限的菜单缓存")
    void cacheMenuWithPermissionsTree_shouldSetCache() throws Exception {
        Method method = SysMenuQueryService.class.getDeclaredMethod("cacheMenuWithPermissionsTree", List.class);
        method.setAccessible(true);

        AuthUser user = createAuthUser(1L, List.of(1L), List.of("ADMIN"));
        SysMenuView view = new SysMenuView();
        view.setId(1L);

        when(cacheProcessor.setCache(anyString(), anyList(), any())).thenReturn(Mono.just(true));

        @SuppressWarnings("unchecked")
        Mono<Void> result = (Mono<Void>) method.invoke(service, List.of(view));

        StepVerifier.create(result.contextWrite(withAuthUser(user)))
                .verifyComplete();
    }

    @Test
    @DisplayName("cacheMenuWithoutPermissionsTree 应设置不带权限的菜单缓存")
    void cacheMenuWithoutPermissionsTree_shouldSetCache() throws Exception {
        Method method = SysMenuQueryService.class.getDeclaredMethod("cacheMenuWithoutPermissionsTree", List.class);
        method.setAccessible(true);

        AuthUser user = createAuthUser(1L, List.of(1L), List.of("ADMIN"));
        SysMenuView view = new SysMenuView();
        view.setId(1L);

        when(cacheProcessor.setCache(anyString(), anyList(), any())).thenReturn(Mono.just(true));

        @SuppressWarnings("unchecked")
        Mono<Void> result = (Mono<Void>) method.invoke(service, List.of(view));

        StepVerifier.create(result.contextWrite(withAuthUser(user)))
                .verifyComplete();
    }

    @Test
    @DisplayName("getTreeWithPermission 当 hasOwner=true 时应查询所有菜单并构建树")
    void getTreeWithPermission_whenHasOwner_shouldQueryAllMenus() throws Exception {
        Method method = SysMenuQueryService.class.getDeclaredMethod("getTreeWithPermission", Long.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Function<Boolean, Mono<? extends List<SysMenuView>>> func =
                (Function<Boolean, Mono<? extends List<SysMenuView>>>) method.invoke(service, 1L);

        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setDeleteStatus(false);

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setParentId(null);
        view.setDeleteStatus(false);
        view.setMenuType(1);
        SysMenuView.Meta meta = new SysMenuView.Meta();
        meta.setOrder(1);
        view.setMeta(meta);

        when(r2dbcEntityTemplate.select(SysMenuEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysMenuViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));

        StepVerifier.create(func.apply(true))
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    assertThat(list.get(0).getId()).isEqualTo(1L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("getTreeWithPermission 当 hasOwner=false 时应从缓存读取")
    void getTreeWithPermission_whenNoOwner_shouldReturnCachedMenus() throws Exception {
        Method method = SysMenuQueryService.class.getDeclaredMethod("getTreeWithPermission", Long.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Function<Boolean, Mono<? extends List<SysMenuView>>> func =
                (Function<Boolean, Mono<? extends List<SysMenuView>>>) method.invoke(service, 1L);

        SysMenuView view = new SysMenuView();
        view.setId(1L);
        List<SysMenuView> cachedList = List.of(view);

        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.just(cachedList));
        when(objectMapper.convertValue(eq(cachedList), any(TypeReference.class))).thenReturn(cachedList);

        StepVerifier.create(func.apply(false))
                .assertNext(list -> assertThat(list).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("getTreeWithPermission 当 hasOwner=false 且缓存反序列化失败时应抛出异常")
    void getTreeWithPermission_whenNoOwnerAndDeserializationFails_shouldThrow() throws Exception {
        Method method = SysMenuQueryService.class.getDeclaredMethod("getTreeWithPermission", Long.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Function<Boolean, Mono<? extends List<SysMenuView>>> func =
                (Function<Boolean, Mono<? extends List<SysMenuView>>>) method.invoke(service, 1L);

        List<?> cachedList = List.of("invalid");
        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.just(cachedList));
        when(objectMapper.convertValue(eq(cachedList), any(TypeReference.class)))
                .thenThrow(new IllegalArgumentException("bad data"));

        StepVerifier.create(func.apply(false))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException
                        && throwable.getMessage().equals("Error deserializing SysMenuView list"))
                .verify();
    }

    @Test
    @DisplayName("queryByMenuId 当菜单已删除时应返回 empty")
    void queryByMenuId_whenDeleted_shouldReturnEmpty() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setName("deleted_menu");
        entity.setDeleteStatus(true);

        when(sysMenuRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(service.queryByMenuId(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryByPermissions lambda$5 应从 SecurityUtils 创建 AuthUser")
    void queryByPermissions_lambda5_shouldCreateAuthUserFromSecurityUtils() throws Exception {
        java.lang.reflect.Method method = SysMenuQueryService.class.getDeclaredMethod("lambda$queryByPermissions$5");
        method.setAccessible(true);

        SecurityUtils.setUserId(99L);
        SecurityUtils.setMenuIds(List.of(1L, 2L));
        SecurityUtils.setRoles(List.of("ADMIN", "USER"));

        @SuppressWarnings("unchecked")
        Mono<AuthUser> result = (Mono<AuthUser>) method.invoke(null);

        StepVerifier.create(result)
                .assertNext(user -> {
                    assertThat(user.getUserId().value()).isEqualTo(99L);
                    assertThat(user.getMenuIds()).containsExactly(1L, 2L);
                    assertThat(user.getRoles()).containsExactly("ADMIN", "USER");
                })
                .verifyComplete();
    }
}
