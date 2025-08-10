package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenColumnBindCommand;
import com.springddd.domain.gen.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenColumnBindCommandService {

    private final GenColumnBindDomainRepository genColumnBindDomainRepository;

    private final GenColumnBindDomainFactory genColumnBindDomainFactory;

    private final WipeGenColumnBindByIdsDomainService wipeGenColumnBindByIdsDomainService;

    private final DeleteGenColumnBindDomainService deleteGenColumnBindDomainService;

    private final RestoreGenColumnBindDomainService restoreGenColumnBindDomainService;

    public Mono<Long> create(GenColumnBindCommand command) {
        GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo(new ColumnType(command.getColumnType()), new EntityType(command.getEntityType()), new ComponentType(command.getComponentType()));
        GenColumnBindDomain genColumnBindDomain = genColumnBindDomainFactory.newInstance(basicInfo);
        genColumnBindDomain.create();
        return genColumnBindDomainRepository.save(genColumnBindDomain);
    }

    public Mono<Void> update(GenColumnBindCommand command) {
        return genColumnBindDomainRepository.load(new ColumnBindId(command.getId())).flatMap(domain -> {
            GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo(new ColumnType(command.getColumnType()), new EntityType(command.getEntityType()), new ComponentType(command.getComponentType()));
            domain.update(basicInfo);
            return genColumnBindDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> delete(List<Long> ids) {
        return deleteGenColumnBindDomainService.deleteByIds(ids);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeGenColumnBindByIdsDomainService.wipeByIds(ids);
    }

    public Mono<Void> restore(List<Long> ids) {
        return restoreGenColumnBindDomainService.restoreByIds(ids);
    }
}
