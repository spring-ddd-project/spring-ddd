package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuDataScopeCommand;
import com.springddd.domain.role.*;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleMenuDataScopeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleMenuDataScopeCommandService {

    private final SysRoleMenuDataScopeDomainRepository sysRoleMenuDataScopeDomainRepository;

    private final SysRoleMenuDataScopeRepository sysRoleMenuDataScopeRepository;

    private final SysRoleMenuDataScopeDomainFactory sysRoleMenuDataScopeDomainFactory;

    private final WipeSysRoleMenuDataScopeByIdsDomainService wipeSysRoleMenuDataScopeByIdsDomainService;

    private final DeleteSysRoleMenuDataScopeByIdsDomainService deleteSysRoleMenuDataScopeByIdsDomainService;

    private final RestoreSysRoleMenuDataScopeByIdsDomainService restoreSysRoleMenuDataScopeByIdsDomainService;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> batchSave(Long roleId, List<SysRoleMenuDataScopeCommand> items) {
        return deleteAllByRoleId(roleId)
                .thenMany(Flux.fromIterable(items))
                .flatMap(item -> {
                    RoleMenuDataScopeInfo info = new RoleMenuDataScopeInfo(roleId, item.getMenuId(), item.getDataScope());
                    SysRoleMenuDataScopeDomain domain = sysRoleMenuDataScopeDomainFactory.newInstance(info);
                    domain.create();
                    return sysRoleMenuDataScopeDomainRepository.save(domain);
                })
                .then();
    }

    private Mono<Void> deleteAllByRoleId(Long roleId) {
        return sysRoleMenuDataScopeRepository.findByRoleIdAndDeleteStatusFalse(roleId)
                .flatMap(entity -> sysRoleMenuDataScopeDomainRepository.load(new RoleMenuDataScopeId(entity.getId()))
                        .flatMap(domain -> {
                            domain.delete();
                            return sysRoleMenuDataScopeDomainRepository.save(domain);
                        }))
                .then();
    }

    public Mono<Void> delete(List<Long> ids) {
        return deleteSysRoleMenuDataScopeByIdsDomainService.deleteByIds(ids);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeSysRoleMenuDataScopeByIdsDomainService.wipeByIds(ids);
    }

    public Mono<Void> restore(List<Long> ids) {
        return restoreSysRoleMenuDataScopeByIdsDomainService.restoreByIds(ids);
    }
}
