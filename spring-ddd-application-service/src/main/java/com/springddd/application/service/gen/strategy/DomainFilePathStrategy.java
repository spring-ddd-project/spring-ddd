package com.springddd.application.service.gen.strategy;

import com.springddd.application.service.gen.dto.GenAggregateView;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DomainFilePathStrategy implements FilePathStrategy {
    private static final Set<String> SUPPORTED = Set.of(
            "aggregateRoot", "objectValue", "extendInfo", "domain", 
            "factory", "domainRepository", "deleteDomain", "wipeDomain", "restoreDomain"
    );

    @Override
    public boolean supports(String templateName) {
        return SUPPORTED.contains(templateName);
    }

    @Override
    public String generatePath(String templateName, Map<String, Object> context, String projectName) {
        String moduleName = (String) context.get("moduleName");
        String packageName = (String) context.get("packageName");
        String className = (String) context.get("className");
        List<GenAggregateView> aggregateViews = (List<GenAggregateView>) context.get("aggregateViews");

        String srcPath = "src/main/java/";
        String packagePath = packageName.replace('.', '/');

        return switch (templateName) {
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
            default -> null;
        };
    }
}



















