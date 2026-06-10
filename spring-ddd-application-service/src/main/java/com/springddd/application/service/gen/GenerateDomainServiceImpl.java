package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenAggregateView;
import com.springddd.application.service.gen.dto.GenTemplateView;
import com.springddd.application.service.gen.dto.ProjectTreeBuilder;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.domain.gen.GenerateDomainService;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenerateDomainServiceImpl implements GenerateDomainService {

    private static final String SRC_MAIN_JAVA = "src/main/java/";
    private static final String DOMAIN_SUFFIX = "-domain/";
    private static final String INFRA_SUFFIX = "-infrastructure-persistence/";
    private static final String APP_SUFFIX = "-application-service/";
    private static final String WEB_SUFFIX = "-interface-web/";

    // Pattern to match class, record, interface, or enum declarations
    private static final Pattern CLASS_DECLARATION_PATTERN =
            Pattern.compile("^public\\s+(?:class|record|interface|enum)\\s+(\\w+)");

    private final GenTableInfoQueryService genTableInfoQueryService;
    private final GenTemplateQueryService templateQueryService;
    private final Configuration configuration;
    private final ReactiveRedisCacheHelper cacheHelper;
    private final ProjectTreeBuilder treeBuilder = new ProjectTreeBuilder();

    @Override
    public Mono<Void> generate(String tableName) {
        if (tableName == null || tableName.isBlank()) {
            return Mono.error(new IllegalArgumentException("Table name must not be empty"));
        }

        return genTableInfoQueryService.buildData(tableName)
                .switchIfEmpty(Mono.error(new IllegalStateException(
                        "Table info not found for: " + tableName + ". Please sync table info first.")))
                .flatMap(this::processTemplates)
                .onErrorResume(e -> {
                    log.error("Code generation failed for table {}: {}", tableName, e.getMessage(), e);
                    return Mono.error(e);
                });
    }

    /**
     * Processes all templates for the given context and caches the generated file tree.
     */
    private Mono<Void> processTemplates(Map<String, Object> context) {
        String projectName = getStr(context, "projectName");
        if (projectName.isBlank()) {
            return Mono.error(new IllegalStateException("Project name is missing in table info"));
        }

        return templateQueryService.queryAllTemplate()
                .flatMapMany(Flux::fromIterable)
                .flatMap(template -> renderAndSplitTemplate(template, context, projectName))
                .collectList()
                .flatMap(generatedFiles -> {
                    if (generatedFiles.isEmpty()) {
                        log.warn("No files generated for project: {}", projectName);
                        return Mono.empty();
                    }
                    List<ProjectTreeView> treeList = buildProjectTree(generatedFiles, projectName);
                    return ReactiveSecurityUtils.getCurrentUserId()
                            .flatMap(userId -> cacheHelper.setCache(
                                    CacheKeys.GEN_FILES.buildKey(userId),
                                    treeList,
                                    CacheKeys.GEN_FILES.ttl()
                            ).then());
                });
    }

    /**
     * Renders a single template and splits the output into multiple files if necessary.
     */
    private Flux<GeneratedFile> renderAndSplitTemplate(GenTemplateView template, Map<String, Object> context, String projectName) {
        String templateName = template.getTemplateName();
        String templateContent = template.getTemplateContent();

        if (templateName == null || templateContent == null) {
            log.warn("Skipping incomplete template: {}", templateName);
            return Flux.empty();
        }

        return renderTemplate(templateName, templateContent, context)
                .flatMapMany(renderedCode -> {
                    if (isMultiClassTemplate(templateName)) {
                        return Flux.fromIterable(splitMultiClassOutput(renderedCode, templateName, context, projectName));
                    }
                    String filePath = generateFilePath(templateName, context, projectName);
                    return Flux.just(new GeneratedFile(filePath, renderedCode));
                })
                .onErrorResume(e -> {
                    log.error("Failed to render template {}: {}", templateName, e.getMessage());
                    return Flux.empty();
                });
    }

    /**
     * Builds the project tree view from the list of generated files.
     */
    private List<ProjectTreeView> buildProjectTree(List<GeneratedFile> files, String projectName) {
        List<ProjectTreeView> treeList = new ArrayList<>();
        ProjectTreeView appRoot = findOrCreateRootNode(projectName, treeList);
        ProjectTreeView uiRoot = findOrCreateRootNode(projectName + "-ui", treeList);
        ProjectTreeView otherRoot = findOrCreateRootNode("README", treeList);

        for (GeneratedFile file : files) {
            String path = file.filePath();
            if (isBackendPath(path)) {
                treeBuilder.buildTree(appRoot, path, file.content());
            } else if (isFrontendPath(path)) {
                treeBuilder.buildTree(uiRoot, path, file.content());
            } else {
                treeBuilder.buildTree(otherRoot, path, file.content());
            }
        }
        return treeList;
    }

    private boolean isBackendPath(String path) {
        return path.contains(INFRA_SUFFIX) || path.contains(DOMAIN_SUFFIX) ||
                path.contains(APP_SUFFIX) || path.contains(WEB_SUFFIX);
    }

    private boolean isFrontendPath(String path) {
        return path.contains("apps/web-ele/src/");
    }

    // ==========================================
    // File Path Generation
    // ==========================================

    /**
     * Generates the file path for a specific template.
     */
    private String generateFilePath(String templateName, Map<String, Object> context, String projectName) {
        String moduleName = getStr(context, "moduleName");
        String packageName = getStr(context, "packageName");
        String className = getStr(context, "className");
        String requestName = getStr(context, "requestName");

        String packagePath = packageName.replace('.', '/');

        // Pre-calculate common base paths to avoid repetition
        String domainBase = projectName + DOMAIN_SUFFIX + SRC_MAIN_JAVA + packagePath + "/domain/" + moduleName + "/";
        String appBase = projectName + APP_SUFFIX + SRC_MAIN_JAVA + packagePath + "/application/service/" + moduleName + "/";
        String appDtoBase = appBase + "dto/";
        String infraBase = projectName + INFRA_SUFFIX + SRC_MAIN_JAVA + packagePath + "/infrastructure/persistence/";
        String webBase = projectName + WEB_SUFFIX + SRC_MAIN_JAVA + packagePath + "/web/";
        String vueBase = "apps/web-ele/src/";

        @SuppressWarnings("unchecked")
        List<GenAggregateView> aggregateViews = (List<GenAggregateView>) context.get("aggregateViews");

        return switch (templateName) {
            // Infrastructure persistence layer
            case "entity" -> infraBase + "entity/" + className + "Entity.java";
            case "r2dbc" -> infraBase + "r2dbc/" + className + "Repository.java";
            case "domainRepositoryImpl" -> infraBase + className + "DomainRepositoryImpl.java";

            // Domain layer
            case "aggregateRoot" -> domainBase + safeGetAggregateName(aggregateViews, 1) + ".java";
            case "objectValue" -> domainBase + safeGetAggregateName(aggregateViews, 2) + ".java";
            case "extendInfo" -> domainBase + safeGetAggregateName(aggregateViews, 3) + ".java";
            case "domain" -> domainBase + className + "Domain.java";
            case "factory" -> domainBase + className + "DomainFactory.java";
            case "domainRepository" -> domainBase + className + "DomainRepository.java";
            case "deleteDomain" -> domainBase + "Delete" + className + "DomainService.java";
            case "wipeDomain" -> domainBase + "Wipe" + className + "DomainService.java";
            case "restoreDomain" -> domainBase + "Restore" + className + "DomainService.java";

            // Application service layer
            case "command", "query", "view", "pageQuery" -> appDtoBase + className + capitalize(templateName) + ".java";
            case "mapstruct" -> appDtoBase + className + "ViewMapStruct.java";
            case "factoryImpl" -> appBase + className + "DomainFactoryImpl.java";
            case "deleteDomainImpl" -> appBase + "Delete" + className + "DomainServiceImpl.java";
            case "wipeDomainImpl" -> appBase + "Wipe" + className + "DomainServiceImpl.java";
            case "restoreDomainImpl" -> appBase + "Restore" + className + "DomainServiceImpl.java";
            case "commandService" -> appBase + className + "CommandService.java";
            case "queryService" -> appBase + className + "QueryService.java";

            // Interface web layer
            case "controller" -> webBase + className + "Controller.java";

            // Vue frontend
            case "index.vue", "recycle.vue", "form.vue" ->
                    vueBase + "views/" + moduleName + "/" + requestName + "/" + templateName;
            case "api.ts" -> vueBase + "api/" + moduleName + "/" + requestName + "/index.ts";
            case "i18n.en.json" -> vueBase + "locales/langs/en-US/" + requestName + ".json";
            case "i18n.locale.json" -> vueBase + "locales/langs/zh-CN/" + requestName + ".json";

            // Miscellaneous
            case "sql" -> "SQL.sql";
            case "readme.txt" -> "readme.txt";

            default -> className + ".txt";
        };
    }

    // ==========================================
    // Multi-class Output Splitting
    // ==========================================

    /**
     * Checks if a template is expected to produce multiple class declarations.
     */
    private boolean isMultiClassTemplate(String templateName) {
        return "objectValue".equals(templateName) ||
                "aggregateRoot".equals(templateName) ||
                "extendInfo".equals(templateName);
    }

    /**
     * Splits a rendered template containing multiple classes into individual files.
     */
    private List<GeneratedFile> splitMultiClassOutput(String content, String templateName, Map<String, Object> context, String projectName) {
        List<GeneratedFile> files = new ArrayList<>();
        List<String> headerLines = new ArrayList<>();

        StringBuilder currentClassBuilder = null;
        String currentClassName = null;
        boolean headerEnded = false;

        for (String line : content.split("\\r?\\n")) {
            Matcher matcher = CLASS_DECLARATION_PATTERN.matcher(line.trim());

            if (matcher.find()) {
                headerEnded = true;
                // Save the previous class if exists
                if (currentClassBuilder != null && currentClassName != null) {
                    files.add(buildClassFile(headerLines, currentClassBuilder, currentClassName, context, projectName));
                }
                // Start new class
                currentClassName = matcher.group(1);
                currentClassBuilder = new StringBuilder().append(line).append("\n");
            } else if (!headerEnded) {
                if (!line.trim().isEmpty()) {
                    headerLines.add(line);
                }
            } else {
                // At this point, headerEnded is true, meaning we have already encountered
                // at least one class declaration, so currentClassBuilder is guaranteed to be non-null.
                currentClassBuilder.append(line).append("\n");
            }
        }

        // Save the last class
        if (currentClassBuilder != null && currentClassName != null) {
            files.add(buildClassFile(headerLines, currentClassBuilder, currentClassName, context, projectName));
        }

        // Fallback if no classes were found
        if (files.isEmpty()) {
            files.add(new GeneratedFile(generateFilePath(templateName, context, projectName), content));
        }
        return files;
    }

    /**
     * Assembles the final file content and generates the path for a single class.
     */
    private GeneratedFile buildClassFile(List<String> headerLines, StringBuilder classContent, String className,
                                         Map<String, Object> context, String projectName) {
        StringBuilder finalContent = new StringBuilder();
        headerLines.forEach(header -> finalContent.append(header).append("\n"));
        if (!headerLines.isEmpty()) {
            finalContent.append("\n");
        }
        finalContent.append(classContent);

        String filePath = generateMultiClassFilePath(className, context, projectName);
        return new GeneratedFile(filePath, finalContent.toString());
    }

    /**
     * Generates the file path for a split class.
     * All multi-class templates currently reside in the same domain module directory.
     */
    private String generateMultiClassFilePath(String className, Map<String, Object> context, String projectName) {
        String moduleName = getStr(context, "moduleName");
        String packagePath = getStr(context, "packageName").replace('.', '/');
        return projectName + DOMAIN_SUFFIX + SRC_MAIN_JAVA + packagePath + "/domain/" + moduleName + "/" + className + ".java";
    }

    // ==========================================
    // Template Rendering & Utilities
    // ==========================================

    /**
     * Renders a FreeMarker template with the given data model.
     */
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

    private String safeGetAggregateName(List<GenAggregateView> aggregateViews, int objectType) {
        if (aggregateViews == null || aggregateViews.isEmpty()) {
            return "Unknown";
        }
        return aggregateViews.stream()
                .filter(q -> q.getObjectType() == objectType && Boolean.TRUE.equals(q.getHasCreated()))
                .map(GenAggregateView::getObjectName)
                .findFirst()
                .orElse("Unknown");
    }

    private ProjectTreeView findOrCreateRootNode(String rootLabel, List<ProjectTreeView> treeList) {
        return treeList.stream()
                .filter(tree -> rootLabel.equals(tree.getLabel()))
                .findFirst()
                .orElseGet(() -> {
                    ProjectTreeView newRoot = new ProjectTreeView();
                    newRoot.setLabel(rootLabel);
                    newRoot.setChildren(new ArrayList<>());
                    treeList.add(newRoot);
                    return newRoot;
                });
    }

    private String getStr(Map<String, Object> map, String key) {
        return Objects.toString(map.get(key), "");
    }

    private String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // Internal record to hold generated file data
    private record GeneratedFile(String filePath, String content) {
    }
}