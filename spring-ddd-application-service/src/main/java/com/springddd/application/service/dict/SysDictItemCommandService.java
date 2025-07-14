package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.SysDictItemCommand;
import com.springddd.domain.dict.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysDictItemCommandService {

    private final SysDictItemDomainRepository sysDictItemDomainRepository;

    private final SysDictItemDomainFactory sysDictItemDomainFactory;

    private final WipeSysDictItemByIdsDomainService wipeSysDictItemByIdsDomainService;

    private final DeleteSysDictItemByIdDomainService deleteSysDictItemByIdDomainService;

    private final RestoreSysDictItemByIdDomainService restoreSysDictItemByIdDomainService;

    public Mono<Long> create(SysDictItemCommand command) {
        DictItemBasicInfo basicInfo = new DictItemBasicInfo(new ItemLabel(command.getItemLabel()), new ItemValue(command.getItemValue()));
        DictItemExtendInfo extendInfo = new DictItemExtendInfo(command.getSortOrder(), command.getItemStatus());

        SysDictItemDomain domain = sysDictItemDomainFactory.newInstance(new DictId(command.getDictId()), basicInfo, extendInfo);
        domain.create();

        return sysDictItemDomainRepository.save(domain);
    }

    public Mono<Void> update(SysDictItemCommand command) {
        return sysDictItemDomainRepository.load(new DictItemId(command.getId())).flatMap(domain -> {
            DictItemBasicInfo basicInfo = new DictItemBasicInfo(new ItemLabel(command.getItemLabel()), new ItemValue(command.getItemValue()));
            DictItemExtendInfo extendInfo = new DictItemExtendInfo(command.getSortOrder(), command.getItemStatus());

            domain.update(basicInfo, extendInfo);

            return sysDictItemDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> delete(List<Long> ids) {
        return deleteSysDictItemByIdDomainService.deleteByIds(ids);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeSysDictItemByIdsDomainService.deleteByIds(ids);
    }

    public Mono<Void> restore(List<Long> ids) {
        return restoreSysDictItemByIdDomainService.restoreByIds(ids);
    }
}
