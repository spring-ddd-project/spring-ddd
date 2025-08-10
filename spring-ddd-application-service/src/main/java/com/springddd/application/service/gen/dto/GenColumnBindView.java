package com.springddd.application.service.gen.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenColumnBindView implements Serializable {

    private Long id;

    private String columnType;

    private String entityType;

    private Integer componentType;
}
