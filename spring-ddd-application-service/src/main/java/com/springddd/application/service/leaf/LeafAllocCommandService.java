package com.springddd.application.service.leaf;

import com.springddd.application.service.leaf.dto.LeafAllocCommand;
import com.springddd.domain.leaf.*;
import com.springddd.infrastructure.persistence.factory.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeafAllocCommandService {

    private final RepositoryFactory repositoryFactory;

    public Mono<Long> create(LeafAllocCommand command) {
        LeafAllocBasicInfo basicInfo = new LeafAllocBasicInfo(command.getDescription());
        LeafAllocExtendInfo extendInfo = new LeafAllocExtendInfo(
                command.getMaxId() != null ? command.getMaxId() : 1L,
                command.getStep() != null ? command.getStep() : 100);

        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setLeafAllocId(new LeafAllocId(command.getBizTag()));
        domain.setId(command.getId());
        domain.setBasicInfo(basicInfo);
        domain.setExtendInfo(extendInfo);
        domain.create();

        return repositoryFactory.getLeafAllocDomainRepository().save(domain);
    }

    public Mono<Void> update(LeafAllocCommand command) {
        return repositoryFactory.getLeafAllocDomainRepository().load(new LeafAllocId(command.getBizTag())).flatMap(domain -> {
            LeafAllocBasicInfo basicInfo = new LeafAllocBasicInfo(command.getDescription());
            LeafAllocExtendInfo extendInfo = new LeafAllocExtendInfo(command.getMaxId(), command.getStep());
            domain.update(basicInfo, extendInfo);
            return repositoryFactory.getLeafAllocDomainRepository().save(domain);
        }).then();
    }

    public Mono<Void> delete(List<Long> ids) {
        return Mono.empty();
    }

    public Mono<Void> wipe(List<Long> ids) {
        return Mono.empty();
    }

    public Mono<Void> restore(List<Long> ids) {
        return Mono.empty();
    }
}
