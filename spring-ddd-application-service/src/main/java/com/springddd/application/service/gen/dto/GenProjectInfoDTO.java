package com.springddd.application.service.gen.dto;

import lombok.Data;

@Data
public class GenProjectInfoDTO {
    private String tableName;
    private String packageName;
    private String className;
    private String moduleName;
    private String projectName;
    private String requestName;
}
