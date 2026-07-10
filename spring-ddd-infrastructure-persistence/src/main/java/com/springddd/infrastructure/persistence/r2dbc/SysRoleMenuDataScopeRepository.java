package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.SysRoleMenuDataScopeEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SysRoleMenuDataScopeRepository extends ReactiveCrudRepository<SysRoleMenuDataScopeEntity, Long> {

    Flux<SysRoleMenuDataScopeEntity> findByRoleIdAndDeleteStatusFalse(Long roleId);

    Mono<SysRoleMenuDataScopeEntity> findByRoleIdAndMenuIdAndDeleteStatusFalse(Long roleId, Long menuId);
}
