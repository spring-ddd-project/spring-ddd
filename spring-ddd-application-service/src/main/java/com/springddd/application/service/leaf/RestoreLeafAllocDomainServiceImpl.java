package com.springddd.application.service.leaf;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.leaf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestoreLeafAllocDomainServiceImpl implements RestoreLeafAllocDomainService {

    private final LeafAllocDomainRepository domainRepository;
    
    @Override
    public Mono<Void> restoreByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> domainRepository.load(new LeafId(id))
                        .flatMap(domain -> {
                            domain.restore();
                            return domainRepository.save(domain);
                        }), SecurityUtils.concurrency())
                .then();
    }
}
