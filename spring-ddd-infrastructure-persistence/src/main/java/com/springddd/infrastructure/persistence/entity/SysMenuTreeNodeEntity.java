package com.springddd.infrastructure.persistence.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * Lightweight projection of sys_menu used for tree traversal operations.
 */
@Data
@Table("sys_menu")
public class SysMenuTreeNodeEntity {

    @Id
    private Long id;

    private Long parentId;

    private Boolean deleteStatus;
}
