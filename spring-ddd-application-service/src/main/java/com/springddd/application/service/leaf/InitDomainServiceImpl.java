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

    private volatile boolean initOK = false;

    @Override
    public boolean init() {
        log.info("Init ...");
        Mono<Void> updateCache = updateCacheDomainService.updateCache().doOnNext(v -> initOK = true);
        return initOK;
    }
}
