package com.springddd.application.service.common;

import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.domain.role.DataScope;
import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import com.springddd.infrastructure.persistence.entity.SysPostEntity;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import com.springddd.infrastructure.persistence.entity.SysRoleMenuDataScopeEntity;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import com.springddd.infrastructure.persistence.entity.SysUserPostEntity;
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

    public Mono<DataScopeResult> apply(Long menuId) {
        if (menuId == null) {
            return Mono.just(DataScopeResult.all());
        }
        return ReactiveSecurityUtils.getCurrentUser()
                .flatMap(authUser -> resolveDataScope(menuId, authUser.getRoles())
                        .flatMap(scope -> {
                            if (scope == DataScope.ALL) {
                                return Mono.just(DataScopeResult.all());
                            }
                            return resolveVisibleUsernames(scope, authUser)
                                    .map(DataScopeResult::new);
                        }));
    }

    private Mono<DataScope> resolveDataScope(Long menuId, List<String> roleCodes) {
        if (roleCodes == null || roleCodes.isEmpty()) {
            return Mono.just(DataScope.PERSONAL);
        }
        return r2dbcEntityTemplate.select(SysRoleEntity.class)
                .matching(Query.query(Criteria.where("roleCode").in(roleCodes).and("deleteStatus").is(false)))
                .all()
                .collectList()
                .flatMap(roles -> {
                    if (roles.isEmpty()) {
                        return Mono.just(DataScope.PERSONAL);
                    }
                    List<Long> roleIds = roles.stream().map(SysRoleEntity::getId).toList();
                    return r2dbcEntityTemplate.select(SysRoleMenuDataScopeEntity.class)
                            .matching(Query.query(Criteria.where("roleId").in(roleIds).and("menuId").is(menuId).and("deleteStatus").is(false)))
                            .all()
                            .collectList()
                            .map(configs -> {
                                if (configs.isEmpty()) {
                                    // fallback to role default data scope
                                    return roles.stream()
                                            .map(SysRoleEntity::getDataScope)
                                            .filter(Objects::nonNull)
                                            .map(DataScope::of)
                                            .filter(Objects::nonNull)
                                            .min(DataScopeQueryFilter::compareScope)
                                            .orElse(DataScope.ALL);
                                }
                                return configs.stream()
                                        .map(SysRoleMenuDataScopeEntity::getDataScope)
                                        .map(DataScope::of)
                                        .filter(Objects::nonNull)
                                        .min(DataScopeQueryFilter::compareScope)
                                        .orElse(DataScope.ALL);
                            });
                });
    }

    private Mono<Set<String>> resolveVisibleUsernames(DataScope scope, AuthUser authUser) {
        String username = authUser.getUsername();
        return switch (scope) {
            case ALL -> Mono.just(Collections.emptySet());
            case PERSONAL -> Mono.just(Set.of(username));
            case DEPT_ONLY, DEPT_AND_CHILDREN -> resolveDeptUsernames(scope, username);
            case POST -> resolvePostUsernames(username);
        };
    }

    private Mono<Set<String>> resolveDeptUsernames(DataScope scope, String username) {
        return r2dbcEntityTemplate.select(SysUserEntity.class)
                .matching(Query.query(Criteria.where("username").is(username).and("deleteStatus").is(false)))
                .first()
                .flatMap(user -> {
                    Long deptId = user.getDeptId();
                    if (deptId == null) {
                        return Mono.just(Set.of(username));
                    }
                    return r2dbcEntityTemplate.select(SysDeptEntity.class)
                            .matching(Query.query(Criteria.where("deleteStatus").is(false)))
                            .all()
                            .collectList()
                            .flatMap(depts -> {
                                Set<Long> deptIds = new HashSet<>();
                                deptIds.add(deptId);
                                if (scope == DataScope.DEPT_AND_CHILDREN) {
                                    deptIds.addAll(findChildrenDeptIds(deptId, depts));
                                }
                                return r2dbcEntityTemplate.select(SysUserEntity.class)
                                        .matching(Query.query(Criteria.where("deptId").in(deptIds).and("deleteStatus").is(false)))
                                        .all()
                                        .map(SysUserEntity::getUsername)
                                        .collect(Collectors.toSet());
                            });
                })
                .switchIfEmpty(Mono.just(Set.of(username)));
    }

    private Mono<Set<String>> resolvePostUsernames(String username) {
        return r2dbcEntityTemplate.select(SysUserEntity.class)
                .matching(Query.query(Criteria.where("username").is(username).and("deleteStatus").is(false)))
                .first()
                .flatMap(user -> r2dbcEntityTemplate.select(SysUserPostEntity.class)
                        .matching(Query.query(Criteria.where("userId").is(user.getId()).and("deleteStatus").is(false)))
                        .all()
                        .map(SysUserPostEntity::getPostId)
                        .collectList()
                        .flatMap(postIds -> {
                            if (postIds.isEmpty()) {
                                return Mono.just(Set.of(username));
                            }
                            return r2dbcEntityTemplate.select(SysPostEntity.class)
                                    .matching(Query.query(Criteria.where("deleteStatus").is(false)))
                                    .all()
                                    .collectList()
                                    .flatMap(posts -> {
                                        Set<Long> allPostIds = new HashSet<>(postIds);
                                        for (Long postId : postIds) {
                                            allPostIds.addAll(findChildrenPostIds(postId, posts));
                                        }
                                        return r2dbcEntityTemplate.select(SysUserPostEntity.class)
                                                .matching(Query.query(Criteria.where("postId").in(allPostIds).and("deleteStatus").is(false)))
                                                .all()
                                                .map(SysUserPostEntity::getUserId)
                                                .collect(Collectors.toSet());
                                    })
                                    .flatMap(userIds -> r2dbcEntityTemplate.select(SysUserEntity.class)
                                            .matching(Query.query(Criteria.where("id").in(userIds).and("deleteStatus").is(false)))
                                            .all()
                                            .map(SysUserEntity::getUsername)
                                            .collect(Collectors.toSet()));
                        }))
                .switchIfEmpty(Mono.just(Set.of(username)));
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

    private static int compareScope(DataScope a, DataScope b) {
        return Integer.compare(a.value(), b.value());
    }
}
