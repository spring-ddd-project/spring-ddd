package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenTemplateDomain;
import com.springddd.domain.gen.GenTemplateDomainFactory;
import com.springddd.domain.gen.TemplateInfo;
import org.springframework.stereotype.Component;

@Component
public class GenTemplateDomainFactoryImpl implements GenTemplateDomainFactory {
    @Override
    public GenTemplateDomain newInstance(TemplateInfo templateInfo) {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setTemplateInfo(templateInfo);
        domain.setDeleteStatus(false);
        return domain;
    }
}
