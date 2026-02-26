package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleCommand;
import com.springddd.domain.role.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SysRoleCommandService {

    private final SysRoleDomainRepository sysRoleDomainRepository;

    private final SysRoleDomainFactory sysRoleDomainFactory;

    private final WipeSysRoleByIdsDomainService wipeSysRoleByIdsDomainService;

    private final DeleteSysRoleByIdDomainService deleteSysRoleByIdDomainService;

    private final RestoreSysRoleByIdDomainService restoreSysRoleByIdDomainService;

    public Mono<Long> createRole(SysRoleCommand command) {
        RoleBasicInfo roleBasicInfo = new RoleBasicInfo(command.getRoleName(), command.getRoleCode(), command.getDataScope(), command.getOwnerStatus());

        RoleExtendInfo roleExtendInfo = new RoleExtendInfo(command.getRoleDesc(), command.getRoleStatus());

        SysRoleDomain domain = sysRoleDomainFactory.newInstance(new RoleId(command.getId()), roleBasicInfo, roleExtendInfo, command.getDeptId());
        domain.create();

        return sysRoleDomainRepository.save(domain);
    }

    public Mono<Void> updateRole(SysRoleCommand command) {
        return sysRoleDomainRepository.load(new RoleId(command.getId())).flatMap(domain -> {

            RoleBasicInfo roleBasicInfo = new RoleBasicInfo(command.getRoleName(), command.getRoleCode(), command.getDataScope(), command.getOwnerStatus());

            RoleExtendInfo roleExtendInfo = new RoleExtendInfo(command.getRoleDesc(), command.getRoleStatus());

            domain.updateRole(roleBasicInfo, roleExtendInfo, command.getDeptId());
            return sysRoleDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> deleteRole(List<Long> ids) {
        return deleteSysRoleByIdDomainService.deleteByIds(ids);
    }

    public Mono<Void> restore(List<Long> ids) {
        return restoreSysRoleByIdDomainService.restoreByIds(ids);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeSysRoleByIdsDomainService.deleteByIds(ids);
    }
}
