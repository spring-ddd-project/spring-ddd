package com.springddd.application.service.common;

import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import com.springddd.infrastructure.persistence.entity.SysPostEntity;
import com.springddd.infrastructure.persistence.entity.SysRowPermissionEntity;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import com.springddd.infrastructure.persistence.entity.SysUserPostEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
import com.springddd.infrastructure.persistence.r2dbc.SysRowPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DataScopeQueryFilter {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;
    private final SysRoleRepository sysRoleRepository;
    private final SysRowPermissionRepository sysRowPermissionRepository;

    public Mono<DataScopeResult> apply(Long menuId) {
        if (menuId == null) {
            return Mono.just(DataScopeResult.all());
        }
        return ReactiveSecurityUtils.getCurrentUser()
                .flatMap(authUser -> resolveDataScopeResult(menuId, authUser));
    }

    private Mono<DataScopeResult> resolveDataScopeResult(Long menuId, AuthUser authUser) {
        List<String> roleCodes = authUser.getRoles();
        if (roleCodes == null || roleCodes.isEmpty()) {
            return Mono.just(new DataScopeResult(Set.of(authUser.getUsername())));
        }
        return Flux.fromIterable(roleCodes)
                .flatMap(sysRoleRepository::findByRoleCodeAndDeleteStatusFalse)
                .map(SysRoleEntity::getId)
                .collectList()
                .flatMap(roleIds -> {
                    if (roleIds.isEmpty()) {
                        return Mono.just(Set.of(authUser.getUsername()));
                    }
                    return Flux.fromIterable(roleIds)
                            .flatMap(roleId -> sysRowPermissionRepository.findByRoleIdAndMenuIdAndDeleteStatusFalse(roleId, menuId))
                            .collectList()
                            .flatMap(rules -> {
                                if (rules.isEmpty()) {
                                    return Mono.just(Set.of(authUser.getUsername()));
                                }
                                boolean hasAll = rules.stream().anyMatch(rule -> rule.getScopeType() != null && rule.getScopeType() == 0);
                                if (hasAll) {
                                    return Mono.just(new HashSet<String>());
                                }
                                return resolveVisibleUsernames(rules, authUser);
                            });
                })
                .map(DataScopeResult::new);
    }

    private Mono<Set<String>> resolveVisibleUsernames(List<SysRowPermissionEntity> rules, AuthUser authUser) {
        Set<String> usernames = new HashSet<>();
        Set<Long> deptOnlyIds = new HashSet<>();
        Set<Long> deptAndChildIds = new HashSet<>();
        Set<Long> postIds = new HashSet<>();
        Set<Long> userIds = new HashSet<>();

        for (SysRowPermissionEntity rule : rules) {
            Integer scopeType = rule.getScopeType();
            Long targetId = rule.getTargetId();
            if (scopeType == null) {
                continue;
            }
            switch (scopeType) {
                case 1 -> {
                    if (targetId != null) {
                        deptOnlyIds.add(targetId);
                    }
                }
                case 2 -> {
                    if (targetId != null) {
                        deptAndChildIds.add(targetId);
                    }
                }
                case 3 -> {
                    if (targetId != null) {
                        postIds.add(targetId);
                    }
                }
                case 4 -> {
                    if (targetId != null) {
                        userIds.add(targetId);
                    } else {
                        usernames.add(authUser.getUsername());
                    }
                }
                default -> {
                    // ignore
                }
            }
        }

        Mono<Set<String>> deptUsersMono = resolveDeptUsernames(deptOnlyIds, deptAndChildIds);
        Mono<Set<String>> postUsersMono = resolvePostUsernames(postIds);
        Mono<Set<String>> userUsersMono = resolveUserUsernames(userIds);

        return Mono.zip(deptUsersMono, postUsersMono, userUsersMono)
                .map(tuple -> {
                    Set<String> result = new HashSet<>(usernames);
                    result.addAll(tuple.getT1());
                    result.addAll(tuple.getT2());
                    result.addAll(tuple.getT3());
                    if (result.isEmpty()) {
                        result.add(authUser.getUsername());
                    }
                    return result;
                });
    }

    private Mono<Set<String>> resolveDeptUsernames(Set<Long> deptOnlyIds, Set<Long> deptAndChildIds) {
        if (deptOnlyIds.isEmpty() && deptAndChildIds.isEmpty()) {
            return Mono.just(Collections.emptySet());
        }
        return r2dbcEntityTemplate.select(SysDeptEntity.class)
                .matching(Query.query(Criteria.where("delete_status").is(false)))
                .all()
                .collectList()
                .flatMap(allDepts -> {
                    Set<Long> targetDeptIds = new HashSet<>(deptOnlyIds);
                    for (Long parentId : deptAndChildIds) {
                        targetDeptIds.add(parentId);
                        targetDeptIds.addAll(findChildrenDeptIds(parentId, allDepts));
                    }
                    if (targetDeptIds.isEmpty()) {
                        return Mono.just(Collections.emptySet());
                    }
                    return Flux.fromIterable(targetDeptIds)
                            .flatMap(deptId -> r2dbcEntityTemplate.select(SysUserEntity.class)
                                    .matching(Query.query(Criteria.where("dept_id").is(deptId).and("delete_status").is(false)))
                                    .all())
                            .map(SysUserEntity::getUsername)
                            .collect(Collectors.toSet());
                });
    }

    private Mono<Set<String>> resolvePostUsernames(Set<Long> postIds) {
        if (postIds.isEmpty()) {
            return Mono.just(Collections.emptySet());
        }
        return r2dbcEntityTemplate.select(SysPostEntity.class)
                .matching(Query.query(Criteria.where("delete_status").is(false)))
                .all()
                .collectList()
                .flatMap(allPosts -> {
                    Set<Long> targetPostIds = new HashSet<>(postIds);
                    for (Long parentId : postIds) {
                        targetPostIds.addAll(findChildrenPostIds(parentId, allPosts));
                    }
                    if (targetPostIds.isEmpty()) {
                        return Mono.just(Collections.emptySet());
                    }
                    return Flux.fromIterable(targetPostIds)
                            .flatMap(postId -> r2dbcEntityTemplate.select(SysUserPostEntity.class)
                                    .matching(Query.query(Criteria.where("post_id").is(postId).and("delete_status").is(false)))
                                    .all())
                            .map(SysUserPostEntity::getUserId)
                            .collect(Collectors.toSet())
                            .flatMap(userIds -> {
                                if (userIds.isEmpty()) {
                                    return Mono.just(Collections.emptySet());
                                }
                                return Flux.fromIterable(userIds)
                                        .flatMap(userId -> r2dbcEntityTemplate.select(SysUserEntity.class)
                                                .matching(Query.query(Criteria.where("id").is(userId).and("delete_status").is(false)))
                                                .all())
                                        .map(SysUserEntity::getUsername)
                                        .collect(Collectors.toSet());
                            });
                });
    }

    private Mono<Set<String>> resolveUserUsernames(Set<Long> userIds) {
        if (userIds.isEmpty()) {
            return Mono.just(Collections.emptySet());
        }
        return Flux.fromIterable(userIds)
                .flatMap(userId -> r2dbcEntityTemplate.select(SysUserEntity.class)
                        .matching(Query.query(Criteria.where("id").is(userId).and("delete_status").is(false)))
                        .all())
                .map(SysUserEntity::getUsername)
                .collect(Collectors.toSet());
    }

    private Set<Long> findChildrenDeptIds(Long parentId, List<SysDeptEntity> allDepts) {
        Set<Long> result = new HashSet<>();
        for (SysDeptEntity dept : allDepts) {
            if (parentId.equals(dept.getParentId())) {
                result.add(dept.getId());
                result.addAll(findChildrenDeptIds(dept.getId(), allDepts));
            }
        }
        return result;
    }

    private Set<Long> findChildrenPostIds(Long parentId, List<SysPostEntity> allPosts) {
        Set<Long> result = new HashSet<>();
        for (SysPostEntity post : allPosts) {
            if (parentId.equals(post.getParentId())) {
                result.add(post.getId());
                result.addAll(findChildrenPostIds(post.getId(), allPosts));
            }
        }
        return result;
    }

    /**
     * 绕过 asyncer-r2dbc-mysql 驱动对 Criteria#in(Collection) 误用 executeMany 的问题。
     * 将用户名集合转换成 (field = u1 OR field = u2 OR ...) 的 Criteria 链。
     * 空集合返回 field IS NULL，保证匹配不到任何记录。
     */
    public static Criteria createByInCriteria(String field, Set<String> usernames) {
        if (usernames == null || usernames.isEmpty()) {
            return Criteria.where(field).is(null);
        }
        Iterator<String> iterator = usernames.iterator();
        Criteria criteria = Criteria.where(field).is(iterator.next());
        while (iterator.hasNext()) {
            criteria = criteria.or(field).is(iterator.next());
        }
        return criteria;
    }
}
