package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.SysDictEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SysDictRepository extends ReactiveCrudRepository<SysDictEntity, Long> {
}
