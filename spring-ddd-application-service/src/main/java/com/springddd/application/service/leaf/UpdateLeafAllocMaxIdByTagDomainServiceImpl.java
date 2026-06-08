package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.LeafAllocDomainRepository;
import com.springddd.domain.leaf.LeafId;
import com.springddd.domain.leaf.LeafProp;
import com.springddd.domain.leaf.UpdateLeafAllocMaxIdByTagDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UpdateLeafAllocMaxIdByTagDomainServiceImpl implements UpdateLeafAllocMaxIdByTagDomainService {

    private final LeafAllocQueryService leafAllocQueryService;

    private final LeafAllocDomainRepository leafAllocDomainRepository;

    @Override
    public Mono<Void> updateMaxIdByTag(String tag) {
        return leafAllocQueryService.getLeafAllocByTag(tag)
                .flatMap(v -> {
                    LeafProp prop = new LeafProp(v.getBizTag(), v.getStep(), v.getMaxId());
                    return leafAllocDomainRepository.load(new LeafId(v.getId()))
                            .flatMap(domain -> {
                                domain.updateMaxId(prop);
                                return leafAllocDomainRepository.save(domain);
                            });
                }).then();
    }
}
