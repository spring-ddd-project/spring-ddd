package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SysUserRepository extends ReactiveCrudRepository<SysUserEntity, Long> {
}
