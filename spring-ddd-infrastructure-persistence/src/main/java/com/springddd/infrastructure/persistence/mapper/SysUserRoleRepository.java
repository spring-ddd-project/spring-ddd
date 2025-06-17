package com.springddd.infrastructure.persistence.mapper;

import com.springddd.infrastructure.persistence.entity.SysUserRoleEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SysUserRoleRepository extends ReactiveCrudRepository<SysUserRoleEntity, Long> {
}
