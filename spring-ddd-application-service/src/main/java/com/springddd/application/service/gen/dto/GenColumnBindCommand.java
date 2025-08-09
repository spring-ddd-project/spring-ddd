package com.springddd.application.service.gen.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenColumnBindCommand implements Serializable {

    private Long id;

    private String columnType;

    private String entityType;

    private String componentName;
}
