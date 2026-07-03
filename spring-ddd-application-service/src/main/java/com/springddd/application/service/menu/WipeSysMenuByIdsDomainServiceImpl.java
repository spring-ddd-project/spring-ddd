package com.springddd.application.service.menu;

import com.springddd.domain.menu.WipeSysMenuByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeSysMenuByIdsDomainServiceImpl implements WipeSysMenuByIdsDomainService {

    private final SysMenuRepository sysMenuRepository;

    /**
     * Physically delete the menu and all its descendants, plus role-menu associations.
     *
     * @param ids menu ids
     * @return Void
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Mono.empty();
        }
        return sysMenuRepository.deleteRoleMenuLinksByDescendantIds(ids)
                .then(sysMenuRepository.deleteAllByIdsWithDescendants(ids));
    }
}
