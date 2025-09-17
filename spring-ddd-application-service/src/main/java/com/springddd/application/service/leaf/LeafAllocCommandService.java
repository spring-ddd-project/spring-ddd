package com.springddd.application.service.leaf;

import com.springddd.application.service.leaf.dto.LeafAllocCommand;
import com.springddd.domain.leaf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeafAllocCommandService {

    private final LeafAllocDomainRepository leafAllocDomainRepository;

    private final LeafAllocDomainFactory leafAllocDomainFactory;

    private final DeleteLeafAllocDomainService deleteLeafAllocDomainService;
    
    private final RestoreLeafAllocDomainService restoreLeafAllocDomainService;

    private final WipeLeafAllocDomainService wipeLeafAllocDomainService;

    private final UpdateLeafAllocMaxIdByTagDomainService updateLeafAllocMaxIdByTagDomainService;

    public Mono<Long> create(LeafAllocCommand command) {
        LeafProp leafProp = new LeafProp(command.getBizTag(), command.getStep(), command.getMaxId());
        ExtendInfo extendInfo = new ExtendInfo(command.getDescription());

        LeafAllocDomain domain = leafAllocDomainFactory.newInstance(leafProp, extendInfo);
        domain.create();
        return leafAllocDomainRepository.save(domain);
    }

    public Mono<Void> update(LeafAllocCommand command) {
        LeafProp leafProp = new LeafProp(command.getBizTag(), command.getStep(), command.getMaxId());
        ExtendInfo extendInfo = new ExtendInfo(command.getDescription());

        return leafAllocDomainRepository.load(new LeafId(command.getId()))
                .flatMap(domain -> {
                    domain.update(leafProp, extendInfo);
                    return leafAllocDomainRepository.save(domain);
                }).then();
    }

    public Mono<Void> delete(List<Long> ids) {
        return deleteLeafAllocDomainService.deleteByIds(ids);
    }
    
    public Mono<Void> restore(List<Long> ids) {
        return restoreLeafAllocDomainService.restoreByIds(ids);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeLeafAllocDomainService.wipeByIds(ids);
    }

    public Mono<Void> updateMaxId(LeafAllocCommand command) {
        return updateLeafAllocMaxIdByTagDomainService.updateMaxIdByTag(command.getBizTag());
    }
}
