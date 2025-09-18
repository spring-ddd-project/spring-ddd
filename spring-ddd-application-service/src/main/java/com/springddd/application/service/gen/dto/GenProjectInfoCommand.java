package com.springddd.application.service.gen.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenProjectInfoCommand implements Serializable {

    private Long id;

    private String tableName;

    private String packageName;

    private String className;

    private String moduleName;

    private String projectName;

    private String requestName;
}
