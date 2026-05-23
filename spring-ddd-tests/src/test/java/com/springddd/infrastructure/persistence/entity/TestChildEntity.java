package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.permission.DataPermissionEntity;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Child entity with duplicate fields from parent to cover the
 * fieldNames.add(name) false branch in extractColumns.
 */
@Table("test_child")
@DataPermissionEntity(name = "Test Child")
public class TestChildEntity extends TestParentEntity {

    // Duplicate id from parent
    private Long id;

    private String childName;
}
