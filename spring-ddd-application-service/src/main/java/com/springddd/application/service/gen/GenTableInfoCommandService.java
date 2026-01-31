package com.springddd.application.service.gen;

import com.springddd.domain.gen.WipeGenDataDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GenTableInfoCommandService {

    private final WipeGenDataDomainService wipeGenDataDomainService;

    public Mono<Void> wipe() {
        return wipeGenDataDomainService.wipe();
    }
}
