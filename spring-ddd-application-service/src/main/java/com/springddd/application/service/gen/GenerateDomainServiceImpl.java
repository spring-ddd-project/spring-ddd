package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenerateDomainService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
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
    public Mono<Void> generate(String tableName, String projectName) {
        return genTableInfoQueryService.buildData(tableName)
                .flatMap(context -> {
                    context.put("projectName", projectName);

                    return templateQueryService.queryAllTemplate()
                            .flatMapMany(Flux::fromIterable)
                            .flatMap(template -> renderTemplate(template.getTemplateName(), template.getTemplateContent(), context)
                                    .flatMap(renderedCode -> {
                                        String filePath = generateFilePath(template.getTemplateName(), context);
                                        return saveGeneratedFile(filePath, renderedCode);
                                    }))
                            .then();
                });
    }

    /**
     * build location
     * projectName-application-infrastructure.persistence/packageName/infrastructure/persistence/entity/ClassNameEntity.java
     */
    private String generateFilePath(String templateName, Map<String, Object> context) {
        String projectName = (String) context.get("projectName");
        String packageName = (String) context.get("packageName");
        String className = (String) context.get("className");
        String requestName = (String) context.get("requestName");

        String packagePath = packageName.replace('.', '/');

        return switch (templateName) {
            case "entity" -> projectName + "-application-infrastructure.persistence/"
                    + packagePath + "/infrastructure/persistence/entity/"
                    + className + "Entity.java";
            case "r2dbc" -> projectName + "-application-infrastructure.persistence/"
                    + packagePath + "/infrastructure/persistence/r2dbc/"
                    + className + "R2dbc.java";
            case "repository" -> projectName + "-application-infrastructure.persistence/"
                    + packagePath + "/infrastructure/persistence/"
                    + className + "Repository.java";
            case "request" -> projectName + "-application-domain/"
                    + packagePath + "/domain/"
                    + requestName + "/"
                    + className + "Request.java";


            default -> projectName + "-application-domain/"
                    + packagePath + "/domain/"
                    + requestName + "/"
                    + className + templateName + ".java";
        };
    }

    private Mono<Void> saveGeneratedFile(String filePath, String content) {
        System.out.println("Saving file: " + filePath);
        return Mono.empty();
    }

    public Mono<String> renderTemplate(String templateName, String templateContent, Map<String, Object> dataModel) {
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
