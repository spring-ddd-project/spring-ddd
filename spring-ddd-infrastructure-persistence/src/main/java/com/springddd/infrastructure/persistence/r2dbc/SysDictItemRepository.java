package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SysDictItemRepository extends ReactiveCrudRepository<SysDictItemEntity, Long> {
}
