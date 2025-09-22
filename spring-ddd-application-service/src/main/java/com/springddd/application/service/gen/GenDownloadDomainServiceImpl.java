package com.springddd.application.service.gen;

import com.springddd.domain.gen.GenDownloadDomainService;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GenDownloadDomainServiceImpl implements GenDownloadDomainService {

    private final ReactiveRedisCacheHelper cacheHelper;

    @Override
    public Mono<Void> download() {
        return Mono.empty();
    }
}
