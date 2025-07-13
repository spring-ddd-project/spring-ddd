package com.springddd.application.service.user;

import com.springddd.domain.user.WipeSysUserByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeSysUserByIdsDomainServiceImpl implements WipeSysUserByIdsDomainService {

    private final SysUserRepository sysUserRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> deleteByIds(List<Long> ids) {
        return sysUserRepository.deleteAllById(ids);
    }
}
