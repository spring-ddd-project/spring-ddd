package com.springddd.application.service.leaf;

import com.springddd.application.service.leaf.dto.LeafAllocCommand;
import com.springddd.domain.leaf.*;
import com.springddd.domain.leaf.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LeafAllocCommandService {

    private final LeafAllocDomainRepository leafAllocDomainRepository;
    private final LeafAllocDomainFactory leafAllocDomainFactory;
    private final CreateLeafAllocDomainService createLeafAllocDomainService;
    private final UpdateLeafAllocDomainService updateLeafAllocDomainService;
    private final DeleteLeafAllocByIdDomainService deleteLeafAllocByIdDomainService;
    private final RestoreLeafAllocByIdDomainService restoreLeafAllocByIdDomainService;
    private final WipeLeafAllocByIdDomainService wipeLeafAllocByIdDomainService;

    public Mono<Long> create(LeafAllocCommand command) {
        LeafAllocDomain domain = leafAllocDomainFactory.newInstance(
                new BizTag(command.getBizTag()),
                new MaxId(command.getMaxId()),
                new Step(command.getStep()),
                new Description(command.getDescription()),
                command.getDeptId()
        );
        return createLeafAllocDomainService.create(domain).map(d -> d.getLeafAllocId().value());
    }

    public Mono<Void> update(LeafAllocCommand command) {
        return leafAllocDomainRepository.load(new LeafAllocId(command.getId()))
                .flatMap(domain -> updateLeafAllocDomainService.update(
                        domain,
                        new BizTag(command.getBizTag()),
                        new MaxId(command.getMaxId()),
                        new Step(command.getStep()),
                        new Description(command.getDescription()),
                        command.getDeptId()
                )).then();
    }

    public Mono<Void> delete(Long id) {
        return deleteLeafAllocByIdDomainService.delete(new LeafAllocId(id));
    }

    public Mono<Void> restore(Long id) {
        return restoreLeafAllocByIdDomainService.restore(new LeafAllocId(id));
    }

    public Mono<Void> wipe(Long id) {
        return wipeLeafAllocByIdDomainService.wipe(new LeafAllocId(id));
    }
}
