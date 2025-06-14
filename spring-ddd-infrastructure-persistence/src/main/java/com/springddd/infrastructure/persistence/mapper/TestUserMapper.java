package com.springddd.infrastructure.persistence.mapper;

import com.springddd.domain.TestUserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TestUserMapper extends ReactiveCrudRepository<TestUserEntity, Integer> {
}
