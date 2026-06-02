package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.LeafAllocDomain;
import com.springddd.domain.leaf.LeafAllocDomainRepository;
import com.springddd.domain.leaf.service.CreateLeafAllocDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreateLeafAllocDomainServiceImpl implements CreateLeafAllocDomainService {

    private final LeafAllocDomainRepository leafAllocDomainRepository;

    @Override
    public Mono<LeafAllocDomain> create(LeafAllocDomain domain) {
        return leafAllocDomainRepository.save(domain).map(id -> {
            domain.setLeafAllocId(new com.springddd.domain.leaf.LeafAllocId(id));
            return domain;
        });
    }
}
