package com.springddd.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("test_user")
public class TestUserEntity {

    @Id
    private Integer id;

    private String name;
}
