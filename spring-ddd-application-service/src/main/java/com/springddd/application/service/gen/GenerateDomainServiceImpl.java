package com.springddd.application.service.gen;

import com.springddd.application.service.gen.adapter.TemplateEngineAdapter;
import com.springddd.application.service.gen.dto.GenAggregateView;
import com.springddd.application.service.gen.dto.ProjectTreeBuilder;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.application.service.gen.strategy.FilePathContext;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.gen.GenerateDomainService;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.CacheProcessor;
import com.springddd.application.service.gen.factory.TemplateEngineFactory;
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

    private final TemplateEngineFactory templateEngineFactory;

    private final ProjectTreeBuilder treeBuilder = ProjectTreeBuilder.getInstance();

    private final CacheProcessor cacheProcessor;

    private final FilePathContext filePathContext;


    @Override
    public Mono<Void> generate(String tableName) {
        return genTableInfoQueryService.buildData(tableName)
                .flatMap(context -> {
                    String projectName = (String) context.get("projectName");
                    return templateQueryService.queryAllTemplate()
                            .flatMapMany(Flux::fromIterable)
                            .flatMap(template -> templateEngineFactory.createEngineAdapter().render(template.getTemplateName(),
                                    template.getTemplateContent(), context)
                                    .map(renderedCode -> {
                                        String filePath = filePathContext.getFilePath(template.getTemplateName(), context, projectName);
                                        return new GeneratedFile(filePath, renderedCode);
                                    }))
                            .collectList()
                            .flatMap(generatedFiles -> {
                                List<ProjectTreeView> treeList = new ArrayList<>();
                                generatedFiles.forEach(file -> saveGeneratedFile(file.filePath, file.content, treeList, projectName));
                                return cacheProcessor.setCache(CacheKeys.GEN_FILES.buildKey(SecurityUtils.getUserId()), treeList,
                                        CacheKeys.GEN_FILES.ttl()).then();
                            });
                });
    }

    private record GeneratedFile(String filePath, String content) {}

    /**
     * build location
     * projectName-application-infrastructure.persistence/packageName/infrastructure/persistence/entity/ClassNameEntity.java
     */
    private String generateFilePath(String templateName, Map<String, Object> context, String projectName) {
        return filePathContext.getFilePath(templateName, context, projectName);
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
}



