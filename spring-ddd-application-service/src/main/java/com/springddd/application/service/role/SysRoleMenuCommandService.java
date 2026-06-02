package com.springddd.application.service.role;

import com.springddd.domain.role.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleMenuCommandService {

    private final LinkRoleAndMenusDomainService linkRoleAndMenusDomainService;

    private final WipeSysRoleMenuByIdsDomainService wipeSysRoleMenuByIdsDomainService;

    public Mono<Void> create(Long roleId, List<Long> menuIds) {
        return linkRoleAndMenusDomainService.link(roleId, menuIds);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeSysRoleMenuByIdsDomainService.deleteByIds(ids);
    }
}
