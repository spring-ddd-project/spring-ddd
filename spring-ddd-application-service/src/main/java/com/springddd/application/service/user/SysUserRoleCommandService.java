package com.springddd.application.service.user;

import com.springddd.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserRoleCommandService {

    private final LinkUsersAndRolesDomainService linkUsersAndRolesDomainService;

    private final DeleteSysUserRoleByIdsDomainService deleteSysUserRoleByIdsDomainService;

    public Mono<Void> create(Long userId, List<Long> roleIds) {
        return linkUsersAndRolesDomainService.link(userId, roleIds);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return deleteSysUserRoleByIdsDomainService.deleteByIds(ids);
    }
}
