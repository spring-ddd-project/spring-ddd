package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.user.WipeSysUserByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeSysUserByIdsDomainServiceImpl implements WipeSysUserByIdsDomainService {

    private final SysUserRepository sysUserRepository;

    private final SysUserRoleQueryService sysUserRoleQueryService;

    private final SysUserRoleCommandService sysUserRoleCommandService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> deleteByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(userId -> sysUserRoleQueryService.queryLinkUserAndRole(userId)
                        .flatMap(ur -> {
                            List<Long> urIds = ur.stream().map(SysUserRoleView::getId).toList();
                            if (CollectionUtils.isEmpty(urIds)) {
                                return Mono.empty();
                            }
                            return sysUserRoleCommandService.wipe(urIds);
                        }), SecurityUtils.concurrency())
                .thenMany(sysUserRepository.deleteAllById(ids)).then();
    }
}
