package com.springddd.infrastructure.persistence.mapper;

import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface SysMenuMapper extends ReactiveCrudRepository<SysMenuEntity, Long> {
}
