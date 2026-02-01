package com.springddd.application.service.gen.strategy;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class FilePathContext {
    private final List<FilePathStrategy> strategies;

    public FilePathContext(List<FilePathStrategy> strategies) {
        this.strategies = strategies;
    }

    public String getFilePath(String templateName, Map<String, Object> context, String projectName) {
        return strategies.stream()
                .filter(strategy -> strategy.supports(templateName))
                .findFirst()
                .map(strategy -> strategy.generatePath(templateName, context, projectName))
                .orElse(context.get("className") + ".txt");
    }
}




















