package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SysMenuRepository extends ReactiveCrudRepository<SysMenuEntity, Long> {
}
