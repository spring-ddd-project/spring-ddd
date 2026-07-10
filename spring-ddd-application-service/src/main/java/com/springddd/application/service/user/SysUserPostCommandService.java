package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserPostCommand;
import com.springddd.domain.user.*;
import com.springddd.infrastructure.persistence.r2dbc.SysUserPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserPostCommandService {

    private final SysUserPostDomainRepository sysUserPostDomainRepository;

    private final SysUserPostRepository sysUserPostRepository;

    private final SysUserPostDomainFactory sysUserPostDomainFactory;

    private final WipeSysUserPostByIdsDomainService wipeSysUserPostByIdsDomainService;

    private final DeleteSysUserPostByIdsDomainService deleteSysUserPostByIdsDomainService;

    private final RestoreSysUserPostByIdsDomainService restoreSysUserPostByIdsDomainService;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> batchSave(Long userId, List<Long> postIds) {
        return sysUserPostRepository.deleteByUserId(userId)
                .thenMany(Flux.fromIterable(postIds))
                .flatMap(postId -> {
                    UserPostInfo info = new UserPostInfo(userId, postId);
                    SysUserPostDomain domain = sysUserPostDomainFactory.newInstance(info);
                    domain.create();
                    return sysUserPostDomainRepository.save(domain);
                })
                .then();
    }

    public Mono<Void> delete(List<Long> ids) {
        return deleteSysUserPostByIdsDomainService.deleteByIds(ids);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeSysUserPostByIdsDomainService.wipeByIds(ids);
    }

    public Mono<Void> restore(List<Long> ids) {
        return restoreSysUserPostByIdsDomainService.restoreByIds(ids);
    }
}
