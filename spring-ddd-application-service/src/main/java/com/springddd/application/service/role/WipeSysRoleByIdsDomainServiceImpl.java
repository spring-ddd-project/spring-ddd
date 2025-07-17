package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.application.service.user.SysUserRoleCommandService;
import com.springddd.application.service.user.SysUserRoleQueryService;
import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.role.WipeSysRoleByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeSysRoleByIdsDomainServiceImpl implements WipeSysRoleByIdsDomainService {

    private final SysRoleRepository sysRoleRepository;

    private final SysUserRoleQueryService sysUserRoleQueryService;

    private final SysUserRoleCommandService sysUserRoleCommandService;

    private final SysRoleMenuQueryService sysRoleMenuQueryService;

    private final SysRoleMenuCommandService sysRoleMenuCommandService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> deleteByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(roleId -> sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(roleId)
                        .flatMap(ur -> {
                            List<Long> urIds = ur.stream().map(SysUserRoleView::getId).toList();
                            if (CollectionUtils.isEmpty(urIds)) {
                                return Mono.empty();
                            }
                            return sysUserRoleCommandService.wipe(urIds);
                        })
                        .then(sysRoleMenuQueryService.queryLinkRoleAndMenus(roleId))
                        .flatMap(rm -> {
                            List<Long> rmIds = rm.stream().map(SysRoleMenuView::getId).toList();
                            if (CollectionUtils.isEmpty(rmIds)) {
                                return Mono.empty();
                            }
                            return sysRoleMenuCommandService.wipe(rmIds);
                        }), SecurityUtils.concurrency())
                .thenMany(sysRoleRepository.deleteAllById(ids)).then();
    }
}
