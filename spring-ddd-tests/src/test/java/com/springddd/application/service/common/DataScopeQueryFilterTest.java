package com.springddd.application.service.common;

import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.domain.role.DataScope;
import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import com.springddd.infrastructure.persistence.entity.SysPostEntity;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import com.springddd.infrastructure.persistence.entity.SysRowPermissionEntity;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import com.springddd.infrastructure.persistence.entity.SysUserPostEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
import com.springddd.infrastructure.persistence.r2dbc.SysRowPermissionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveSelectOperation;
import org.springframework.data.relational.core.query.CriteriaDefinition;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class DataScopeQueryFilterTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysRoleRepository sysRoleRepository;

    @Mock
    private SysRowPermissionRepository sysRowPermissionRepository;

    private DataScopeQueryFilter filter;
    private final Map<Class<?>, List<StubConfig<?>>> stubRegistry = new HashMap<>();
    private final Map<String, SysRoleEntity> roleByCode = new HashMap<>();
    private final Map<Long, List<SysRowPermissionEntity>> configsByRoleId = new HashMap<>();

    @BeforeEach
    void setUp() {
        stubRegistry.clear();
        roleByCode.clear();
        configsByRoleId.clear();
        filter = new DataScopeQueryFilter(r2dbcEntityTemplate, sysRoleRepository, sysRowPermissionRepository);
        when(r2dbcEntityTemplate.select(any(Class.class))).thenAnswer(invocation -> {
            Class<?> entityClass = invocation.getArgument(0);
            ReactiveSelectOperation.ReactiveSelect<?> select = mock(ReactiveSelectOperation.ReactiveSelect.class);
            when(select.matching(any(Query.class))).thenAnswer(matchInvocation -> {
                Query query = matchInvocation.getArgument(0);
                ReactiveSelectOperation.TerminatingSelect<?> terminating = mock(ReactiveSelectOperation.TerminatingSelect.class);
                applyStubs(terminating, entityClass, query);
                return terminating;
            });
            return select;
        });
        when(sysRoleRepository.findByRoleCodeAndDeleteStatusFalse(any())).thenAnswer(invocation -> {
            String roleCode = invocation.getArgument(0);
            SysRoleEntity role = roleByCode.get(roleCode);
            return role == null ? Mono.empty() : Mono.just(role);
        });
        when(sysRowPermissionRepository.findByRoleIdAndMenuIdAndDeleteStatusFalse(any(), any())).thenAnswer(invocation -> {
            Long roleId = invocation.getArgument(0);
            return Flux.fromIterable(configsByRoleId.getOrDefault(roleId, List.of()));
        });
    }

    @SuppressWarnings("unchecked")
    private <T> void applyStubs(ReactiveSelectOperation.TerminatingSelect<?> terminating, Class<?> entityClass, Query query) {
        List<StubConfig<?>> configs = stubRegistry.getOrDefault(entityClass, List.of());
        for (StubConfig<?> config : configs) {
            if (config.matcher.test(query)) {
                if (config.allResult != null) {
                    when(terminating.all()).thenReturn((Flux) config.allResult);
                }
                if (config.firstResult != null) {
                    when(terminating.first()).thenReturn((Mono) config.firstResult);
                }
            }
        }
    }

    // ---------------- tests ----------------

    @Test
    void nullMenuIdShouldReturnAll() {
        StepVerifier.create(filter.apply(null))
                .assertNext(result -> {
                    assertTrue(result.isAll());
                    assertEquals(DataScope.ALL, result.getScope());
                    assertTrue(result.getVisibleUsernames().isEmpty());
                })
                .verifyComplete();
    }

    @Test
    void emptyRolesShouldReturnPersonal() {
        AuthUser user = authUser("zhangsan", List.of());
        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(ReactiveSecurityUtils::getCurrentUser).thenReturn(Mono.just(user));

            StepVerifier.create(filter.apply(1L))
                    .assertNext(result -> {
                        assertEquals(DataScope.PERSONAL, result.getScope());
                        assertEquals(Set.of("zhangsan"), result.getVisibleUsernames());
                    })
                    .verifyComplete();
        }
    }

    @Test
    void roleLookupReturnsEmptyShouldReturnPersonal() {
        AuthUser user = authUser("zhangsan", List.of("role_a"));

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(ReactiveSecurityUtils::getCurrentUser).thenReturn(Mono.just(user));

            StepVerifier.create(filter.apply(1L))
                    .assertNext(result -> {
                        assertEquals(DataScope.PERSONAL, result.getScope());
                        assertEquals(Set.of("zhangsan"), result.getVisibleUsernames());
                    })
                    .verifyComplete();
        }
    }

    @Test
    void allScopeFromRoleDefaultShouldReturnAll() {
        AuthUser user = authUser("zhangsan", List.of("admin"));
        SysRoleEntity role = role(1L, "admin", DataScope.ALL);
        registerRole(role);
        registerAll(SysRowPermissionEntity.class, anyQuery(), List.of());

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(ReactiveSecurityUtils::getCurrentUser).thenReturn(Mono.just(user));

            StepVerifier.create(filter.apply(1L))
                    .assertNext(result -> {
                        assertTrue(result.isAll());
                        assertEquals(DataScope.ALL, result.getScope());
                    })
                    .verifyComplete();
        }
    }

    @Test
    void allScopeFromMenuConfigShouldReturnAll() {
        AuthUser user = authUser("zhangsan", List.of("admin"));
        SysRoleEntity role = role(1L, "admin", DataScope.PERSONAL);
        SysRowPermissionEntity config = menuConfig(1L, 1L, 1L, DataScope.ALL);
        registerRole(role);
        registerMenuConfig(config);

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(ReactiveSecurityUtils::getCurrentUser).thenReturn(Mono.just(user));

            StepVerifier.create(filter.apply(1L))
                    .assertNext(result -> {
                        assertTrue(result.isAll());
                        assertEquals(DataScope.ALL, result.getScope());
                    })
                    .verifyComplete();
        }
    }

    @Test
    void personalScopeShouldReturnCurrentUsernameOnly() {
        AuthUser user = authUser("zhangsan", List.of("user"));
        SysRoleEntity role = role(1L, "user", DataScope.PERSONAL);
        registerRole(role);
        registerAll(SysRowPermissionEntity.class, anyQuery(), List.of());

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(ReactiveSecurityUtils::getCurrentUser).thenReturn(Mono.just(user));

            StepVerifier.create(filter.apply(1L))
                    .assertNext(result -> {
                        assertEquals(DataScope.PERSONAL, result.getScope());
                        assertEquals(Set.of("zhangsan"), result.getVisibleUsernames());
                    })
                    .verifyComplete();
        }
    }

    @Test
    void deptOnlyScopeShouldReturnUsersInSameDept() {
        AuthUser user = authUser("zhangsan", List.of("dept_mgr"));
        SysRoleEntity role = role(1L, "dept_mgr", DataScope.DEPT_ONLY);
        SysUserEntity currentUser = user(1L, "zhangsan", 10L);
        SysUserEntity peer = user(2L, "lisi", 10L);

        registerRole(role);
        registerAll(SysRowPermissionEntity.class, anyQuery(), List.of());
        registerFirst(SysUserEntity.class, queryUsername("zhangsan"), currentUser);
        registerAll(SysUserEntity.class, queryDeptIdEquals(), List.of(currentUser, peer));
        registerAll(SysDeptEntity.class, anyQuery(), List.of());

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(ReactiveSecurityUtils::getCurrentUser).thenReturn(Mono.just(user));

            StepVerifier.create(filter.apply(1L))
                    .assertNext(result -> {
                        assertEquals(DataScope.PERSONAL, result.getScope());
                        assertEquals(Set.of("zhangsan", "lisi"), result.getVisibleUsernames());
                    })
                    .verifyComplete();
        }
    }

    @Test
    void deptAndChildrenScopeShouldIncludeChildDepts() {
        AuthUser user = authUser("zhangsan", List.of("dept_mgr"));
        SysRoleEntity role = role(1L, "dept_mgr", DataScope.DEPT_AND_CHILDREN);
        SysUserEntity currentUser = user(1L, "zhangsan", 10L);
        SysUserEntity peer = user(2L, "lisi", 10L);
        SysUserEntity childUser = user(3L, "wangwu", 11L);

        SysDeptEntity parent = dept(10L, 0L);
        SysDeptEntity child = dept(11L, 10L);
        SysDeptEntity other = dept(20L, 0L);

        registerRole(role);
        registerAll(SysRowPermissionEntity.class, anyQuery(), List.of());
        registerFirst(SysUserEntity.class, queryUsername("zhangsan"), currentUser);
        registerAll(SysUserEntity.class, queryDeptIdEquals(), List.of(currentUser, peer, childUser));
        registerAll(SysDeptEntity.class, anyQuery(), List.of(parent, child, other));

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(ReactiveSecurityUtils::getCurrentUser).thenReturn(Mono.just(user));

            StepVerifier.create(filter.apply(1L))
                    .assertNext(result -> {
                        assertEquals(DataScope.PERSONAL, result.getScope());
                        assertEquals(Set.of("zhangsan", "lisi", "wangwu"), result.getVisibleUsernames());
                    })
                    .verifyComplete();
        }
    }

    @Test
    void deptScopeWithMissingUserShouldFallbackToPersonal() {
        AuthUser user = authUser("zhangsan", List.of("dept_mgr"));
        SysRoleEntity role = role(1L, "dept_mgr", DataScope.DEPT_ONLY);

        registerRole(role);
        registerAll(SysRowPermissionEntity.class, anyQuery(), List.of());
        registerFirst(SysUserEntity.class, queryUsername("zhangsan"), null);

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(ReactiveSecurityUtils::getCurrentUser).thenReturn(Mono.just(user));

            StepVerifier.create(filter.apply(1L))
                    .assertNext(result -> {
                        assertEquals(DataScope.PERSONAL, result.getScope());
                        assertEquals(Set.of("zhangsan"), result.getVisibleUsernames());
                    })
                    .verifyComplete();
        }
    }

    @Test
    void deptScopeWithNullDeptIdShouldFallbackToPersonal() {
        AuthUser user = authUser("zhangsan", List.of("dept_mgr"));
        SysRoleEntity role = role(1L, "dept_mgr", DataScope.DEPT_ONLY);
        SysUserEntity currentUser = user(1L, "zhangsan", null);

        registerRole(role);
        registerAll(SysRowPermissionEntity.class, anyQuery(), List.of());
        registerFirst(SysUserEntity.class, queryUsername("zhangsan"), currentUser);

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(ReactiveSecurityUtils::getCurrentUser).thenReturn(Mono.just(user));

            StepVerifier.create(filter.apply(1L))
                    .assertNext(result -> {
                        assertEquals(DataScope.PERSONAL, result.getScope());
                        assertEquals(Set.of("zhangsan"), result.getVisibleUsernames());
                    })
                    .verifyComplete();
        }
    }

    @Test
    void postScopeShouldReturnUsersInSamePostAndChildren() {
        AuthUser user = authUser("zhangsan", List.of("post_mgr"));
        SysRoleEntity role = role(1L, "post_mgr", DataScope.POST);
        SysUserEntity currentUser = user(1L, "zhangsan", 10L);
        SysUserPostEntity up1 = userPost(1L, 1L, 100L);
        SysUserPostEntity up2 = userPost(2L, 2L, 100L);
        SysUserPostEntity up3 = userPost(3L, 3L, 101L);

        SysPostEntity parentPost = post(100L, 0L);
        SysPostEntity childPost = post(101L, 100L);
        SysPostEntity otherPost = post(200L, 0L);

        registerRole(role);
        registerAll(SysRowPermissionEntity.class, anyQuery(), List.of());
        registerFirst(SysUserEntity.class, queryUsername("zhangsan"), currentUser);
        registerAll(SysUserEntity.class, queryIdEquals(), List.of(currentUser, user(2L, "lisi", 10L), user(3L, "wangwu", 11L)));
        registerAll(SysUserPostEntity.class, queryUserId(1L), List.of(up1));
        registerAll(SysUserPostEntity.class, queryPostIdEquals(), List.of(up1, up2, up3));
        registerAll(SysPostEntity.class, anyQuery(), List.of(parentPost, childPost, otherPost));

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(ReactiveSecurityUtils::getCurrentUser).thenReturn(Mono.just(user));

            StepVerifier.create(filter.apply(1L))
                    .assertNext(result -> {
                        assertEquals(DataScope.PERSONAL, result.getScope());
                        assertEquals(Set.of("zhangsan", "lisi", "wangwu"), result.getVisibleUsernames());
                    })
                    .verifyComplete();
        }
    }

    @Test
    void postScopeWithNoPostsShouldFallbackToPersonal() {
        AuthUser user = authUser("zhangsan", List.of("post_mgr"));
        SysRoleEntity role = role(1L, "post_mgr", DataScope.POST);
        SysUserEntity currentUser = user(1L, "zhangsan", 10L);

        registerRole(role);
        registerAll(SysRowPermissionEntity.class, anyQuery(), List.of());
        registerFirst(SysUserEntity.class, queryUsername("zhangsan"), currentUser);
        registerAll(SysUserPostEntity.class, queryUserId(1L), List.of());

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(ReactiveSecurityUtils::getCurrentUser).thenReturn(Mono.just(user));

            StepVerifier.create(filter.apply(1L))
                    .assertNext(result -> {
                        assertEquals(DataScope.PERSONAL, result.getScope());
                        assertEquals(Set.of("zhangsan"), result.getVisibleUsernames());
                    })
                    .verifyComplete();
        }
    }

    @Test
    void multipleRolesShouldPickMinScopeFromDefaults() {
        AuthUser user = authUser("zhangsan", List.of("role_a", "role_b"));
        SysRoleEntity roleA = role(1L, "role_a", DataScope.PERSONAL);   // value 3
        SysRoleEntity roleB = role(2L, "role_b", DataScope.ALL);        // value 0
        registerRole(roleA);
        registerRole(roleB);
        registerAll(SysRowPermissionEntity.class, anyQuery(), List.of());

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(ReactiveSecurityUtils::getCurrentUser).thenReturn(Mono.just(user));

            StepVerifier.create(filter.apply(1L))
                    .assertNext(result -> {
                        assertTrue(result.isAll());
                        assertEquals(DataScope.ALL, result.getScope());
                    })
                    .verifyComplete();
        }
    }

    @Test
    void multipleMenuConfigsShouldPickMinScope() {
        AuthUser user = authUser("zhangsan", List.of("admin"));
        SysRoleEntity role = role(1L, "admin", DataScope.ALL);
        SysRowPermissionEntity cfg1 = menuConfig(1L, 1L, 1L, DataScope.PERSONAL); // value 3
        SysRowPermissionEntity cfg2 = menuConfig(2L, 1L, 1L, DataScope.ALL);      // value 0
        registerRole(role);
        registerMenuConfig(cfg1);
        registerMenuConfig(cfg2);

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(ReactiveSecurityUtils::getCurrentUser).thenReturn(Mono.just(user));

            StepVerifier.create(filter.apply(1L))
                    .assertNext(result -> {
                        assertTrue(result.isAll());
                        assertEquals(DataScope.ALL, result.getScope());
                    })
                    .verifyComplete();
        }
    }

    @Test
    void nullDataScopeOnRoleShouldBeIgnored() {
        AuthUser user = authUser("zhangsan", List.of("admin"));
        SysRoleEntity role = new SysRoleEntity();
        role.setId(1L);
        role.setRoleCode("admin");
        role.setDataScope(null);
        role.setDeleteStatus(false);
        registerRole(role);
        registerAll(SysRowPermissionEntity.class, anyQuery(), List.of());

        try (MockedStatic<ReactiveSecurityUtils> mocked = mockStatic(ReactiveSecurityUtils.class)) {
            mocked.when(ReactiveSecurityUtils::getCurrentUser).thenReturn(Mono.just(user));

            StepVerifier.create(filter.apply(1L))
                    .assertNext(result -> assertEquals(DataScope.ALL, result.getScope()))
                    .verifyComplete();
        }
    }

    // ---------------- entity builders ----------------

    private AuthUser authUser(String username, List<String> roles) {
        AuthUser user = new AuthUser();
        user.setUsername(username);
        user.setRoles(roles);
        return user;
    }

    private SysRoleEntity role(Long id, String code, DataScope scope) {
        SysRoleEntity role = new SysRoleEntity();
        role.setId(id);
        role.setRoleCode(code);
        role.setDataScope(scope.value());
        role.setDeleteStatus(false);
        return role;
    }

    private SysRowPermissionEntity menuConfig(Long id, Long roleId, Long menuId, DataScope scope) {
        SysRowPermissionEntity cfg = new SysRowPermissionEntity();
        cfg.setId(id);
        cfg.setRoleId(roleId);
        cfg.setMenuId(menuId);
        cfg.setScopeType(scope.value());
        cfg.setDeleteStatus(false);
        return cfg;
    }

    private SysUserEntity user(Long id, String username, Long deptId) {
        SysUserEntity user = new SysUserEntity();
        user.setId(id);
        user.setUsername(username);
        user.setDeptId(deptId);
        user.setDeleteStatus(false);
        return user;
    }

    private SysDeptEntity dept(Long id, Long parentId) {
        SysDeptEntity dept = new SysDeptEntity();
        dept.setId(id);
        dept.setParentId(parentId);
        dept.setDeleteStatus(false);
        return dept;
    }

    private SysPostEntity post(Long id, Long parentId) {
        SysPostEntity post = new SysPostEntity();
        post.setId(id);
        post.setParentId(parentId);
        post.setDeleteStatus(false);
        return post;
    }

    private SysUserPostEntity userPost(Long id, Long userId, Long postId) {
        SysUserPostEntity up = new SysUserPostEntity();
        up.setId(id);
        up.setUserId(userId);
        up.setPostId(postId);
        up.setDeleteStatus(false);
        return up;
    }

    // ---------------- stubbing registry ----------------

    private <T> void registerAll(Class<T> entityClass, Predicate<Query> matcher, List<T> results) {
        stubRegistry.computeIfAbsent(entityClass, k -> new ArrayList<>())
                .add(new StubConfig<>(matcher, Flux.fromIterable(results), null));
    }

    private <T> void registerFirst(Class<T> entityClass, Predicate<Query> matcher, T result) {
        stubRegistry.computeIfAbsent(entityClass, k -> new ArrayList<>())
                .add(new StubConfig<>(matcher, null, result == null ? Mono.empty() : Mono.just(result)));
    }

    private void registerRole(SysRoleEntity role) {
        roleByCode.put(role.getRoleCode(), role);
    }

    private void registerMenuConfig(SysRowPermissionEntity config) {
        configsByRoleId.computeIfAbsent(config.getRoleId(), k -> new ArrayList<>()).add(config);
    }

    private record StubConfig<T>(Predicate<Query> matcher, Flux<T> allResult, Mono<T> firstResult) {
    }

    // ---------------- query matchers ----------------

    private Predicate<Query> anyQuery() {
        return q -> true;
    }

    private Predicate<Query> queryUsername(String username) {
        return q -> criteriaContains(q, "username = '" + username + "'");
    }

    private Predicate<Query> queryUserId(Long userId) {
        return q -> criteriaContains(q, "userId = " + userId);
    }

    private Predicate<Query> queryDeptIdEquals() {
        return q -> criteriaContains(q, "deptId = ");
    }

    private Predicate<Query> queryPostIdEquals() {
        return q -> criteriaContains(q, "postId = ");
    }

    private Predicate<Query> queryIdEquals() {
        return q -> criteriaContains(q, "id = ");
    }

    private boolean criteriaContains(Query query, String text) {
        if (query == null || query.getCriteria().isEmpty()) {
            return false;
        }
        String criteria = query.getCriteria().map(CriteriaDefinition::toString).orElse("");
        return criteria.contains("deleteStatus = 'false'") && criteria.contains(text);
    }
}
