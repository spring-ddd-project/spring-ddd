package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SysDeptRepository extends ReactiveCrudRepository<SysDeptEntity, Long> {
}