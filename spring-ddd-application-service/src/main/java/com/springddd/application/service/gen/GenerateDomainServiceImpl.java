package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenAggregateView;
import com.springddd.application.service.gen.dto.ProjectTreeBuilder;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.gen.CodeFormatter;
import com.springddd.domain.gen.GenerateDomainService;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GenerateDomainServiceImpl implements GenerateDomainService {

    private final GenTableInfoQueryService genTableInfoQueryService;

    private final GenTemplateQueryService templateQueryService;

    private final Configuration configuration;

    private final ProjectTreeBuilder treeBuilder = new ProjectTreeBuilder();

    private final ReactiveRedisCacheHelper cacheHelper;

    private final List<CodeFormatter> codeFormatters;

    @Override
    public Mono<Void> generate(String tableName) {
        return genTableInfoQueryService.buildData(tableName)
                .flatMap(context -> {
                    String projectName = (String) context.get("projectName");
                    return templateQueryService.queryAllTemplate()
                            .flatMapMany(Flux::fromIterable)
                            .flatMap(template -> renderTemplate(template.getTemplateName(),
                                    template.getTemplateContent(), context)
                                    .flatMap(renderedCode -> {
                                        String filePath = generateFilePath(template.getTemplateName(), context, projectName);
                                        return formatCode(filePath, renderedCode)
                                                .map(formatted -> new GeneratedFile(filePath, formatted));
                                    }))
                            .collectList()
                            .flatMap(generatedFiles -> {
                                List<ProjectTreeView> treeList = new ArrayList<>();
                                generatedFiles.forEach(file -> saveGeneratedFile(file.filePath, file.content, treeList, projectName));
                                return cacheHelper.setCache(CacheKeys.GEN_FILES.buildKey(SecurityUtils.getUserId()), treeList,
                                        CacheKeys.GEN_FILES.ttl()).then();
                            });
                });
    }

    private record GeneratedFile(String filePath, String content) {}
    private Mono<String> formatCode(String filePath, String content) {
        Mono<String> result = Mono.just(content);
        for (CodeFormatter formatter : codeFormatters) {
            result = result.flatMap(c -> formatter.format(filePath, c));
        }
        return result;
    }

    /**
     * build location
     * projectName-application-infrastructure.persistence/packageName/infrastructure/persistence/entity/ClassNameEntity.java
     */
    private String generateFilePath(String templateName, Map<String, Object> context, String projectName) {
        String moduleName = (String) context.get("moduleName");
        String packageName = (String) context.get("packageName");
        String className = (String) context.get("className");
        String requestName = (String) context.get("requestName");
        List<GenAggregateView> aggregateViews = (List<GenAggregateView>) context.get("aggregateViews");

        String srcPath = "src/main/java/";
        String packagePath = packageName.replace('.', '/');

        return switch (templateName) {
            // infrastructure-persistence
            case "entity" -> projectName + "-infrastructure-persistence/" + srcPath
                    + packagePath + "/infrastructure/persistence/entity/"
                    + className + "Entity.java";
            case "r2dbc" -> projectName + "-infrastructure-persistence/" + srcPath
                    + packagePath + "/infrastructure/persistence/r2dbc/"
                    + className + "Repository.java";
            case "domainRepositoryImpl" -> projectName + "-infrastructure-persistence/" + srcPath
                    + packagePath + "/infrastructure/persistence/"
                    + className + "DomainRepositoryImpl.java";

            // domain
            case "aggregateRoot" -> projectName + "-domain/" + srcPath
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + aggregateViews.stream()
                    .filter(q -> q.getObjectType() == 1 && q.getHasCreated())
                    .map(GenAggregateView::getObjectName).toList().getFirst()
                    + ".java";
            case "objectValue" -> projectName + "-domain/" + srcPath
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + aggregateViews.stream()
                    .filter(q -> q.getObjectType() == 2 && q.getHasCreated())
                    .map(GenAggregateView::getObjectName).toList().getFirst()
                    + ".java";
            case "extendInfo" -> projectName + "-domain/" + srcPath
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + aggregateViews.stream()
                    .filter(q -> q.getObjectType() == 3 && q.getHasCreated())
                    .map(GenAggregateView::getObjectName).toList().getFirst()
                    + ".java";
            case "domain" -> projectName + "-domain/" + srcPath
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + className + "Domain.java";
            case "factory" -> projectName + "-domain/" + srcPath
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + className + "DomainFactory.java";
            case "domainRepository" -> projectName + "-domain/" + srcPath
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + className + "DomainRepository.java";
            case "deleteDomain" -> projectName + "-domain/" + srcPath
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + "Delete" + className + "DomainService.java";
            case "wipeDomain" -> projectName + "-domain/" + srcPath
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + "Wipe" + className + "DomainService.java";
            case "restoreDomain" -> projectName + "-domain/" + srcPath
                    + packagePath + "/domain/"
                    + moduleName + "/"
                    + "Restore" + className + "DomainService.java";

            // application-service
            case "command" -> projectName + "-application-service/" + srcPath
                    + packagePath + "/application/service/"
                    + moduleName + "/dto/"
                    + className + "Command.java";
            case "query" -> projectName + "-application-service/" + srcPath
                    + packagePath + "/application/service/"
                    + moduleName + "/dto/"
                    + className + "Query.java";
            case "view" -> projectName + "-application-service/" + srcPath
                    + packagePath + "/application/service/"
                    + moduleName + "/dto/"
                    + className + "View.java";
            case "mapstruct" -> projectName + "-application-service/" + srcPath
                    + packagePath + "/application/service/"
                    + moduleName + "/dto/"
                    + className + "ViewMapStruct.java";
            case "pageQuery" -> projectName + "-application-service/" + srcPath
                    + packagePath + "/application/service/"
                    + moduleName + "/dto/"
                    + className + "PageQuery.java";
            case "factoryImpl" -> projectName + "-application-service/" + srcPath
                    + packagePath + "/application/service/"
                    + moduleName + "/"
                    + className + "DomainFactoryImpl.java";
            case "deleteDomainImpl" -> projectName + "-application-service/" + srcPath
                    + packagePath + "/application/service/"
                    + moduleName + "/"
                    + "Delete" + className + "DomainServiceImpl.java";
            case "wipeDomainImpl" -> projectName + "-application-service/" + srcPath
                    + packagePath + "/application/service/"
                    + moduleName + "/"
                    + "Wipe" + className + "DomainServiceImpl.java";
            case "restoreDomainImpl" -> projectName + "-application-service/" + srcPath
                    + packagePath + "/application/service/"
                    + moduleName + "/"
                    + "Restore" + className + "DomainServiceImpl.java";
            case "commandService" -> projectName + "-application-service/" + srcPath
                    + packagePath + "/application/service/"
                    + moduleName + "/"
                    + className + "CommandService.java";
            case "queryService" -> projectName + "-application-service/" + srcPath
                    + packagePath + "/application/service/"
                    + moduleName + "/"
                    + className + "QueryService.java";

            // interface-web
            case "controller" -> projectName + "-interface-web/" + srcPath
                    + packagePath + "/web/"
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
                    + "index.ts";
            case "i18n.en.json" -> "apps/web-ele/src/locales/langs/en-US/"
                    + requestName + ".json";
            case "i18n.locale.json" -> "apps/web-ele/src/locales/langs/zh-CN/"
                    + requestName + ".json";

            // readme
            case "sql" -> "SQL.sql";
            case "readme.txt" -> "readme.txt";

            default -> className + ".txt";
        };
    }

    private void saveGeneratedFile(String filePath, String content, List<ProjectTreeView> treeList, String projectName) {
        // Find or create the root node for both cases
        ProjectTreeView applicationRoot = findOrCreateRootNode(projectName, treeList);
        ProjectTreeView appsRoot = findOrCreateRootNode(projectName + "-ui", treeList);
        ProjectTreeView otherRoot = findOrCreateRootNode("README", treeList);

        // Depending on the file path, process under the correct root
        if (filePath.contains("-infrastructure-persistence/") ||
                filePath.contains("-domain/") ||
                filePath.contains("-application-service/") ||
                filePath.contains("-interface-web/")) {
            processPath(filePath, content, applicationRoot, treeList);
        } else if (filePath.contains("apps/web-ele/src/views/") ||
                filePath.contains("apps/web-ele/src/api/") ||
                filePath.contains("apps/web-ele/src/locales/langs/en-US/") ||
                filePath.contains("apps/web-ele/src/locales/langs/zh-CN/")) {
            processPath(filePath, content, appsRoot, treeList);
        } else {
            processOtherPaths(filePath, content, otherRoot, treeList);
        }
    }

    private ProjectTreeView findOrCreateRootNode(String rootLabel, List<ProjectTreeView> treeList) {
        for (ProjectTreeView tree : treeList) {
            if (tree.getLabel().equals(rootLabel)) {
                return tree;
            }
        }

        ProjectTreeView newRoot = new ProjectTreeView();
        newRoot.setLabel(rootLabel);
        newRoot.setChildren(new ArrayList<>());
        treeList.add(newRoot);
        return newRoot;
    }

    private void processPath(String filePath, String content, ProjectTreeView root, List<ProjectTreeView> treeList) {
        ProjectTreeView updatedRoot = treeBuilder.buildTree(root, filePath, content);

        if (!updatedRoot.equals(root)) {
            // checking whether the treeList needs to be updated.
            // In the new implementation this is less critical since we maintain treeList locally
        }
    }

    private void processOtherPaths(String filePath, String content, ProjectTreeView root, List<ProjectTreeView> treeList) {
        ProjectTreeView tree = treeBuilder.buildTree(root, filePath, content);

        // Check if the tree already exists in the list
        Optional<ProjectTreeView> existingTree = treeList.stream()
                .filter(t -> t.getLabel().equals(root.getLabel()))
                .findFirst();

        if (existingTree.isPresent()) {
            ProjectTreeView existing = existingTree.get();
            existing.setChildren(tree.getChildren());
        } else {
            treeList.add(tree);
        }
    }

    public Mono<String> renderTemplate(String templateName, String templateContent, Map<String, Object> dataModel) {
        try {
            Template template = new Template(templateName, new StringReader(templateContent),
                    configuration);
            StringWriter out = new StringWriter();
            template.process(dataModel, out);
            return Mono.just(out.toString());
        } catch (Exception e) {
            return Mono.error(e);
        }
    }
}