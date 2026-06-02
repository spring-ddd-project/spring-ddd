package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenProjectInfoCommand;
import com.springddd.domain.gen.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenProjectInfoCommandService {

    private final GenProjectInfoDomainRepository genProjectInfoDomainRepository;

    private final GenProjectInfoDomainFactory genProjectInfoDomainFactory;

    private final WipeGenProjectInfoByIdsDomainService wipeGenInfoByIdsDomainService;

    public Mono<Long> create(GenProjectInfoCommand command) {
        ProjectInfo projectInfo = new ProjectInfo(command.getTableName(), command.getPackageName(), command.getClassName(), command.getModuleName(), command.getProjectName());
        GenProjectInfoExtendInfo extendInfo = new GenProjectInfoExtendInfo(command.getRequestName());
        GenProjectInfoDomain genInfoDomain = genProjectInfoDomainFactory.newInstance(projectInfo, extendInfo);
        genInfoDomain.create();
        return genProjectInfoDomainRepository.save(genInfoDomain);
    }

    public Mono<Void> update(GenProjectInfoCommand command) {
        return genProjectInfoDomainRepository.load(new InfoId(command.getId())).flatMap(domain -> {
            ProjectInfo projectInfo = new ProjectInfo(command.getTableName(), command.getPackageName(), command.getClassName(), command.getModuleName(), command.getProjectName());
            GenProjectInfoExtendInfo extendInfo = new GenProjectInfoExtendInfo(command.getRequestName());
            domain.update(projectInfo, extendInfo);
            return genProjectInfoDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> delete(GenProjectInfoCommand command) {
        return genProjectInfoDomainRepository.load(new InfoId(command.getId())).flatMap(domain -> {
            domain.delete();
            return genProjectInfoDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> wipeByIds(List<Long> ids) {
        return wipeGenInfoByIdsDomainService.wipeByIds(ids);
    }
}
