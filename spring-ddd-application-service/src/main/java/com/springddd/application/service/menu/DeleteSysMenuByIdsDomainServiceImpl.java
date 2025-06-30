package com.springddd.application.service.menu;

import com.springddd.domain.menu.DeleteSysMenuByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysMenuByIdsDomainServiceImpl implements DeleteSysMenuByIdsDomainService {

    private final SysMenuRepository sysMenuRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> deleteByIds(List<Long> ids) {
        return sysMenuRepository.deleteAllById(ids);
    }
}
