package com.springddd.application.service.gen.proxy;

import com.springddd.domain.gen.GenerateDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Primary
@Service
public class GenerateDomainServiceProxy implements GenerateDomainService {

    private final GenerateDomainService targetService;

    public GenerateDomainServiceProxy(@org.springframework.beans.factory.annotation.Qualifier("generateDomainServiceImpl") GenerateDomainService targetService) {
        this.targetService = targetService;
    }

    @Override
    public Mono<Void> generate(String tableName) {
        long start = System.currentTimeMillis();
        return targetService.generate(tableName)
                .doFinally(signalType -> log.info("Code generation for {} took {} ms", tableName, System.currentTimeMillis() - start));
    }
}



































