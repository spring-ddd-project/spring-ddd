package com.springddd.application.service.gen.mediator;

import com.springddd.application.service.gen.GenTableInfoQueryService;
import com.springddd.application.service.gen.GenTemplateQueryService;
import com.springddd.application.service.gen.GenerateDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GenerationMediator {
    private final GenTableInfoQueryService tableInfoService;
    private final GenTemplateQueryService templateService;
    private final GenerateDomainService generator;

    public Mono<Void> performFullGeneration(String tableName) {
        return generator.generate(tableName);
    }
}






















