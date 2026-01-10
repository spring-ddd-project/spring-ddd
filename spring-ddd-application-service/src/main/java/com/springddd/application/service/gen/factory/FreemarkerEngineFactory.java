package com.springddd.application.service.gen.factory;

import com.springddd.application.service.gen.adapter.FreemarkerTemplateAdapter;
import com.springddd.application.service.gen.adapter.TemplateEngineAdapter;
import freemarker.template.Configuration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FreemarkerEngineFactory implements TemplateEngineFactory {

    private final Configuration configuration;

    @Override
    public TemplateEngineAdapter createEngineAdapter() {
        return new FreemarkerTemplateAdapter(configuration);
    }
}























