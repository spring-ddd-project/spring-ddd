package com.springddd.application.service.gen.strategy;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Set;

@Component
public class ApplicationServiceFilePathStrategy implements FilePathStrategy {
    private static final Set<String> SUPPORTED = Set.of(
            "command", "query", "view", "mapstruct", "pageQuery", 
            "factoryImpl", "deleteDomainImpl", "wipeDomainImpl", "restoreDomainImpl",
            "commandService", "queryService"
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

        String srcPath = "src/main/java/";
        String packagePath = packageName.replace('.', '/');

        return switch (templateName) {
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
            default -> null;
        };
    }
}


















































