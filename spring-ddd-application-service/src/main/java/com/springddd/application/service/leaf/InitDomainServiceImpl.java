package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.InitDomainService;
import com.springddd.domain.leaf.UpdateCacheDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitDomainServiceImpl implements InitDomainService {

    private final UpdateCacheDomainService updateCacheDomainService;

    @Override
    public Mono<Boolean> init() {
        log.info("Init ...");
        return updateCacheDomainService.updateCache()
                .then(Mono.just(true));
    }
}
