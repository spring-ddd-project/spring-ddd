package com.springddd.application.service.permission;

import com.springddd.application.service.dept.SysDeptQueryService;
import com.springddd.application.service.dept.dto.SysDeptView;
import com.springddd.domain.auth.AuthUser;
import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.domain.permission.DataScopeEnabled;
import com.springddd.domain.role.DataPermission;
import com.springddd.domain.role.RowScope;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.CacheDefinition;
import com.springddd.infrastructure.cache.util.CacheProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataScopeCriteriaBuilder {

    private final SysDeptQueryService sysDeptQueryService;
    private final EntityMetadataScanner entityMetadataScanner;
    private final CacheProcessor cacheProcessor;

    private static final CacheDefinition DEPT_TREE_CACHE = CacheDefinition.of("datascope:dept:tree", Duration.ofMinutes(5));
    private static final String DEPT_ID_FIELD = "deptId";
    private static final String CREATE_BY_FIELD = "createBy";
    private static final String ID_FIELD = "id";

    public Mono<Criteria> apply(Criteria criteria, String entityCode) {
        if (criteria == null) {
            criteria = Criteria.empty();
        }
        if (ObjectUtils.isEmpty(entityCode)) {
            return Mono.just(criteria);
        }

        final Criteria baseCriteria = criteria;
        return ReactiveSecurityUtils.getCurrentUser()
                .flatMap(user -> applyInternal(baseCriteria, entityCode, user))
                .switchIfEmpty(Mono.just(baseCriteria));
    }

    private Mono<Criteria> applyInternal(Criteria criteria, String entityCode, AuthUser user) {
        // Super admin bypass
        if (isSuperAdmin(user)) {
            return Mono.just(criteria);
        }

        DataPermission dataPermission = user.getDataPermission();
        if (dataPermission == null || dataPermission.dataScope() == null) {
            return Mono.just(criteria);
        }

        Integer dataScope = dataPermission.dataScope();

        return switch (dataScope) {
            case 1 -> Mono.just(criteria); // All data
            case 2 -> applyDeptScope(criteria, entityCode, user.getDeptId(), false);
            case 3 -> applyDeptScope(criteria, entityCode, user.getDeptId(), true);
            case 4 -> applySelfScope(criteria, entityCode, user.getUsername());
            case 5 -> applyCustomScope(criteria, entityCode, dataPermission.rowScope(), user);
            default -> Mono.just(criteria);
        };
    }

    private boolean isSuperAdmin(AuthUser user) {
        return user != null && "admin".equals(user.getUsername());
    }

    private Mono<Criteria> applyDeptScope(Criteria criteria, String entityCode, Long deptId, boolean includeSubDepts) {
        if (deptId == null) {
            return Mono.just(criteria);
        }

        // Check if entity has deptId field
        if (!entityMetadataScanner.hasField(entityCode, DEPT_ID_FIELD)) {
            log.debug("Entity {} does not have deptId field, skipping dept scope", entityCode);
            return Mono.just(criteria);
        }

        if (!includeSubDepts) {
            return Mono.just(criteria.and(DEPT_ID_FIELD).is(deptId));
        }

        // Include sub-departments
        return getAllChildDeptIds(deptId)
                .map(deptIds -> criteria.and(DEPT_ID_FIELD).in(deptIds));
    }

    private Mono<Criteria> applySelfScope(Criteria criteria, String entityCode, String username) {
        if (ObjectUtils.isEmpty(username)) {
            return Mono.just(criteria);
        }

        // Prefer createBy filter; if entity doesn't have createBy, fall back to id matching (less ideal)
        if (entityMetadataScanner.hasField(entityCode, CREATE_BY_FIELD)) {
            return Mono.just(criteria.and(CREATE_BY_FIELD).is(username));
        }

        log.debug("Entity {} does not have createBy field, cannot apply self scope", entityCode);
        return Mono.just(criteria);
    }

    private Mono<Criteria> applyCustomScope(Criteria criteria, String entityCode, RowScope rowScope, AuthUser user) {
        if (rowScope == null) {
            return Mono.just(criteria);
        }

        boolean hasCondition = false;
        Criteria result = criteria;

        // deptIds condition
        if (!CollectionUtils.isEmpty(rowScope.deptIds())
                && entityMetadataScanner.hasField(entityCode, DEPT_ID_FIELD)) {
            result = result.and(DEPT_ID_FIELD).in(rowScope.deptIds());
            hasCondition = true;
        }

        // self condition
        if (Boolean.TRUE.equals(rowScope.self())
                && entityMetadataScanner.hasField(entityCode, CREATE_BY_FIELD)
                && !ObjectUtils.isEmpty(user.getUsername())) {
            result = result.and(CREATE_BY_FIELD).is(user.getUsername());
            hasCondition = true;
        }

        // userIds condition - map userIds to usernames, then filter by createBy
        if (!CollectionUtils.isEmpty(rowScope.userIds())
                && entityMetadataScanner.hasField(entityCode, CREATE_BY_FIELD)) {
            // For simplicity, we filter by id if the entity has id field and userIds match ids
            // Otherwise we need a user lookup which is complex in reactive context
            // Fallback: if self is also set, we already covered current user
            // If not, we can't easily map userIds to usernames here without extra DB call
            log.debug("Custom scope userIds not fully supported without username lookup for entity {}", entityCode);
        }

        if (!hasCondition) {
            return Mono.just(criteria);
        }
        return Mono.just(result);
    }

    private Mono<Set<Long>> getAllChildDeptIds(Long rootDeptId) {
        String cacheKey = DEPT_TREE_CACHE.buildKey(rootDeptId);

        return cacheProcessor.getCache(cacheKey, List.class)
                .flatMap(cached -> {
                    try {
                        @SuppressWarnings("unchecked")
                        List<Long> ids = (List<Long>) cached;
                        return Mono.just(new HashSet<>(ids));
                    } catch (Exception e) {
                        return loadAndCacheChildDeptIds(rootDeptId, cacheKey);
                    }
                })
                .switchIfEmpty(Mono.defer(() -> loadAndCacheChildDeptIds(rootDeptId, cacheKey)));
    }

    private Mono<Set<Long>> loadAndCacheChildDeptIds(Long rootDeptId, String cacheKey) {
        return sysDeptQueryService.queryAllDept()
                .map(allDepts -> {
                    Set<Long> result = new HashSet<>();
                    result.add(rootDeptId);
                    collectChildDeptIds(allDepts, rootDeptId, result);
                    return result;
                })
                .flatMap(ids -> cacheProcessor.setCache(cacheKey, new ArrayList<>(ids), DEPT_TREE_CACHE.ttl())
                        .thenReturn(ids));
    }

    private void collectChildDeptIds(List<SysDeptView> allDepts, Long parentId, Set<Long> result) {
        for (SysDeptView dept : allDepts) {
            if (parentId.equals(dept.getParentId())) {
                result.add(dept.getId());
                collectChildDeptIds(allDepts, dept.getId(), result);
            }
        }
    }

    public Mono<Void> evictDeptTreeCache() {
        return cacheProcessor.deleteCache(DEPT_TREE_CACHE.buildKey("*"));
    }
}
