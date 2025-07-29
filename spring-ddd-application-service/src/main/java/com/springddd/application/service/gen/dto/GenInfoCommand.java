package com.springddd.application.service.gen.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenInfoCommand implements Serializable {

    private Long id;

    private String tableName;

    private String packageName;

    private String className;

    private String requestName;

    private Boolean deleteStatus;
}
