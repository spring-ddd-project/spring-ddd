package com.springddd.mapper;

import com.springddd.entity.TestUserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface TestUserMapper extends ReactiveCrudRepository<TestUserEntity, Integer> {
}
