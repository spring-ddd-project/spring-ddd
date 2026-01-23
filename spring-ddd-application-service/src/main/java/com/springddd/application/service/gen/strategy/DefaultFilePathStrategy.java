package com.springddd.application.service.gen.strategy;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Set;

@Component
public class DefaultFilePathStrategy implements FilePathStrategy {
    private static final Set<String> SUPPORTED = Set.of("sql", "readme.txt");

    @Override
    public boolean supports(String templateName) {
        return SUPPORTED.contains(templateName) || templateName.endsWith(".txt");
    }

    @Override
    public String generatePath(String templateName, Map<String, Object> context, String projectName) {
        String className = (String) context.get("className");

        return switch (templateName) {
            case "sql" -> "SQL.sql";
            case "readme.txt" -> "readme.txt";
            default -> className + ".txt";
        };
    }
}






























