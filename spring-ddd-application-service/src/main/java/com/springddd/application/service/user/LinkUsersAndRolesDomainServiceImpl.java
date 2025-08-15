package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LinkUsersAndRolesDomainServiceImpl implements LinkUsersAndRolesDomainService {

    private final SysUserRoleQueryService sysUserRoleQueryService;

    private final WipeSysUserRoleByIdsDomainService wipeSysUserRoleByIdsDomainService;

    private final SysUserRoleDomainRepository sysUserRoleDomainRepository;

    private final SysUserRoleDomainFactory sysUserRoleDomainFactory;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> link(Long userId, List<Long> roleIds) {
        return sysUserRoleQueryService.queryLinkUserAndRole(userId).flatMap(sysUserRoleViews -> {
            List<Long> ids = sysUserRoleViews.stream().map(SysUserRoleView::getId).toList();
            Mono<Void> wiped = wipeSysUserRoleByIdsDomainService.deleteByIds(ids);

            List<Mono<Long>> saved = roleIds.stream()
                    .map(rid -> {
                        SysUserRoleDomain domain = sysUserRoleDomainFactory.newInstance(
                                new UserId(userId),
                                new RoleId(rid),
                                null
                        );
                        domain.create();
                        return sysUserRoleDomainRepository.save(domain);
                    })
                    .toList();

            return wiped.then(Mono.when(saved)).then();
        });
    }
}
