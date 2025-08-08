package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenColumnBindCommand;
import com.springddd.domain.gen.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GenColumnBindCommandService {

    private final GenColumnBindDomainRepository genColumnBindDomainRepository;

    private final GenColumnBindDomainFactory genColumnBindDomainFactory;

    public Mono<Long> create(GenColumnBindCommand command) {
        GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo(new ColumnName(command.getColumnName()), new EntityName(command.getEntityName()), new ComponentName(command.getComponentName()));
        GenColumnBindDomain genColumnBindDomain = genColumnBindDomainFactory.newInstance(basicInfo);
        genColumnBindDomain.create();
        return genColumnBindDomainRepository.save(genColumnBindDomain);
    }

    public Mono<Void> update(GenColumnBindCommand command) {
        return genColumnBindDomainRepository.load(new ColumnBindId(command.getId())).flatMap(domain -> {
            GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo(new ColumnName(command.getColumnName()), new EntityName(command.getEntityName()), new ComponentName(command.getComponentName()));
            domain.update(basicInfo);
            return genColumnBindDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> delete(GenColumnBindCommand command) {
        return genColumnBindDomainRepository.load(new ColumnBindId(command.getId())).flatMap(domain -> {
            domain.delete();
            return genColumnBindDomainRepository.save(domain);
        }).then();
    }
}
