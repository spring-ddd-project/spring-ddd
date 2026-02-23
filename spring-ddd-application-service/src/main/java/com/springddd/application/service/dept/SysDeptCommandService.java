package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptCommand;
import com.springddd.domain.dept.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysDeptCommandService {

    private final SysDeptDomainRepository sysDeptDomainRepository;

    private final SysDeptDomainFactory sysDeptDomainFactory;

    private final WipeSysDeptByIdsDomainService wipeSysDeptByIdsDomainService;

    private final DeleteSysDeptByIdDomainService deleteSysDeptByIdDomainService;

    private final RestoreSysDeptByIdDomainService restoreSysDeptByIdDomainService;

    public Mono<Long> create(SysDeptCommand command) {
        DeptBasicInfo basicInfo = new DeptBasicInfo(command.getDeptName());
        DeptExtendInfo extendInfo = new DeptExtendInfo(command.getSortOrder(), command.getDeptStatus());

        SysDeptDomain sysDeptDomain = sysDeptDomainFactory.newInstance(new DeptId(command.getParentId()), basicInfo, extendInfo);
        sysDeptDomain.create();

        return sysDeptDomainRepository.save(sysDeptDomain);
    }

    public Mono<Void> update(SysDeptCommand command) {
        return sysDeptDomainRepository.load(new DeptId(command.getId())).flatMap(domain -> {
            DeptBasicInfo basicInfo = new DeptBasicInfo(command.getDeptName());
            DeptExtendInfo extendInfo = new DeptExtendInfo(command.getSortOrder(), command.getDeptStatus());

            domain.update(new DeptId(command.getParentId()), basicInfo, extendInfo);
            return sysDeptDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> delete(List<Long> ids) {
        return deleteSysDeptByIdDomainService.deleteByIds(ids);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeSysDeptByIdsDomainService.deleteByIds(ids);
    }

    public Mono<Void> restore(List<Long> ids) {
        return restoreSysDeptByIdDomainService.restoreByIds(ids);
    }
}
