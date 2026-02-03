package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.GenAggregateEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GenAggregateRepository extends ReactiveCrudRepository<GenAggregateEntity, Long>{
}
