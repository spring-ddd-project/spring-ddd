package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictCommand;
import com.springddd.domain.dict.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysDictCommandService {

    private final SysDictDomainRepository sysDictDomainRepository;

    private final SysDictDomainFactory sysDictDomainFactory;

    private final WipeSysDictByIdsDomainService wipeSysDictByIdsDomainService;

    private final DeleteSysDictByIdDomainService deleteSysDictByIdDomainService;

    public Mono<Long> create(SysDictCommand command) {
        DictBasicInfo basicInfo = new DictBasicInfo(new DictName(command.getDictName()), new DictCode(command.getDictCode()));
        DictExtendInfo extendInfo = new DictExtendInfo(command.getSortOrder(), command.getDictStatus());
        SysDictDomain sysDictDomain = sysDictDomainFactory.newInstance(basicInfo, extendInfo);
        sysDictDomain.create();
        return sysDictDomainRepository.save(sysDictDomain);
    }

    public Mono<Void> update(SysDictCommand command) {
        return sysDictDomainRepository.load(new DictId(command.getId())).flatMap(domain -> {
            DictBasicInfo basicInfo = new DictBasicInfo(new DictName(command.getDictName()), new DictCode(command.getDictCode()));
            DictExtendInfo extendInfo = new DictExtendInfo(command.getSortOrder(), command.getDictStatus());

            domain.update(basicInfo, extendInfo);
            return sysDictDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> delete(List<Long> ids) {
        return deleteSysDictByIdDomainService.deleteByIds(ids);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeSysDictByIdsDomainService.deleteByIds(ids);
    }
}
