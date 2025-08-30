package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenerateDomainService;
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
public class GenerateDomainServiceImpl implements GenerateDomainService {

    private final GenTableInfoQueryService genTableInfoQueryService;

    private final GenTemplateQueryService templateQueryService;

    private final Configuration configuration;

    @Override
    public Mono<Void> generate(String tableName) {
        return genTableInfoQueryService.buildData(tableName)
                .flatMap(context -> templateQueryService.queryByTemplateName("r2dbc")
                        .flatMap(template -> renderTemplate(template.getTemplateContent(), context))
                        .flatMap(text -> {
                            System.out.println("text = " + text);
                            return Mono.empty();
                        }));
    }

    public Mono<String> renderTemplate(String templateContent, Map<String, Object> dataModel) {
        try {
            Template template = new Template("dynamicTemplate", new StringReader(templateContent), configuration);
            StringWriter out = new StringWriter();
            template.process(dataModel, out);
            return Mono.just(out.toString());
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}
