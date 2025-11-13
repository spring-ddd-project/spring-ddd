package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.UpdateCacheDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class UpdateCacheDomainServiceImpl implements UpdateCacheDomainService {

    private final LeafAllocQueryService leafAllocQueryService;

    @Override
    public Mono<Void> updateCache() {
        log.info("update cache from db");
        StopWatch sw = new StopWatch();
        return leafAllocQueryService.getAllLeafAlloc()
                .filter(Objects::nonNull)
                .flatMap()
                ;
    }
}
