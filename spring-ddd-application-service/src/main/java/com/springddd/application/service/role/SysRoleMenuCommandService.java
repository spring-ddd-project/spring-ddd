package com.springddd.application.service.role;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleMenuCommandService {

    private final LinkRoleAndMenusDomainService linkRoleAndMenusDomainService;

    private final DeleteSysRoleMenuByIdsDomainService deleteSysRoleMenuByIdsDomainService;

    public Mono<Void> create(RoleId roleId, List<MenuId> menuIds) {
        return linkRoleAndMenusDomainService.link(roleId, menuIds);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return deleteSysRoleMenuByIdsDomainService.deleteByIds(ids);
    }
}
