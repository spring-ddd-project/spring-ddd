package com.springddd.application.service.gen.strategy;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Set;

@Component
public class InfrastructurePersistenceFilePathStrategy implements FilePathStrategy {
    private static final Set<String> SUPPORTED = Set.of("entity", "r2dbc", "domainRepositoryImpl");

    @Override
    public boolean supports(String templateName) {
        return SUPPORTED.contains(templateName);
    }

    @Override
    public String generatePath(String templateName, Map<String, Object> context, String projectName) {
        String packageName = (String) context.get("packageName");
        String className = (String) context.get("className");
        String srcPath = "src/main/java/";
        String packagePath = packageName.replace('.', '/');

        return switch (templateName) {
            case "entity" -> projectName + "-infrastructure-persistence/" + srcPath
                    + packagePath + "/infrastructure/persistence/entity/"
                    + className + "Entity.java";
            case "r2dbc" -> projectName + "-infrastructure-persistence/" + srcPath
                    + packagePath + "/infrastructure/persistence/r2dbc/"
                    + className + "Repository.java";
            case "domainRepositoryImpl" -> projectName + "-infrastructure-persistence/" + srcPath
                    + packagePath + "/infrastructure/persistence/"
                    + className + "DomainRepositoryImpl.java";
            default -> null;
        };
    }
}




















































