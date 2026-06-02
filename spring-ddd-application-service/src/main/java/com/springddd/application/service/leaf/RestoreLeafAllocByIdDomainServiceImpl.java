package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.LeafAllocDomainRepository;
import com.springddd.domain.leaf.LeafAllocId;
import com.springddd.domain.leaf.exception.LeafAllocNotFoundException;
import com.springddd.domain.leaf.service.RestoreLeafAllocByIdDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class RestoreLeafAllocByIdDomainServiceImpl implements RestoreLeafAllocByIdDomainService {

    private final LeafAllocDomainRepository leafAllocDomainRepository;

    @Override
    public Mono<Void> restore(LeafAllocId id) {
        return leafAllocDomainRepository.load(id)
                .switchIfEmpty(Mono.error(new LeafAllocNotFoundException()))
                .doOnNext(domain -> domain.restore())
                .flatMap(leafAllocDomainRepository::save)
                .then();
    }
}
