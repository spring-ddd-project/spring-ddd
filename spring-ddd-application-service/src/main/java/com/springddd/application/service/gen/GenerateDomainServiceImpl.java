package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.ProjectTreeBuilder;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.domain.gen.GenerateDomainService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GenerateDomainServiceImpl implements GenerateDomainService {

    private final GenTableInfoQueryService genTableInfoQueryService;

    private final GenTemplateQueryService templateQueryService;

    private final Configuration configuration;

    private static List<String> generatedPaths = new ArrayList<>();

    @Override
    public Mono<Void> generate(String tableName) {
        return genTableInfoQueryService.buildData(tableName)
                .flatMap(context -> templateQueryService.queryAllTemplate()
                        .flatMapMany(Flux::fromIterable)
                        .flatMap(template -> renderTemplate(template.getTemplateName(), template.getTemplateContent(), context)
                                .flatMap(renderedCode -> {
                                    String filePath = generateFilePath(template.getTemplateName(), context);
                                    generatedPaths.add(filePath);
                                    return saveGeneratedFile(filePath, renderedCode);
                                }))
                        .then());
    }

    /**
     * build location
     * projectName-application-infrastructure.persistence/packageName/infrastructure/persistence/entity/ClassNameEntity.java
     */
    private String generateFilePath(String templateName, Map<String, Object> context) {
        String projectName = (String) context.get("projectName");
        String moduleName = (String) context.get("moduleName");
        String packageName = (String) context.get("packageName");
        String className = (String) context.get("className");
        String requestName = (String) context.get("requestName");

        String packagePath = packageName.replace('.', '/');

        return switch (templateName) {
            // application-infrastructure-persistence
            case "entity" -> projectName + "-application-infrastructure/persistence/"
                    + packagePath + "/infrastructure/persistence/entity/"
                    + className + "Entity.java";
            case "r2dbc" -> projectName + "-application-infrastructure/persistence/"
                    + packagePath + "/infrastructure/persistence/r2dbc/"
                    + className + "Repository.java";
            case "repository" -> projectName + "-application-infrastructure/persistence/"
                    + packagePath + "/infrastructure/persistence/"
                    + className + "DomainRepositoryImpl.java";

            // application-domain
            case "aggregateRoot" -> projectName + "-application-domain/"
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + requestName + "/"
                    + className + "Id.java";
            case "objectValue" -> projectName + "-application-domain/"
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + requestName + "/"
                    + className + ".java";
            case "extendInfo" -> projectName + "-application-domain/"
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + requestName + "/"
                    + className + "ExtendInfo.java";
            case "domain" -> projectName + "-application-domain/"
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + requestName + "/"
                    + className + "Domain.java";
            case "factory" -> projectName + "-application-domain/"
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + requestName + "/"
                    + className + "DomainFactory.java";
            case "domainRepository" -> projectName + "-application-domain/"
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + requestName + "/"
                    + className + "DomainRepository.java";
            case "deleteDomain" -> projectName + "-application-domain/"
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + requestName + "/"
                    + "Delete" + className + "DomainService.java";
            case "wipeDomain" -> projectName + "-application-domain/"
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + requestName + "/"
                    + "Wipe" + className + "DomainService.java";
            case "restoreDomain" -> projectName + "-application-domain/"
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + requestName + "/"
                    + "Restore" + className + "DomainService.java";

            // application-service
            case "command" -> projectName + "-application-service/"
                    + packagePath + "/service/"
                    + moduleName + "/"
                    + requestName + "/dto/"
                    + className + "Command.java";
            case "query" -> projectName + "-application-service/"
                    + packagePath + "/service/"
                    + moduleName + "/"
                    + requestName + "/dto/"
                    + className + "Query.java";
            case "view" -> projectName + "-application-service/"
                    + packagePath + "/service/"
                    + moduleName + "/"
                    + requestName + "/dto/"
                    + className + "View.java";
            case "mapstruct" -> projectName + "-application-service/"
                    + packagePath + "/service/"
                    + moduleName + "/"
                    + requestName + "/dto/"
                    + className + "ViewMapstruct.java";
            case "pageQuery" -> projectName + "-application-service/"
                    + packagePath + "/service/"
                    + moduleName + "/"
                    + requestName + "/dto/"
                    + className + "PageQuery.java";
            case "deleteDomainImpl" -> projectName + "-application-service/"
                    + packagePath + "/service/"
                    + moduleName + "/"
                    + requestName + "/"
                    + "Delete" + className + "DomainServiceImpl.java";
            case "wipeDomainImpl" -> projectName + "-application-service/"
                    + packagePath + "/service/"
                    + moduleName + "/"
                    + requestName + "/"
                    + "Wipe" + className + "DomainServiceImpl.java";
            case "restoreDomainImpl" -> projectName + "-application-service/"
                    + packagePath + "/service/"
                    + moduleName + "/"
                    + requestName + "/"
                    + "Restore" + className + "DomainServiceImpl.java";
            case "commandService" -> projectName + "-application-service/"
                    + packagePath + "/service/"
                    + moduleName + "/"
                    + requestName + "/"
                    + className + "CommandService.java";
            case "queryService" -> projectName + "-application-service/"
                    + packagePath + "/service/"
                    + moduleName + "/"
                    + requestName + "/"
                    + className + "QueryService.java";

            // application-web
            case "controller" -> projectName + "-application-web/"
                    + className + "Controller.java";

            // vue
            case "index.vue" -> "apps/web-ele/src/views/"
                    + moduleName + "/"
                    + requestName + "/"
                    + "index.vue";
            case "recycle.vue" -> "apps/web-ele/src/views/"
                    + moduleName + "/"
                    + requestName + "/"
                    + "recycle.vue";
            case "form.vue" -> "apps/web-ele/src/views/"
                    + moduleName + "/"
                    + requestName + "/"
                    + "form.vue";
            case "api.ts" -> "apps/web-ele/src/api/"
                    + moduleName + "/"
                    + requestName + "/"
                    + className + "index.ts";
            case "i18n.en.json" -> "apps/web-ele/src/locales/langs/en-US/"
                    + requestName + ".json";
            case "i18n.locale.json" -> "apps/web-ele/src/locales/langs/zh-CN/"
                    + requestName + ".json";

            default -> className + ".txt";
        };
    }

    private Mono<Void> saveGeneratedFile(String filePath, String content) {
//        System.out.println("file: " + filePath + ":\n" + content);
//        System.out.println(" ================================= ");
        ProjectTreeView projectTree = ProjectTreeBuilder.buildTreeFromPaths(generatedPaths);
        ProjectTreeBuilder.printTree(projectTree, "");
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
