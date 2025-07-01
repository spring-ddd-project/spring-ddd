package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuQuery;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.application.service.menu.dto.SysMenuViewMapStruct;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SysMenuQueryService {

    private final SysMenuRepository sysMenuRepository;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    private final SysMenuViewMapStruct sysMenuViewMapStruct;

    private final ReactiveRedisCacheHelper reactiveRedisCacheHelper;

    public Mono<PageResponse<SysMenuView>> page(SysMenuQuery query) {
        Criteria criteria = Criteria.where("delete_status").is("0");
        Query qry = Query.query(criteria)
                .limit(query.getPageSize())
                .offset((long) (query.getPageNum() - 1) * query.getPageSize());
        Mono<List<SysMenuView>> list = r2dbcEntityTemplate.select(SysMenuEntity.class).matching(qry).all().collectList().map(sysMenuViewMapStruct::toViewList);
        Mono<Long> count = r2dbcEntityTemplate.count(Query.query(criteria), SysMenuEntity.class);
        return Mono.zip(list, count).map(tuple -> new PageResponse<>(tuple.getT1(), tuple.getT2(), query.getPageNum(), query.getPageSize()));
    }

    public Mono<SysMenuView> queryByMenuId(Long menuId) {
        return sysMenuRepository.findById(menuId).map(sysMenuViewMapStruct::toView);
    }

    public Mono<SysMenuView> queryByMenuComponent(String component) {
        Criteria criteria = Criteria.where("component").is(component);
        Query qry = Query.query(criteria);
        return r2dbcEntityTemplate.selectOne(qry, SysMenuEntity.class).map(sysMenuViewMapStruct::toView);
    }

    public Mono<List<SysMenuView>> queryAll() {
        return Flux.fromIterable(SecurityUtils.getPermissions())
                .flatMap(p ->
                        r2dbcEntityTemplate.selectOne(
                                Query.query(Criteria.where("permission").is(p.value())),
                                SysMenuEntity.class
                        ).map(sysMenuViewMapStruct::toView)
                )
                .collectList()
                .flatMap(this::loadParentsAndBuildTree)
                .flatMap(menus -> cacheTree(menus).thenReturn(menus));
    }

    private Mono<List<SysMenuView>> loadParentsAndBuildTree(List<SysMenuView> menus) {
        if (CollectionUtils.isEmpty(menus)) {
            return Mono.empty();
        }
        Map<Long, SysMenuView> menuMap = new ConcurrentHashMap<>();
        menus.forEach(menu -> menuMap.put(menu.getId(), menu));

        return Flux.fromIterable(menus)
                .flatMap(menu -> loadParentChain(menu.getParentId(), menuMap))
                .then(Mono.fromCallable(() -> new ArrayList<>(menuMap.values())))
                .map(this::buildMenuTree);
    }

    private Mono<Void> loadParentChain(Long parentId, Map<Long, SysMenuView> menuMap) {
        if (parentId == null || parentId == 0 || menuMap.containsKey(parentId)) {
            return Mono.empty();
        }

        return r2dbcEntityTemplate.selectOne(
                        Query.query(Criteria.where("id").is(parentId)),
                        SysMenuEntity.class
                )
                .map(sysMenuViewMapStruct::toView)
                .flatMap(parent -> {
                    menuMap.put(parent.getId(), parent);
                    return loadParentChain(parent.getParentId(), menuMap);
                });
    }


    private List<SysMenuView> buildMenuTree(List<SysMenuView> flatList) {
        Map<Long, SysMenuView> idToMenuMap = new HashMap<>();
        List<SysMenuView> rootMenus = new ArrayList<>();

        for (SysMenuView menu : flatList) {
            idToMenuMap.put(menu.getId(), menu);
            menu.setChildren(new ArrayList<>());
        }

        for (SysMenuView menu : flatList) {
            Long parentId = menu.getParentId();
            if (parentId == null || parentId == 0) {
                rootMenus.add(menu);
            } else {
                SysMenuView parent = idToMenuMap.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(menu);
                } else {
                    rootMenus.add(menu);
                }
            }
        }

        return rootMenus;
    }

    private Mono<Void> cacheTree(List<SysMenuView> menus) {
        return reactiveRedisCacheHelper.setCache("user:" + SecurityUtils.getUserId() + ":menus", menus, Duration.ofDays(1)).then();
    }

    public Mono<List<SysMenuView>> getParentTree() {
        String key = "user:" + SecurityUtils.getUserId() + ":menus";
        Duration ttl = Duration.ofDays(1);

        return reactiveRedisCacheHelper.getOrLoad(
                key,
                List.class,
                ttl,
                this::queryAll
        );
    }

}
