package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenerateDomainService;
import com.springddd.domain.gen.WipeGenDataDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GenTableInfoCommandService {

    private final WipeGenDataDomainService wipeGenDataDomainService;

    private final GenerateDomainService generateDomainService;

    public Mono<Void> wipe() {
        return wipeGenDataDomainService.wipe();
    }

    public Mono<byte[]> generate(String tableName, String projectName) {
        return generateDomainService.generate(tableName, projectName);
    }
}
