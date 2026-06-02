package com.springddd.application.service.gen;

import com.springddd.domain.gen.WipeGenDataDomainService;
import com.springddd.infrastructure.persistence.r2dbc.GenAggregateRepository;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnsRepository;
import com.springddd.infrastructure.persistence.r2dbc.GenProjectInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class WipeGenDataDomainServiceImpl implements WipeGenDataDomainService {

    private final GenProjectInfoRepository infoProjectRepository;

    private final GenColumnsRepository columnsRepository;

    private final GenAggregateRepository aggregateRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> wipe() {
        return Mono.when(
                infoProjectRepository.deleteAll(),
                columnsRepository.deleteAll(),
                aggregateRepository.deleteAll()
        );
    }
}
