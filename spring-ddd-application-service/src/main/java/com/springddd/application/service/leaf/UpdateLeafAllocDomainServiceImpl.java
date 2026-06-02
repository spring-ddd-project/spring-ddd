package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.*;
import com.springddd.domain.leaf.service.UpdateLeafAllocDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdateLeafAllocDomainServiceImpl implements UpdateLeafAllocDomainService {

    private final LeafAllocDomainRepository leafAllocDomainRepository;

    @Override
    public Mono<LeafAllocDomain> update(LeafAllocDomain domain, BizTag bizTag, MaxId maxId, Step step, Description description, Long deptId) {
        domain.update(bizTag, maxId, step, description, deptId);
        return leafAllocDomainRepository.save(domain).then(Mono.just(domain));
    }
}
