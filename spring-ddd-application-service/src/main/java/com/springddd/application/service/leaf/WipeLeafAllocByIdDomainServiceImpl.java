package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.LeafAllocDomainRepository;
import com.springddd.domain.leaf.LeafAllocId;
import com.springddd.domain.leaf.exception.LeafAllocNotFoundException;
import com.springddd.domain.leaf.service.WipeLeafAllocByIdDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class WipeLeafAllocByIdDomainServiceImpl implements WipeLeafAllocByIdDomainService {

    private final LeafAllocDomainRepository leafAllocDomainRepository;

    @Override
    public Mono<Void> wipe(LeafAllocId id) {
        return leafAllocDomainRepository.load(id)
                .switchIfEmpty(Mono.error(new LeafAllocNotFoundException()))
                .flatMap(domain -> leafAllocDomainRepository.load(id))
                .then();
    }
}
