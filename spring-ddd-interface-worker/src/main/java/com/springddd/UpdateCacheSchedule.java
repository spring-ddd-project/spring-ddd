package com.springddd;

import com.springddd.domain.leaf.UpdateCacheDomainService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class UpdateCacheSchedule {

    private final UpdateCacheDomainService updateCacheDomainService;

    @PostConstruct
    public void start() {
        Flux.interval(Duration.ZERO, Duration.ofSeconds(60))
                .flatMap(tick -> updateCacheDomainService.updateCache())
                .doOnError(err -> System.err.println("Cache error: " + err))
                .subscribe(
                        result -> System.out.println("Cache update success"),
                        err -> {
                        }
                );
    }

}