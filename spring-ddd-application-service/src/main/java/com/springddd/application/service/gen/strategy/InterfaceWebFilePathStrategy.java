package com.springddd.application.service.gen.strategy;

import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class InterfaceWebFilePathStrategy implements FilePathStrategy {
    @Override
    public boolean supports(String templateName) {
        return "controller".equals(templateName);
    }

    @Override
    public String generatePath(String templateName, Map<String, Object> context, String projectName) {
        String packageName = (String) context.get("packageName");
        String className = (String) context.get("className");
        String srcPath = "src/main/java/";
        String packagePath = packageName.replace('.', '/');

        return projectName + "-interface-web/" + srcPath
                + packagePath + "/web/"
                + className + "Controller.java";
    }
}




















































