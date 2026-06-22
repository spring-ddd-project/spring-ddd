package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.LeafWorkerEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface LeafWorkerRepository extends ReactiveCrudRepository<LeafWorkerEntity, Long>{
}
