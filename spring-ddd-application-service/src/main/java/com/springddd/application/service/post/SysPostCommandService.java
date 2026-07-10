package com.springddd.application.service.post;

import com.springddd.application.service.post.dto.SysPostCommand;
import com.springddd.domain.post.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysPostCommandService {

    private final SysPostDomainRepository sysPostDomainRepository;

    private final SysPostDomainFactory sysPostDomainFactory;

    private final WipeSysPostByIdsDomainService wipeSysPostByIdsDomainService;

    private final DeleteSysPostByIdsDomainService deleteSysPostByIdsDomainService;

    private final RestoreSysPostByIdsDomainService restoreSysPostByIdsDomainService;

    public Mono<Long> create(SysPostCommand command) {
        PostBasicInfo basicInfo = new PostBasicInfo(command.getPostCode(), command.getPostName());
        PostExtendInfo extendInfo = new PostExtendInfo(command.getParentId(), command.getSortOrder(), command.getPostStatus());

        SysPostDomain domain = sysPostDomainFactory.newInstance(basicInfo, extendInfo);
        domain.create();

        return sysPostDomainRepository.save(domain);
    }

    public Mono<Void> update(SysPostCommand command) {
        return sysPostDomainRepository.load(new PostId(command.getId())).flatMap(domain -> {
            PostBasicInfo basicInfo = new PostBasicInfo(command.getPostCode(), command.getPostName());
            PostExtendInfo extendInfo = new PostExtendInfo(command.getParentId(), command.getSortOrder(), command.getPostStatus());

            domain.update(basicInfo, extendInfo);
            return sysPostDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> delete(List<Long> ids) {
        return deleteSysPostByIdsDomainService.deleteByIds(ids);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeSysPostByIdsDomainService.wipeByIds(ids);
    }

    public Mono<Void> restore(List<Long> ids) {
        return restoreSysPostByIdsDomainService.restoreByIds(ids);
    }
}
