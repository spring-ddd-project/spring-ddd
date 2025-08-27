package com.springddd.infrastructure.persistence.r2dbc;

import com.springddd.infrastructure.persistence.entity.GenTemplateEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface GenTemplateRepository extends ReactiveCrudRepository<GenTemplateEntity, Long> {
}
