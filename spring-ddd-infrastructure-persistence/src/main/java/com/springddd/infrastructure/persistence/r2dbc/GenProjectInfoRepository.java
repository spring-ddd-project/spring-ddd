package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.GenProjectInfoEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GenProjectInfoRepository extends ReactiveCrudRepository<GenProjectInfoEntity, Long> {
}
