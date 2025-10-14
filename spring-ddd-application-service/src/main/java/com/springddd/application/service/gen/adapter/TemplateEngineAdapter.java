package com.springddd.application.service.gen.adapter;

import java.util.Map;
import reactor.core.publisher.Mono;

public interface TemplateEngineAdapter {
    Mono<String> render(String templateName, String templateContent, Map<String, Object> dataModel);
}








