package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.WipeLeafWorkerDomainService;
import com.springddd.infrastructure.persistence.r2dbc.LeafWorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeLeafWorkerDomainServiceImpl implements WipeLeafWorkerDomainService {

    private final LeafWorkerRepository repository;

    @Override
    public Mono<Void> wipeByIds(List<Long> ids) {
        return repository.deleteAllById(ids);
    }
}