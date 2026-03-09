package com.springddd.application.service.gen.adapter;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FreemarkerTemplateAdapter implements TemplateEngineAdapter {
    private final Configuration configuration;

    @Override
    public Mono<String> render(String templateName, String templateContent, Map<String, Object> dataModel) {
        try {
            Template template = new Template(templateName, new StringReader(templateContent), configuration);
            StringWriter out = new StringWriter();
            template.process(dataModel, out);
            return Mono.just(out.toString());
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}

















































