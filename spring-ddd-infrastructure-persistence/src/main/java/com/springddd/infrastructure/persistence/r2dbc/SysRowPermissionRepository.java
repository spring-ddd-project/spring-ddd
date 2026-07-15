package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.SysRowPermissionEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface SysRowPermissionRepository extends ReactiveCrudRepository<SysRowPermissionEntity, Long> {

    @Query("SELECT id, role_id, menu_id, scope_type, target_type, target_id, delete_status, create_by, create_time, update_by, update_time, version FROM sys_row_permission WHERE role_id = :roleId AND menu_id = :menuId AND delete_status = false")
    Flux<SysRowPermissionEntity> findByRoleIdAndMenuIdAndDeleteStatusFalse(Long roleId, Long menuId);

    @Query("SELECT id, role_id, menu_id, scope_type, target_type, target_id, delete_status, create_by, create_time, update_by, update_time, version FROM sys_row_permission WHERE role_id = :roleId AND delete_status = false")
    Flux<SysRowPermissionEntity> findByRoleIdAndDeleteStatusFalse(Long roleId);
}
