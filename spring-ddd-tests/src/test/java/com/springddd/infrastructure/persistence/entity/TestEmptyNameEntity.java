package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.permission.DataPermissionEntity;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Test entity with DataPermissionEntity but empty name.
 */
@Table("test_empty_name")
@DataPermissionEntity(name = "")
public class TestEmptyNameEntity {

    private Long id;

    private String name;
}
