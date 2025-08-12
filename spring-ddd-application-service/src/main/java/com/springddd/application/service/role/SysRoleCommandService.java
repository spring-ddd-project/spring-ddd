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

    private final DeleteSysRoleByIdsDomainService deleteSysRoleByIdsDomainService;

    public Mono<Long> createRole(SysRoleCommand command) {
        RoleBasicInfo roleBasicInfo = new RoleBasicInfo(new RoleName(command.getRoleName()), new RoleCode(command.getRoleCode()), new RoleDataScope(command.getDataScope()));

        RoleExtendInfo roleExtendInfo = new RoleExtendInfo(command.getRoleDesc(), command.getRoleStatus());

        SysRoleDomain domain = sysRoleDomainFactory.newInstance(new RoleId(command.getId()), roleBasicInfo, roleExtendInfo, command.getDeptId());
        domain.create();

        return sysRoleDomainRepository.save(domain);
    }

    public Mono<Void> updateRole(SysRoleCommand command) {
        return sysRoleDomainRepository.load(new RoleId(command.getId())).flatMap(domain -> {

            RoleBasicInfo roleBasicInfo = new RoleBasicInfo(new RoleName(command.getRoleName()), new RoleCode(command.getRoleCode()), new RoleDataScope(command.getDataScope()));

            RoleExtendInfo roleExtendInfo = new RoleExtendInfo(command.getRoleDesc(), command.getRoleStatus());

            domain.updateRole(roleBasicInfo, roleExtendInfo, command.getDeptId());
            return sysRoleDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> deleteRole(SysRoleCommand sysRoleCommand) {
        return sysRoleDomainRepository.load(new RoleId(sysRoleCommand.getId())).flatMap(domain -> {
            domain.delete();
            return sysRoleDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> wipe(List<Long> ids) {
        return deleteSysRoleByIdsDomainService.deleteByIds(ids);
    }
}
