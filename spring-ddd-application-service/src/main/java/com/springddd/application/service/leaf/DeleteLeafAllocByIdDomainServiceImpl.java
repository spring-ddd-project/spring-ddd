package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.LeafAllocDomainRepository;
import com.springddd.domain.leaf.LeafAllocId;
import com.springddd.domain.leaf.exception.LeafAllocNotFoundException;
import com.springddd.domain.leaf.service.DeleteLeafAllocByIdDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DeleteLeafAllocByIdDomainServiceImpl implements DeleteLeafAllocByIdDomainService {

    private final LeafAllocDomainRepository leafAllocDomainRepository;

    @Override
    public Mono<Void> delete(LeafAllocId id) {
        return leafAllocDomainRepository.load(id)
                .switchIfEmpty(Mono.error(new LeafAllocNotFoundException()))
                .doOnNext(domain -> domain.delete())
                .flatMap(leafAllocDomainRepository::save)
                .then();
    }
}
