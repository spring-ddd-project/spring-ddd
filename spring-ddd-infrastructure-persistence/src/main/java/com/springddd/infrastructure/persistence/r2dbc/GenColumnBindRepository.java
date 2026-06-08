package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.GenColumnBindEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GenColumnBindRepository extends ReactiveCrudRepository<GenColumnBindEntity, Long> {
}
