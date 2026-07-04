package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.Collection;

public interface SysMenuRepository extends ReactiveCrudRepository<SysMenuEntity, Long> {

    /**
     * Find direct children of a parent menu filtered by delete_status.
     */
    Flux<SysMenuEntity> findByParentIdAndDeleteStatus(Long parentId, boolean deleteStatus);

    /**
     * Find direct children of a parent menu regardless of delete_status.
     */
    Flux<SysMenuEntity> findByParentId(Long parentId);

    /**
     * Find active root-level menus (no parent).
     */
    Flux<SysMenuEntity> findByDeleteStatusAndParentIdIsNull(boolean deleteStatus);

    /**
     * Find all menus by delete_status up to a given depth.
     */
    Flux<SysMenuEntity> findByDeleteStatusAndDepthLessThanEqual(boolean deleteStatus, int maxDepth);

    /**
     * Find menus by api address.
     */
    Flux<SysMenuEntity> findByApi(String api);

    /**
     * Find menus by id set.
     */
    Flux<SysMenuEntity> findByIdIn(Collection<Long> ids);

    /**
     * Find menus by id set and delete_status.
     */
    Flux<SysMenuEntity> findByIdInAndDeleteStatus(Collection<Long> ids, boolean deleteStatus);
}
