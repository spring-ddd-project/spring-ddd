package com.springddd.infrastructure.persistence.entity;

import com.springddd.domain.permission.DataPermissionEntity;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Test entity with a non-static field named serialVersionUID to cover
 * the serialVersionUID branch in extractColumns.
 */
@Table("test_serial_uid")
@DataPermissionEntity(name = "Test Serial UID")
public class TestSerialUidEntity {

    private Long id;

    // Intentionally non-static to exercise the serialVersionUID name check
    private String serialVersionUID;

    private String name;
}
