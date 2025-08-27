package com.springddd.application.service.gen.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenTemplateQuery implements Serializable {

    private Long id;

    private String templateName;

    private String templateContent;

    private Boolean deleteStatus;
}
