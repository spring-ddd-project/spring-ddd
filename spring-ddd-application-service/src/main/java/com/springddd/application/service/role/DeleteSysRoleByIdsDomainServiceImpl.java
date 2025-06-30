package com.springddd.application.service.role;

import com.springddd.domain.role.DeleteSysRoleByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysRoleByIdsDomainServiceImpl implements DeleteSysRoleByIdsDomainService {

    private final SysRoleRepository sysRoleRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> deleteByIds(List<Long> ids) {
        return sysRoleRepository.deleteAllById(ids);
    }
}
