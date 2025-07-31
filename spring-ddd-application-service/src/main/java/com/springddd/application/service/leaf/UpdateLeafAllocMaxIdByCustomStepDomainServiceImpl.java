package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.LeafAllocDomain;
import com.springddd.domain.leaf.LeafAllocDomainRepository;
import com.springddd.domain.leaf.LeafProp;
import com.springddd.domain.leaf.UpdateLeafAllocMaxIdByCustomStepDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UpdateLeafAllocMaxIdByCustomStepDomainServiceImpl implements UpdateLeafAllocMaxIdByCustomStepDomainService {

    private final LeafAllocQueryService leafAllocQueryService;

    private final LeafAllocDomainRepository leafAllocDomainRepository;

    @Override
    public Mono<Void> updateMaxIdByCustomStep(LeafAllocDomain domain) {
        return leafAllocQueryService.getLeafAllocByTag(domain.getLeafProp().bizTag())
                .flatMap(v -> {
                    LeafProp prop = new LeafProp(v.getBizTag(), v.getStep(), v.getMaxId());
                    domain.updateMaxIdByCustomStep(prop);
                    return leafAllocDomainRepository.save(domain);
                }).then();
    }
}
