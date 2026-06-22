package com.springddd.application.service.leaf;

import com.springddd.application.service.leaf.dto.LeafWorkerCommand;
import com.springddd.domain.leaf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeafWorkerCommandService {

    private final LeafWorkerDomainRepository leafWorkerDomainRepository;

    private final LeafWorkerDomainFactory leafWorkerDomainFactory;

    private final DeleteLeafWorkerDomainService deleteLeafWorkerDomainService;
    
    private final RestoreLeafWorkerDomainService restoreLeafWorkerDomainService;

    private final WipeLeafWorkerDomainService wipeLeafWorkerDomainService;

    public Mono<Long> create(LeafWorkerCommand command) {
        Worker worker = new Worker(command.getWorkerId(), command.getDatacenterId());
        Address address = new Address(command.getIp(), command.getPort());
        ExtendInfo extendInfo = new ExtendInfo(command.getLastTimestamp(), null, command.getDeleteStatus());

        LeafWorkerDomain domain = leafWorkerDomainFactory.newInstance(worker, address, extendInfo);
        domain.create();
        return leafWorkerDomainRepository.save(domain);
    }

    public Mono<Void> update(LeafWorkerCommand command) {
        Worker worker = new Worker(command.getWorkerId(), command.getDatacenterId());
        Address address = new Address(command.getIp(), command.getPort());
        ExtendInfo extendInfo = new ExtendInfo(command.getLastTimestamp(), null, command.getDeleteStatus());

        return leafWorkerDomainRepository.load(new LeafId(command.getId()))
                .flatMap(domain -> {
                    domain.update(worker, address, extendInfo);
                    return leafWorkerDomainRepository.save(domain);
                }).then();
    }

    public Mono<Void> delete(List<Long> ids) {
        return deleteLeafWorkerDomainService.deleteByIds(ids);
    }
    
    public Mono<Void> restore(List<Long> ids) {
        return restoreLeafWorkerDomainService.restoreByIds(ids);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeLeafWorkerDomainService.wipeByIds(ids);
    }
}
