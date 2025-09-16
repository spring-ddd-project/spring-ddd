package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.WipeLeafAllocDomainService;
import com.springddd.infrastructure.persistence.r2dbc.LeafAllocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeLeafAllocDomainServiceImpl implements WipeLeafAllocDomainService {

    private final LeafAllocRepository repository;

    @Override
    public Mono<Void> wipeByIds(List<Long> ids) {
        return repository.deleteAllById(ids);
    }
}