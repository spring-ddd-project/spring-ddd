package com.springddd.application.service.menu;

import com.springddd.domain.menu.DeleteSysMenuByIdDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysMenuByIdDomainServiceImpl implements DeleteSysMenuByIdDomainService {

    private final SysMenuRepository sysMenuRepository;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Mono.empty();
        }
        return sysMenuRepository.softDeleteByIdsWithDescendants(ids);
    }
}
