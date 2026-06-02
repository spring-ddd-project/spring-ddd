package com.springddd.application.service.gen.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class GenTemplateCommand implements Serializable {

    private Long id;

    private String templateName;

    private String templateContent;
}
