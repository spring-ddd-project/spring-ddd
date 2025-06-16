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
        RoleBasicInfo roleBasicInfo = new RoleBasicInfo();
        roleBasicInfo.setRoleName(new RoleName(command.getRoleName()));
        roleBasicInfo.setRoleCode(new RoleCode(command.getRoleCode()));
        roleBasicInfo.setRoleDataScope(new RoleDataScope(command.getDataScope()));

        RoleExtendInfo roleExtendInfo = new RoleExtendInfo();
        roleExtendInfo.setRoleDesc(command.getRoleDesc());
        roleExtendInfo.setRoleStatus(command.getRoleStatus());

        SysRoleDomain domain = sysRoleDomainFactory.newInstance(new RoleId(command.getId()), roleBasicInfo, roleExtendInfo, command.getDeptId(), "TODO");
        domain.create();

        return sysRoleDomainRepository.save(domain);
    }

    public Mono<Void> updateRole(SysRoleCommand command) {
        return sysRoleDomainRepository.load(new RoleId(command.getId())).flatMap(domain -> {

            RoleBasicInfo roleBasicInfo = new RoleBasicInfo();
            roleBasicInfo.setRoleName(new RoleName(command.getRoleName()));
            roleBasicInfo.setRoleCode(new RoleCode(command.getRoleCode()));
            roleBasicInfo.setRoleDataScope(new RoleDataScope(command.getDataScope()));

            RoleExtendInfo roleExtendInfo = new RoleExtendInfo();
            roleExtendInfo.setRoleDesc(command.getRoleDesc());
            roleExtendInfo.setRoleStatus(command.getRoleStatus());

            domain.updateRole(roleBasicInfo, roleExtendInfo, command.getDeptId(), "TODO");
            return sysRoleDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> deleteRole(SysRoleCommand sysRoleCommand) {
        return sysRoleDomainRepository.load(new RoleId(sysRoleCommand.getId())).flatMap(domain -> {
            domain.delete("TODO");
            return sysRoleDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> wipe(List<Long> ids) {
        return deleteSysRoleByIdsDomainService.deleteByIds(ids);
    }
}
