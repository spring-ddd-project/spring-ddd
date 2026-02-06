package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictCommand;
import com.springddd.domain.dict.*;
import com.springddd.infrastructure.persistence.factory.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysDictCommandService {

    private final RepositoryFactory repositoryFactory;

    private final SysDictDomainFactory sysDictDomainFactory;

    private final WipeSysDictByIdsDomainService wipeSysDictByIdsDomainService;

    private final DeleteSysDictByIdDomainService deleteSysDictByIdDomainService;

    private final RestoreSysDictByIdDomainService restoreSysDictByIdDomainService;

    public Mono<Long> create(SysDictCommand command) {
        DictBasicInfo basicInfo = new DictBasicInfo(command.getDictName(), command.getDictCode());
        DictExtendInfo extendInfo = new DictExtendInfo(command.getSortOrder(), command.getDictStatus());
        SysDictDomain sysDictDomain = sysDictDomainFactory.newInstance(basicInfo, extendInfo);
        sysDictDomain.create();
        return repositoryFactory.getSysDictDomainRepository().save(sysDictDomain);
    }

    public Mono<Void> update(SysDictCommand command) {
        return repositoryFactory.getSysDictDomainRepository().load(new DictId(command.getId())).flatMap(domain -> {
            DictBasicInfo basicInfo = new DictBasicInfo(command.getDictName(), command.getDictCode());
            DictExtendInfo extendInfo = new DictExtendInfo(command.getSortOrder(), command.getDictStatus());

            domain.update(basicInfo, extendInfo);
            return repositoryFactory.getSysDictDomainRepository().save(domain);
        }).then();
    }

    public Mono<Void> delete(List<Long> ids) {
        return deleteSysDictByIdDomainService.deleteByIds(ids);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeSysDictByIdsDomainService.deleteByIds(ids);
    }

    public Mono<Void> restore(List<Long> ids) {
        return restoreSysDictByIdDomainService.restoreByIds(ids);
    }
}





























