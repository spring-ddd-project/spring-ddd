package com.springddd.application.service.gen.dto;

import lombok.Data;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

@Data
@FieldNameConstants
public class GenTemplateQuery implements Serializable {

    private Long id;

    private String templateName;

    private String templateContent;

    private Boolean deleteStatus;
}
