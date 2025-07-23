package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.GenInfoEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GenInfoRepository extends ReactiveCrudRepository<GenInfoEntity, Long> {
}
