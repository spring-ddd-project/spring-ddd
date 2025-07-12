package com.springddd.domain.gen;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenTemplateDomain extends AbstractDomainMask {

    private TemplateId id;

    private TemplateInfo templateInfo;
}
