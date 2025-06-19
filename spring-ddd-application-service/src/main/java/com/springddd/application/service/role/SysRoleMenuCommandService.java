package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuCommand;
import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleMenuCommandService {

    private final SysRoleMenuDomainRepository sysRoleMenuDomainRepository;

    private final SysRoleMenuDomainFactory sysRoleMenuDomainFactory;

    private final DeleteSysRoleMenuByIdsDomainService deleteSysRoleMenuByIdsDomainService;

    public Mono<Long> create(SysRoleMenuCommand command) {
        SysRoleMenuDomain domain = sysRoleMenuDomainFactory.create(new RoleId(command.getRoleId()),
                new MenuId(command.getMenuId()), command.getDeptId(), "TODO");
        domain.create();
        return sysRoleMenuDomainRepository.save(domain);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return deleteSysRoleMenuByIdsDomainService.deleteByIds(ids);
    }
}
