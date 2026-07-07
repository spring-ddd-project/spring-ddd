package com.springddd.application.service.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.menu.dto.SysMenuQuery;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.application.service.menu.dto.SysMenuViewMapStruct;
import com.springddd.application.service.role.SysRoleQueryService;
import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.domain.util.PageResponse;
import com.springddd.domain.util.ReactiveTreeUtils;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuQueryService {

    /**
     * Hard ceiling for full-tree loading. Rendering more than a few dozen levels in the
     * UI is meaningless, and this protects both the DB and the browser from 1M-depth chains.
     */
    private static final int FULL_TREE_MAX_DEPTH = 30;

    private final SysMenuRepository sysMenuRepository;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final SysMenuViewMapStruct sysMenuViewMapStruct;

    private final ReactiveRedisCacheHelper reactiveRedisCacheHelper;

    private final ObjectMapper objectMapper;

    private final SysRoleQueryService sysRoleQueryService;

    /* ==========================================================
     * Flat / paginated listing (no tree building)
     * ========================================================== */

    public Mono<PageResponse<SysMenuView>> index(SysMenuQuery query) {
        Criteria criteria = Criteria.where(SysMenuQuery.Fields.deleteStatus).is(false);
        Query qry = Query.query(criteria).sort(Sort.by("sort_order"));
        Mono<List<SysMenuView>> list = r2dbcEntityTemplate.select(SysMenuEntity.class).matching(qry).all().collectList()
                .map(sysMenuViewMapStruct::toViewList);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysMenuEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), 0, 0));
    }

    public Mono<SysMenuView> queryByMenuId(Long menuId) {
        return sysMenuRepository.findById(menuId)
                .filter(menu -> !Boolean.TRUE.equals(menu.getDeleteStatus()))
                .map(sysMenuViewMapStruct::toView);
    }

    public Mono<SysMenuView> queryByApi(String api) {
        Criteria criteria = Criteria.where(SysMenuQuery.Fields.api).is(api);
        Query qry = Query.query(criteria);
        return r2dbcEntityTemplate.selectOne(qry, SysMenuEntity.class).map(sysMenuViewMapStruct::toView);
    }

    /* ==========================================================
     * Lazy tree loading: primary path for the Menu Management UI.
     * Reads only one level at a time; each call is a single indexed
     * query plus one batch children-existence check.
     * ========================================================== */

    public Mono<List<SysMenuView>> getMenuTreeWithoutPermission(Long parentId) {
        Criteria criteria;
        if (parentId == null || parentId == 0) {
            criteria = Criteria.where(SysMenuQuery.Fields.deleteStatus).is(false)
                    .and(SysMenuQuery.Fields.parentId).isNull();
        } else {
            criteria = Criteria.where(SysMenuQuery.Fields.deleteStatus).is(false)
                    .and(SysMenuQuery.Fields.parentId).is(parentId);
        }
        Query qry = Query.query(criteria).sort(Sort.by("sort_order"));
        return r2dbcEntityTemplate.select(SysMenuEntity.class).matching(qry).all()
                .collectList()
                .flatMap(this::toViewsWithChildrenFlag);
    }

    /* ==========================================================
     * Full permission tree: used during login and by /sys/menu/all.
     * Result is cached per user because the permission set is small.
     * ========================================================== */

    public Mono<List<SysMenuView>> queryByPermissions() {
        return ReactiveSecurityUtils.getCurrentUser()
                .flatMap(user -> {
                    List<Long> menuIds = user.getMenuIds() != null ? user.getMenuIds() : List.of();
                    if (CollectionUtils.isEmpty(menuIds)) {
                        return Mono.just(Collections.<SysMenuView>emptyList());
                    }
                    return collectMenusWithAncestors(menuIds)
                            .collectList()
                            .map(sysMenuViewMapStruct::toViewList)
                            .flatMap(buildTree());
                })
                .flatMap(cacheTree());
    }

    public Mono<List<SysMenuView>> getMenuTreeWithPermission() {
        return ReactiveSecurityUtils.getCurrentUserRoles()
                .flatMap(codes -> {
                    if (CollectionUtils.isEmpty(codes)) {
                        return Mono.empty();
                    }
                    return Flux.fromIterable(codes)
                            .flatMap(sysRoleQueryService::getByCode)
                            .filter(Objects::nonNull)
                            .filter(role -> Boolean.TRUE.equals(role.getOwnerStatus()))
                            .hasElements()
                            .flatMap(getTreeWithPermission());
                });
    }

    /**
     * Loads the full active tree up to {@link #FULL_TREE_MAX_DEPTH}. This is intended for
     * small-to-medium menus; for million-row datasets the lazy endpoints should be used.
     */
    public Mono<List<SysMenuView>> getFullMenuTree() {
        return sysMenuRepository.findByDeleteStatusAndDepthLessThanEqual(false, FULL_TREE_MAX_DEPTH)
                .collectList()
                .map(sysMenuViewMapStruct::toViewList)
                .flatMap(buildTree());
    }

    /* ==========================================================
     * Recycle bin: show deleted nodes together with their live ancestors.
     * ========================================================== */

    public Mono<PageResponse<SysMenuView>> recycle(SysMenuQuery query) {
        return collectDeletedMenusWithAncestors()
                .sort(Comparator.comparing(
                        SysMenuEntity::getSortOrder,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .collectList()
                .map(sysMenuViewMapStruct::toViewList)
                .map(views -> {
                    long count = views.stream().filter(SysMenuView::getDeleteStatus).count();
                    return new PageResponse<>(views, count, 0, 0);
                });
    }

    /* ==========================================================
     * Internal helpers
     * ========================================================== */

    /**
     * Collects all menus in the input set plus their active ancestors iteratively.
     * Avoids recursive CTEs for cross-database compatibility.
     */
    private Flux<SysMenuEntity> collectMenusWithAncestors(Collection<Long> menuIds) {
        return sysMenuRepository.findByIdInAndDeleteStatus(menuIds, false)
                .collectList()
                .flatMapMany(entities -> {
                    Set<Long> collectedIds = entities.stream().map(SysMenuEntity::getId).collect(Collectors.toSet());
                    Set<Long> parentIds = entities.stream()
                            .map(SysMenuEntity::getParentId)
                            .filter(Objects::nonNull)
                            .filter(id -> !collectedIds.contains(id) && id > 0)
                            .collect(Collectors.toSet());
                    if (parentIds.isEmpty()) {
                        return Flux.fromIterable(entities);
                    }
                    return Flux.fromIterable(entities)
                            .concatWith(collectAncestorsIteratively(parentIds, collectedIds));
                });
    }

    private Flux<SysMenuEntity> collectAncestorsIteratively(Set<Long> parentIds, Set<Long> collectedIds) {
        if (parentIds.isEmpty()) {
            return Flux.empty();
        }
        return sysMenuRepository.findByIdInAndDeleteStatus(parentIds, false)
                .collectList()
                .flatMapMany(parents -> {
                    for (SysMenuEntity parent : parents) {
                        collectedIds.add(parent.getId());
                    }
                    Set<Long> nextParentIds = parents.stream()
                            .map(SysMenuEntity::getParentId)
                            .filter(Objects::nonNull)
                            .filter(id -> !collectedIds.contains(id) && id > 0)
                            .collect(Collectors.toSet());
                    if (nextParentIds.isEmpty()) {
                        return Flux.fromIterable(parents);
                    }
                    return Flux.fromIterable(parents)
                            .concatWith(collectAncestorsIteratively(nextParentIds, collectedIds));
                });
    }

    /**
     * Collects all deleted menus plus their live ancestors iteratively.
     */
    private Flux<SysMenuEntity> collectDeletedMenusWithAncestors() {
        return r2dbcEntityTemplate.select(SysMenuEntity.class)
                .matching(Query.query(Criteria.where(SysMenuQuery.Fields.deleteStatus).is(true)))
                .all()
                .collectList()
                .flatMapMany(deleted -> {
                    Set<Long> deletedIds = deleted.stream().map(SysMenuEntity::getId).collect(Collectors.toSet());
                    Set<Long> parentIds = deleted.stream()
                            .map(SysMenuEntity::getParentId)
                            .filter(Objects::nonNull)
                            .filter(id -> !deletedIds.contains(id) && id > 0)
                            .collect(Collectors.toSet());
                    if (parentIds.isEmpty()) {
                        return Flux.fromIterable(deleted);
                    }
                    return Flux.fromIterable(deleted)
                            .concatWith(collectAncestorsIteratively(parentIds, deletedIds));
                });
    }

    private Mono<List<SysMenuView>> toViewsWithChildrenFlag(List<SysMenuEntity> entities) {
        if (entities.isEmpty()) {
            return Mono.just(Collections.emptyList());
        }
        List<SysMenuView> views = sysMenuViewMapStruct.toViewList(entities);
        List<Long> ids = views.stream().map(SysMenuView::getId).toList();
        Criteria criteria = Criteria.where(SysMenuQuery.Fields.parentId).in(ids)
                .and(SysMenuQuery.Fields.deleteStatus).is(false);
        return r2dbcEntityTemplate.select(SysMenuEntity.class).matching(Query.query(criteria)).all()
                .map(SysMenuEntity::getParentId)
                .collect(Collectors.toSet())
                .map(hasChildrenSet -> {
                    views.forEach(view -> view.setHasChildren(hasChildrenSet.contains(view.getId())));
                    return views;
                });
    }

    private Function<List<SysMenuView>, Mono<? extends List<SysMenuView>>> buildTree() {
        return menus -> ReactiveTreeUtils.buildTree(
                menus,
                SysMenuView::getId,
                SysMenuView::getParentId,
                SysMenuView::setChildren,
                menu -> menu.getParentId() == null || menu.getParentId() == 0,
                Comparator.comparingInt(o -> {
                    if (o.getMeta() == null || o.getMeta().getOrder() == null) {
                        return Integer.MAX_VALUE;
                    }
                    return o.getMeta().getOrder();
                }),
                menu -> !Boolean.TRUE.equals(menu.getDeleteStatus()),
                FULL_TREE_MAX_DEPTH,
                SysMenuView::getDeleteStatus);
    }

    private Function<List<SysMenuView>, Mono<? extends List<SysMenuView>>> cacheTree() {
        return menus -> {
            Mono<Void> withPermissionsTreeCache = cacheMenuWithPermissionsTree(menus);
            List<SysMenuView> withoutPermissionsTree = extractMenuWithoutPermissionsTree(menus);
            Mono<Void> withoutPermissionsTreeCache = cacheMenuWithoutPermissionsTree(withoutPermissionsTree);
            return Mono.when(withPermissionsTreeCache, withoutPermissionsTreeCache).thenReturn(withoutPermissionsTree);
        };
    }

    private Function<Boolean, Mono<? extends List<SysMenuView>>> getTreeWithPermission() {
        return hasOwner -> ReactiveSecurityUtils.getCurrentUserId()
                .flatMap(userId -> {
                    if (hasOwner) {
                        return sysMenuRepository.findByDeleteStatusAndDepthLessThanEqual(false, FULL_TREE_MAX_DEPTH)
                                .collectList()
                                .map(sysMenuViewMapStruct::toViewList)
                                .flatMap(buildTree());
                    }
                    return reactiveRedisCacheHelper
                            .getCache(CacheKeys.MENU_WITH_PERMISSIONS.buildKey(userId), List.class)
                            .flatMap(list -> {
                                try {
                                    List<SysMenuView> menuViews = objectMapper.convertValue(list, new TypeReference<>() {
                                    });
                                    return Mono.just(menuViews);
                                } catch (IllegalArgumentException e) {
                                    return Mono.error(new RuntimeException("Error deserializing SysMenuView list"));
                                }
                            })
                            .switchIfEmpty(Mono.error(new RuntimeException("No menus found")));
                });
    }

    private List<SysMenuView> extractMenuWithoutPermissionsTree(List<SysMenuView> menus) {
        return menus.stream()
                .map(this::filterOutInvalidMenusRecursively)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private SysMenuView filterOutInvalidMenusRecursively(SysMenuView menu) {
        if (menu.getMenuType() == 3) {
            return null;
        }
        List<SysMenuView> validChildren = Optional.ofNullable(menu.getChildren())
                .orElse(Collections.emptyList())
                .stream()
                .map(this::filterOutInvalidMenusRecursively)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        SysMenuView copy = copyMenuWithoutChildren(menu);
        copy.setChildren(validChildren);
        return copy;
    }

    private SysMenuView copyMenuWithoutChildren(SysMenuView menu) {
        SysMenuView copy = new SysMenuView();
        copy.setMeta(menu.getMeta());
        copy.setId(menu.getId());
        copy.setParentId(menu.getParentId());
        copy.setMenuType(menu.getMenuType());
        copy.setPath(menu.getPath());
        copy.setName(menu.getName());
        copy.setComponent(menu.getComponent());
        copy.setApi(menu.getApi());
        copy.setPermission(menu.getPermission());
        copy.setVisible(menu.getVisible());
        copy.setEmbedded(menu.getEmbedded());
        copy.setMenuStatus(menu.getMenuStatus());
        copy.setDeleteStatus(menu.getDeleteStatus());
        return copy;
    }

    private Mono<Void> cacheMenuWithPermissionsTree(List<SysMenuView> menus) {
        return ReactiveSecurityUtils.getCurrentUserId()
                .flatMap(userId -> reactiveRedisCacheHelper.setCache(
                        CacheKeys.MENU_WITH_PERMISSIONS.buildKey(userId),
                        menus, CacheKeys.MENU_WITH_PERMISSIONS.ttl()).then());
    }

    private Mono<Void> cacheMenuWithoutPermissionsTree(List<SysMenuView> menus) {
        return ReactiveSecurityUtils.getCurrentUserId()
                .flatMap(userId -> reactiveRedisCacheHelper.setCache(
                        CacheKeys.MENU_WITHOUT_PERMISSIONS.buildKey(userId),
                        menus, CacheKeys.MENU_WITHOUT_PERMISSIONS.ttl()).then());
    }

    public Mono<List<SysMenuView>> queryAllMenu() {
        return sysMenuRepository.findAll().collectList().map(sysMenuViewMapStruct::toViewList);
    }
}
