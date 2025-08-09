package com.springddd.application.service.user;

import com.springddd.domain.user.DeleteSysUserRoleByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysUserRoleByIdsDomainServiceImpl implements DeleteSysUserRoleByIdsDomainService {

    private final SysUserRoleRepository sysUserRoleRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> deleteByIds(List<Long> ids) {
        return sysUserRoleRepository.deleteAllById(ids);
    }
}
