package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LeafAllocRepository extends ReactiveCrudRepository<LeafAllocEntity, Long>{
}
