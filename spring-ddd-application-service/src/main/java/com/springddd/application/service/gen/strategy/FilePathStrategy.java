package com.springddd.application.service.gen.strategy;

import java.util.Map;

public interface FilePathStrategy {
    boolean supports(String templateName);
    String generatePath(String templateName, Map<String, Object> context, String projectName);
}



























