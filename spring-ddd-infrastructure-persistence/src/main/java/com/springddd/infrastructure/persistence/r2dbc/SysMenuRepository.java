package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

public interface SysMenuRepository extends ReactiveCrudRepository<SysMenuEntity, Long> {

    /* ==========================================================
     * Hot-path read queries: avoid recursive CTEs.
     * Recursive CTEs are O(depth) and hit MySQL recursion limits
     * (cte_max_recursion_depth). For 1M+ rows we rely on the
     * explicitly maintained tree_path/depth columns and on the
     * parent_id index instead.
     * ========================================================== */

    /**
     * Find root-level active menus (no parent), ordered by sort_order.
     * Primary entry for lazy tree UIs.
     */
    @Query("""
            SELECT * FROM sys_menu
            WHERE delete_status = 0 AND (parent_id IS NULL OR parent_id = 0)
            ORDER BY sort_order
            """)
    Flux<SysMenuEntity> findRootMenus();

    /**
     * Find direct active children of a parent menu, ordered by sort_order.
     * This is the work-horse for lazy tree expansion; uses idx_delete_status_parent_id.
     */
    @Query("""
            SELECT * FROM sys_menu
            WHERE delete_status = 0 AND parent_id = :parentId
            ORDER BY sort_order
            """)
    Flux<SysMenuEntity> findDirectChildrenByParentId(Long parentId);

    /**
     * Find which of the given menu ids have active children.
     * Single round-trip to set hasChildren flags for a list of nodes.
     */
    @Query("""
            SELECT DISTINCT parent_id FROM sys_menu
            WHERE delete_status = 0 AND parent_id IN (:ids)
            """)
    Flux<Long> findParentIdsWithActiveChildren(Collection<Long> ids);

    /**
     * Find all active menus up to a given depth. Uses idx_delete_status_depth.
     * Used for owner/full-tree scenarios where the consumer truncates at the same depth.
     */
    @Query("SELECT * FROM sys_menu WHERE delete_status = 0 AND (depth IS NULL OR depth <= :maxDepth)")
    Flux<SysMenuEntity> findAllActive(int maxDepth);

    /**
     * Find a shallow subtree by tree_path prefix. Requires tree_path to be materialized
     * (depth small enough that path length <= 500). Avoids recursion entirely.
     */
    @Query("""
            SELECT * FROM sys_menu
            WHERE delete_status = 0
              AND tree_path IS NOT NULL
              AND tree_path LIKE CONCAT(:treePathPrefix, '%')
            ORDER BY depth, sort_order
            """)
    Flux<SysMenuEntity> findActiveSubtreeByTreePath(String treePathPrefix);

    /**
     * Find deleted subtree by tree_path prefix for recycle-bin views.
     */
    @Query("""
            SELECT * FROM sys_menu
            WHERE delete_status = 1
              AND tree_path IS NOT NULL
              AND tree_path LIKE CONCAT(:treePathPrefix, '%')
            ORDER BY depth, sort_order
            """)
    Flux<SysMenuEntity> findDeletedSubtreeByTreePath(String treePathPrefix);

    /* ==========================================================
     * Permission / limited-input read queries: recursive CTEs are
     * acceptable here because the input set is small.
     * ========================================================== */

    /**
     * Find menus by ids plus all their ancestors, filtering out deleted nodes.
     * Used during login to build the user's permission tree; input is the user's menuIds.
     */
    @Query("""
            WITH RECURSIVE allowed AS (
                SELECT m.* FROM sys_menu m WHERE m.id IN (:ids) AND m.delete_status = 0
                UNION
                SELECT m.* FROM sys_menu m
                INNER JOIN allowed a ON m.id = a.parent_id
                WHERE m.delete_status = 0
            )
            SELECT * FROM allowed
            """)
    Flux<SysMenuEntity> findMenusByIdsWithAncestors(Collection<Long> ids);

    /**
     * Find all ancestor ids (including the node itself) using a recursive CTE.
     * Used for cycle detection when moving a node. Called rarely.
     */
    @Query("""
            WITH RECURSIVE ancestors AS (
                SELECT id, parent_id FROM sys_menu WHERE id = :menuId
                UNION ALL
                SELECT m.id, m.parent_id FROM sys_menu m
                INNER JOIN ancestors a ON m.id = a.parent_id
            )
            SELECT id FROM ancestors
            """)
    Flux<Long> findAllAncestorIds(Long menuId);

