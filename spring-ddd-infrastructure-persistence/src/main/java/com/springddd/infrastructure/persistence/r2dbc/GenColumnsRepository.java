package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.GenColumnsEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GenColumnsRepository extends ReactiveCrudRepository<GenColumnsEntity, Long> {
}
