package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.TemplateContentNullException;
import com.springddd.domain.gen.exception.TemplateNameNullException;
import org.springframework.util.ObjectUtils;

public record TemplateInfo(String templateName, String templateContent) {

    public TemplateInfo {
        if (ObjectUtils.isEmpty(templateName)) {
            throw new TemplateNameNullException();
        }
        if (ObjectUtils.isEmpty(templateContent)) {
            throw new TemplateContentNullException();
        }
    }
}
