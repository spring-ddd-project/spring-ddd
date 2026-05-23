package com.springddd.infrastructure.persistence.entity;

import org.springframework.data.relational.core.mapping.Table;

/**
 * Test entity without DataPermissionEntity annotation.
 */
@Table("test_no_dp")
public class TestNoDpEntity {

    private Long id;

    private String name;
}
