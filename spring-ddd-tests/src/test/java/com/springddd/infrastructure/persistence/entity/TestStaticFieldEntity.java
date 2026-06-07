package com.springddd.infrastructure.persistence.entity;

import org.springframework.data.relational.core.mapping.Table;

/**
 * Test entity with static fields and serialVersionUID to cover extractColumns branches.
 */
@Table("test_static_field")
public class TestStaticFieldEntity {

    private static final long serialVersionUID = 1L;

    private static final String STATIC_FIELD = "static";

    private Long id;

    private String name;
}
