package com.springddd.application.service.role;

import com.springddd.domain.role.DeleteSysRoleMenuByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysRoleMenuByIdsDomainServiceImpl implements DeleteSysRoleMenuByIdsDomainService {

    private final SysRoleMenuRepository sysRoleMenuRepository;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return sysRoleMenuRepository.deleteAllById(ids);
    }
}
