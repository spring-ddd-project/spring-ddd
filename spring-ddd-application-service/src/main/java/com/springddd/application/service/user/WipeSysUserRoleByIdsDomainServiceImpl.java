package com.springddd.application.service.user;

import com.springddd.domain.user.WipeSysUserRoleByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeSysUserRoleByIdsDomainServiceImpl implements WipeSysUserRoleByIdsDomainService {

    private final SysUserRoleRepository sysUserRoleRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> deleteByIds(List<Long> ids) {
        return sysUserRoleRepository.deleteAllById(ids);
    }
}
