package com.springddd.application.service.leaf;

import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.domain.leaf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteLeafWorkerDomainServiceImpl implements DeleteLeafWorkerDomainService {

    private final LeafWorkerDomainRepository domainRepository;
    
    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> domainRepository.load(new LeafId(id))
                        .flatMap(domain -> {
                            domain.delete();
                            return domainRepository.save(domain);
                        }), ReactiveSecurityUtils.concurrency())
                .then();
    }
}
