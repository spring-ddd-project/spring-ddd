package com.springddd.application.service.gen;

import com.springddd.domain.gen.WipeGenAggregateDomainService;
import com.springddd.infrastructure.persistence.r2dbc.GenAggregateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeGenAggregateDomainServiceImpl implements WipeGenAggregateDomainService {

    private final GenAggregateRepository aggregateRepository;

    @Override
    public Mono<Void> wipe(List<Long> ids) {
        return aggregateRepository.deleteAllById(ids);
    }
}
