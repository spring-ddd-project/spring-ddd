package com.springddd.application.service.menu;

import com.springddd.domain.menu.RestoreSysMenuByIdDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestoreSysMenuByIdDomainServiceImpl implements RestoreSysMenuByIdDomainService {

    private final SysMenuRepository sysMenuRepository;

    @Override
    public Mono<Void> restoreByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Mono.empty();
        }
        return sysMenuRepository.restoreByIdsWithDescendants(ids);
    }
}
