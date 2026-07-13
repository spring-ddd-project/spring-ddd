package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Mono;

public interface SysRoleRepository extends ReactiveCrudRepository<SysRoleEntity, Long> {

    Mono<SysRoleEntity> findByRoleCodeAndDeleteStatusFalse(String roleCode);
}
