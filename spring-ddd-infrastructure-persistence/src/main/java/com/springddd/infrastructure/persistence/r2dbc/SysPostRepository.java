package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.SysPostEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SysPostRepository extends ReactiveCrudRepository<SysPostEntity, Long> {
}