    /* ==========================================================
     * Recycle bin: prefer path-based; keep CTE fallback for mixed
     * deleted/ancestor retrieval when path is unavailable.
     * ========================================================== */

    /**
     * Find all deleted menus plus their live ancestors for recycle bin display.
     * CTE is acceptable because the recycle bin is usually small.
     */
    @Query("""
            WITH RECURSIVE deleted_tree AS (
                SELECT m.* FROM sys_menu m WHERE m.delete_status = 1
                UNION ALL
                SELECT m.* FROM sys_menu m
                INNER JOIN deleted_tree d ON m.id = d.parent_id
                WHERE m.delete_status = 0
            )
            SELECT * FROM deleted_tree
            """)
    Flux<SysMenuEntity> findDeletedMenusWithAncestors();

    /* ==========================================================
     * Write paths: descendants must be touched in bulk. CTEs are
     * used because tree_path may be NULL for extremely deep chains
     * and we must still cascade correctly.
     * ========================================================== */

    /**
     * Soft delete a node and all its descendants.
     */
    @Query("""
            WITH RECURSIVE descendants AS (
                SELECT id FROM sys_menu WHERE id IN (:ids)
                UNION ALL
                SELECT m.id FROM sys_menu m
                INNER JOIN descendants d ON m.parent_id = d.id
            )
            UPDATE sys_menu m
            INNER JOIN descendants d ON m.id = d.id
            SET m.delete_status = 1
            """)
    Mono<Void> softDeleteByIdsWithDescendants(List<Long> ids);

    /**
     * Restore a node and all its deleted descendants.
     */
    @Query("""
            WITH RECURSIVE descendants AS (
                SELECT id FROM sys_menu WHERE id IN (:ids)
                UNION ALL
                SELECT m.id FROM sys_menu m
                INNER JOIN descendants d ON m.parent_id = d.id
            )
            UPDATE sys_menu m
            INNER JOIN descendants d ON m.id = d.id
            SET m.delete_status = 0
            WHERE m.delete_status = 1
            """)
    Mono<Void> restoreByIdsWithDescendants(List<Long> ids);

    /**
     * Update depth and tree_path of a node's descendants (children and below) after a parent move.
     * The node itself must already have been updated by the caller.
     */
    @Query("""
            WITH RECURSIVE descendants AS (
                SELECT id, tree_path FROM sys_menu WHERE parent_id = :menuId
                UNION ALL
                SELECT m.id, m.tree_path FROM sys_menu m
                INNER JOIN descendants d ON m.parent_id = d.id
            )
            UPDATE sys_menu m
            INNER JOIN descendants d ON m.id = d.id
            SET m.depth = m.depth + :depthDelta,
                m.tree_path = CASE
                    WHEN m.tree_path IS NOT NULL AND :oldTreePath IS NOT NULL AND LENGTH(:oldTreePath) > 0
                        AND :newTreePath IS NOT NULL AND LENGTH(:newTreePath) > 0
                    THEN CONCAT(:newTreePath, SUBSTRING(m.tree_path, LENGTH(:oldTreePath) + 1))
                    ELSE m.tree_path
                END
            """)
    Mono<Void> updateDescendantsTreePath(Long menuId, String oldTreePath, String newTreePath, Integer depthDelta);

    /**
     * Delete role-menu associations for all descendant menus.
     */
    @Query("""
            WITH RECURSIVE descendants AS (
                SELECT id FROM sys_menu WHERE id IN (:ids)
                UNION ALL
                SELECT m.id FROM sys_menu m
                INNER JOIN descendants d ON m.parent_id = d.id
            )
            DELETE rm FROM sys_role_menu rm
            INNER JOIN descendants d ON rm.menu_id = d.id
            """)
    Mono<Void> deleteRoleMenuLinksByDescendantIds(List<Long> ids);

    /**
     * Physical delete a node and all its descendants.
     */
    @Query("""
            WITH RECURSIVE descendants AS (
                SELECT id FROM sys_menu WHERE id IN (:ids)
                UNION ALL
                SELECT m.id FROM sys_menu m
                INNER JOIN descendants d ON m.parent_id = d.id
            )
            DELETE m FROM sys_menu m
            INNER JOIN descendants d ON m.id = d.id
            """)
    Mono<Void> deleteAllByIdsWithDescendants(List<Long> ids);
}
