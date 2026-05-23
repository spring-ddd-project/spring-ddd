package com.springddd.application.service.permission;

import com.springddd.application.service.dept.SysDeptQueryService;
import com.springddd.application.service.dept.dto.SysDeptView;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.role.DataPermission;
import com.springddd.domain.role.RowScope;
import com.springddd.domain.user.UserId;
import com.springddd.infrastructure.cache.util.CacheProcessor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import reactor.util.context.Context;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataScopeCriteriaBuilderTest {

    @Mock
    private SysDeptQueryService sysDeptQueryService;

    @Mock
    private EntityMetadataScanner entityMetadataScanner;

    @Mock
    private CacheProcessor cacheProcessor;

    @InjectMocks
    private DataScopeCriteriaBuilder builder;

    private Context withAuthUser(AuthUser user) {
        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());
        return ReactiveSecurityContextHolder.withAuthentication(auth);
    }

    private AuthUser userWithDataPermission(Long deptId, String username, DataPermission dp) {
        AuthUser user = new AuthUser();
        user.setUserId(new UserId(1L));
        user.setUsername(username);
        user.setDeptId(deptId);
        user.setDataPermission(dp);
        return user;
    }

    @Test
    @DisplayName("当数据权限范围是全部时，应返回原始 Criteria")
    void apply_whenDataScopeIsAll_shouldReturnOriginalCriteria() {
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(null, null, 1, null));
        Criteria criteria = Criteria.empty();

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> assertThat(result).isSameAs(criteria))
                .verifyComplete();
    }

    @Test
    @DisplayName("当数据权限范围是部门时，应添加 deptId 条件")
    void apply_whenDataScopeIsDept_shouldAddDeptIdCondition() {
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(null, null, 2, null));
        Criteria criteria = Criteria.empty();

        when(entityMetadataScanner.hasField("sys_user", "deptId")).thenReturn(true);

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> {
                    assertThat(result).isNotSameAs(criteria);
                    assertThat(result.toString()).contains("deptId");
                    assertThat(result.toString()).contains("10");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("当数据权限范围是部门但实体没有 deptId 字段时，应返回原始 Criteria")
    void apply_whenDataScopeIsDeptAndEntityHasNoDeptId_shouldReturnOriginal() {
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(null, null, 2, null));
        Criteria criteria = Criteria.empty();

        when(entityMetadataScanner.hasField("sys_user", "deptId")).thenReturn(false);

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> assertThat(result).isSameAs(criteria))
                .verifyComplete();
    }

    @Test
    @DisplayName("当数据权限范围是本人时，应添加 createBy 条件")
    void apply_whenDataScopeIsSelf_shouldAddCreateByCondition() {
        AuthUser user = userWithDataPermission(10L, "admin2",
                new DataPermission(null, null, 4, null));
        Criteria criteria = Criteria.empty();

        when(entityMetadataScanner.hasField("sys_user", "createBy")).thenReturn(true);

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> {
                    assertThat(result).isNotSameAs(criteria);
                    assertThat(result.toString()).contains("createBy");
                    assertThat(result.toString()).contains("admin2");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("当数据权限范围是部门及子部门时，应添加 IN 条件")
    void apply_whenDataScopeIsDeptAndSub_shouldAddInCondition() {
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(null, null, 3, null));
        Criteria criteria = Criteria.empty();

        SysDeptView child1 = new SysDeptView();
        child1.setId(11L);
        child1.setParentId(10L);

        SysDeptView child2 = new SysDeptView();
        child2.setId(12L);
        child2.setParentId(11L);

        when(entityMetadataScanner.hasField("sys_user", "deptId")).thenReturn(true);
        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.empty());
        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(List.of(child1, child2)));
        when(cacheProcessor.setCache(anyString(), anyList(), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> {
                    assertThat(result).isNotSameAs(criteria);
                    assertThat(result.toString()).contains("deptId");
                    assertThat(result.toString()).contains("IN");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("当用户是超级管理员时，应直接放行")
    void apply_whenSuperAdmin_shouldBypass() {
        AuthUser user = userWithDataPermission(10L, "admin", null);
        Criteria criteria = Criteria.empty();

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> assertThat(result).isSameAs(criteria))
                .verifyComplete();
    }

    @Test
    @DisplayName("当数据权限范围是自定义且指定了 deptIds 时，应添加 IN 条件")
    void apply_whenDataScopeIsCustomWithDeptIds_shouldAddInCondition() {
        RowScope rowScope = new RowScope(List.of(1L, 2L, 3L), null, null, null);
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(rowScope, null, 5, null));
        Criteria criteria = Criteria.empty();

        when(entityMetadataScanner.hasField("sys_user", "deptId")).thenReturn(true);

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> {
                    assertThat(result).isNotSameAs(criteria);
                    assertThat(result.toString()).contains("deptId");
                    assertThat(result.toString()).contains("IN");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("当用户没有数据权限时，应返回原始 Criteria")
    void apply_whenNoDataPermission_shouldReturnOriginal() {
        AuthUser user = userWithDataPermission(10L, "user1", null);
        Criteria criteria = Criteria.empty();

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> assertThat(result).isSameAs(criteria))
                .verifyComplete();
    }

    @Test
    @DisplayName("evictDeptTreeCache 应通过 CacheProcessor 删除部门树缓存")
    void evictDeptTreeCache_shouldCallDeleteCache() {
        when(cacheProcessor.deleteCache("datascope:dept:tree")).thenReturn(Mono.empty());

        StepVerifier.create(builder.evictDeptTreeCache())
                .verifyComplete();

        verify(cacheProcessor).deleteCache("datascope:dept:tree");
    }

    @Test
    @DisplayName("当 criteria 为 null 时，应创建空 Criteria 并继续应用数据权限")
    void apply_whenCriteriaIsNull_shouldCreateEmptyCriteria() {
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(null, null, 2, null));

        when(entityMetadataScanner.hasField("sys_user", "deptId")).thenReturn(true);

        StepVerifier.create(
                        builder.apply(null, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> {
                    assertThat(result).isNotNull();
                    assertThat(result.toString()).contains("deptId");
                    assertThat(result.toString()).contains("10");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("当 entityCode 为空时，应返回原始 Criteria")
    void apply_whenEntityCodeIsEmpty_shouldReturnOriginal() {
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(null, null, 2, null));
        Criteria criteria = Criteria.empty();

        StepVerifier.create(
                        builder.apply(criteria, "")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> assertThat(result).isSameAs(criteria))
                .verifyComplete();
    }

    @Test
    @DisplayName("当 dataScope 为未知值时，应返回原始 Criteria")
    void apply_whenDataScopeIsDefault_shouldReturnOriginal() {
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(null, null, 99, null));
        Criteria criteria = Criteria.empty();

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> assertThat(result).isSameAs(criteria))
                .verifyComplete();
    }

    @Test
    @DisplayName("当用户 deptId 为 null 且数据权限为部门时，应返回原始 Criteria")
    void applyDeptScope_whenDeptIdIsNull_shouldReturnOriginal() {
        AuthUser user = userWithDataPermission(null, "user1",
                new DataPermission(null, null, 2, null));
        Criteria criteria = Criteria.empty();

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> assertThat(result).isSameAs(criteria))
                .verifyComplete();
    }

    @Test
    @DisplayName("当用户用户名为空且数据权限为本人时，应返回原始 Criteria")
    void applySelfScope_whenUsernameIsEmpty_shouldReturnOriginal() {
        AuthUser user = userWithDataPermission(10L, "",
                new DataPermission(null, null, 4, null));
        Criteria criteria = Criteria.empty();

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> assertThat(result).isSameAs(criteria))
                .verifyComplete();
    }

    @Test
    @DisplayName("当实体没有 createBy 字段且数据权限为本人时，应返回原始 Criteria")
    void applySelfScope_whenEntityHasNoCreateBy_shouldReturnOriginal() {
        AuthUser user = userWithDataPermission(10L, "admin2",
                new DataPermission(null, null, 4, null));
        Criteria criteria = Criteria.empty();

        when(entityMetadataScanner.hasField("sys_user", "createBy")).thenReturn(false);

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> assertThat(result).isSameAs(criteria))
                .verifyComplete();
    }

    @Test
    @DisplayName("当自定义范围 rowScope 为 null 时，应返回原始 Criteria")
    void applyCustomScope_whenRowScopeIsNull_shouldReturnOriginal() {
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(null, null, 5, null));
        Criteria criteria = Criteria.empty();

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> assertThat(result).isSameAs(criteria))
                .verifyComplete();
    }

    @Test
    @DisplayName("当自定义范围 self 为 true 时，应添加 createBy 条件")
    void applyCustomScope_whenSelfIsTrue_shouldAddCreateByCondition() {
        RowScope rowScope = new RowScope(null, null, null, true);
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(rowScope, null, 5, null));
        Criteria criteria = Criteria.empty();

        when(entityMetadataScanner.hasField("sys_user", "createBy")).thenReturn(true);

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> {
                    assertThat(result).isNotSameAs(criteria);
                    assertThat(result.toString()).contains("createBy");
                    assertThat(result.toString()).contains("user1");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("当自定义范围包含 userIds 时，应记录日志并返回原始 Criteria")
    void applyCustomScope_whenUserIdsPresent_shouldLogDebug() {
        RowScope rowScope = new RowScope(null, null, List.of(1L), null);
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(rowScope, null, 5, null));
        Criteria criteria = Criteria.empty();

        when(entityMetadataScanner.hasField("sys_user", "createBy")).thenReturn(true);

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> assertThat(result).isSameAs(criteria))
                .verifyComplete();
    }

    @Test
    @DisplayName("当自定义范围没有任何匹配条件时，应返回原始 Criteria")
    void applyCustomScope_whenNoConditionsMatch_shouldReturnOriginal() {
        RowScope rowScope = new RowScope(null, List.of(1L), null, null);
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(rowScope, null, 5, null));
        Criteria criteria = Criteria.empty();

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> assertThat(result).isSameAs(criteria))
                .verifyComplete();
    }

    @Test
    @DisplayName("当自定义范围同时包含 deptIds 和 self 时，应同时添加两个条件")
    void applyCustomScope_whenDeptAndSelfBothTrue_shouldAddBothConditions() {
        RowScope rowScope = new RowScope(List.of(1L, 2L), null, null, true);
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(rowScope, null, 5, null));
        Criteria criteria = Criteria.empty();

        when(entityMetadataScanner.hasField("sys_user", "deptId")).thenReturn(true);
        when(entityMetadataScanner.hasField("sys_user", "createBy")).thenReturn(true);

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> {
                    assertThat(result).isNotSameAs(criteria);
                    assertThat(result.toString()).contains("deptId");
                    assertThat(result.toString()).contains("IN");
                    assertThat(result.toString()).contains("createBy");
                    assertThat(result.toString()).contains("user1");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("当数据权限为部门及子部门且缓存命中时，应使用缓存的部门 ID")
    void apply_whenDataScopeIsDeptAndSubAndCacheHit_shouldUseCachedDeptIds() {
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(null, null, 3, null));
        Criteria criteria = Criteria.empty();

        java.util.ArrayList<Long> cachedIds = new java.util.ArrayList<>();
        cachedIds.add(10L);
        cachedIds.add(11L);
        cachedIds.add(12L);

        when(entityMetadataScanner.hasField("sys_user", "deptId")).thenReturn(true);
        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.just(cachedIds));

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> {
                    assertThat(result).isNotSameAs(criteria);
                    assertThat(result.toString()).contains("deptId");
                    assertThat(result.toString()).contains("IN");
                    assertThat(result.toString()).contains("10");
                })
                .verifyComplete();

        verify(sysDeptQueryService, never()).queryAllDept();
        verify(cacheProcessor, never()).setCache(anyString(), anyList(), any(Duration.class));
    }

    @Test
    @DisplayName("当数据权限为部门及子部门且缓存返回 null 时应回退到 DB 查询")
    void apply_whenDataScopeIsDeptAndSubAndCacheReturnsNull_shouldFallbackToDb() {
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(null, null, 3, null));
        Criteria criteria = Criteria.empty();

        SysDeptView child1 = new SysDeptView();
        child1.setId(11L);
        child1.setParentId(10L);

        when(entityMetadataScanner.hasField("sys_user", "deptId")).thenReturn(true);
        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.empty());
        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(List.of(child1)));
        when(cacheProcessor.setCache(anyString(), anyList(), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> {
                    assertThat(result).isNotSameAs(criteria);
                    assertThat(result.toString()).contains("deptId");
                    assertThat(result.toString()).contains("IN");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("当数据权限为部门及子部门且缓存返回非法类型时应回退到 DB 查询")
    void apply_whenDataScopeIsDeptAndSubAndCacheReturnsInvalidType_shouldFallbackToDb() {
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(null, null, 3, null));
        Criteria criteria = Criteria.empty();

        SysDeptView child1 = new SysDeptView();
        child1.setId(11L);
        child1.setParentId(10L);

        @SuppressWarnings("unchecked")
        List<Long> badList = org.mockito.Mockito.mock(List.class);
        when(badList.iterator()).thenThrow(new RuntimeException("bad list"));

        when(entityMetadataScanner.hasField("sys_user", "deptId")).thenReturn(true);
        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.just(badList));
        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(List.of(child1)));
        when(cacheProcessor.setCache(anyString(), anyList(), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> {
                    assertThat(result).isNotSameAs(criteria);
                    assertThat(result.toString()).contains("deptId");
                    assertThat(result.toString()).contains("IN");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("当数据权限为部门及子部门且存在多级嵌套部门时，应收集所有后代部门")
    void apply_whenDataScopeIsDeptAndSubWithNestedDepts_shouldCollectAllDescendants() {
        AuthUser user = userWithDataPermission(10L, "user1",
                new DataPermission(null, null, 3, null));
        Criteria criteria = Criteria.empty();

        SysDeptView child1 = new SysDeptView();
        child1.setId(11L);
        child1.setParentId(10L);

        SysDeptView child2 = new SysDeptView();
        child2.setId(12L);
        child2.setParentId(11L);

        SysDeptView child3 = new SysDeptView();
        child3.setId(13L);
        child3.setParentId(12L);

        when(entityMetadataScanner.hasField("sys_user", "deptId")).thenReturn(true);
        when(cacheProcessor.getCache(anyString(), eq(List.class))).thenReturn(Mono.empty());
        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(List.of(child1, child2, child3)));
        when(cacheProcessor.setCache(anyString(), anyList(), any(Duration.class))).thenReturn(Mono.just(true));

        StepVerifier.create(
                        builder.apply(criteria, "sys_user")
                                .contextWrite(withAuthUser(user))
                )
                .assertNext(result -> {
                    assertThat(result).isNotSameAs(criteria);
                    assertThat(result.toString()).contains("deptId");
                    assertThat(result.toString()).contains("IN");
                    assertThat(result.toString()).contains("10");
                    assertThat(result.toString()).contains("11");
                    assertThat(result.toString()).contains("12");
                    assertThat(result.toString()).contains("13");
                })
                .verifyComplete();
    }
}
