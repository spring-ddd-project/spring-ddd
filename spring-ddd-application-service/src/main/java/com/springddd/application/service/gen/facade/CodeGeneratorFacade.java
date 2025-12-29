package com.springddd.application.service.gen.facade;

import com.springddd.application.service.gen.GenerateDomainService;
import com.springddd.application.service.gen.GenTableInfoQueryService;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CodeGeneratorFacade {
    private final GenerateDomainService generateDomainService;
    private final GenTableInfoQueryService genTableInfoQueryService;

    public Mono<Void> generateCode(String tableName) {
        return generateDomainService.generate(tableName);
    }

    public Mono<List<ProjectTreeView>> previewCode() {
        return genTableInfoQueryService.preview();
    }
}











