package com.springddd.application.service.menu;

import com.springddd.domain.menu.DeleteSysMenuByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysMenuByIdsDomainServiceImpl implements DeleteSysMenuByIdsDomainService {

    private final SysMenuRepository sysMenuRepository;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return sysMenuRepository.deleteAllById(ids);
    }
}
