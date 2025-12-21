package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.SysRoleMenuEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SysRoleMenuRepository extends ReactiveCrudRepository<SysRoleMenuEntity, Long> {
}
