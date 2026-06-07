package com.springddd.infrastructure.persistence.entity;

import org.springframework.data.relational.core.mapping.Table;

/**
 * Test entity with empty name.
 */
@Table("test_empty_name")
public class TestEmptyNameEntity {

    private Long id;

    private String name;
}
