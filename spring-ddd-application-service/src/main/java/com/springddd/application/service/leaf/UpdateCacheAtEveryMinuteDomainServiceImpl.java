package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.UpdateCacheAtEveryMinuteDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UpdateCacheAtEveryMinuteDomainServiceImpl implements UpdateCacheAtEveryMinuteDomainService {


    @Override
    public Mono<Void> updateCacheSchedule() {
        return null;
    }
}
