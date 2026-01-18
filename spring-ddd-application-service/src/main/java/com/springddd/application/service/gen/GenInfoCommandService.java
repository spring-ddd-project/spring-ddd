package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenInfoCommand;
import com.springddd.domain.gen.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class GenInfoCommandService {

    private final GenInfoDomainRepository genInfoDomainRepository;

    private final GenInfoDomainFactory genInfoDomainFactory;

    public Mono<Long> create(GenInfoCommand command) {
        GenInfoBasicInfo basicInfo = new GenInfoBasicInfo(new TableName(command.getTableName()), new PackageName(command.getPackageName()), new ClassName(command.getClassName()));
        GenInfoExtendInfo extendInfo = new GenInfoExtendInfo((command.getRequestName()));
        GenInfoDomain genInfoDomain = genInfoDomainFactory.newInstance(basicInfo, extendInfo);
        genInfoDomain.create();
        return genInfoDomainRepository.save(genInfoDomain);
    }

    public Mono<Void> update(GenInfoCommand command) {
        return genInfoDomainRepository.load(new GenInfoId(command.getId())).flatMap(domain -> {
            GenInfoBasicInfo basicInfo = new GenInfoBasicInfo(new TableName(command.getTableName()), new PackageName(command.getPackageName()), new ClassName(command.getClassName()));
            GenInfoExtendInfo extendInfo = new GenInfoExtendInfo((command.getRequestName()));
            domain.update(basicInfo, extendInfo);
            return genInfoDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> delete(GenInfoCommand command) {
        return genInfoDomainRepository.load(new GenInfoId(command.getId())).flatMap(domain -> {
            domain.delete();
            return genInfoDomainRepository.save(domain);
        }).then();
    }
}
