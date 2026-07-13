package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuDataScopeQuery;
import com.springddd.application.service.role.dto.SysRoleMenuDataScopeView;
import com.springddd.application.service.role.dto.SysRoleMenuDataScopeViewMapStruct;
import com.springddd.infrastructure.persistence.entity.SysRoleMenuDataScopeEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleMenuDataScopeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysRoleMenuDataScopeQueryService {

    private final SysRoleMenuDataScopeRepository sysRoleMenuDataScopeRepository;

    private final SysRoleMenuDataScopeViewMapStruct sysRoleMenuDataScopeViewMapStruct;

    public Mono<List<SysRoleMenuDataScopeView>> listByRoleId(Long roleId) {
        Flux<SysRoleMenuDataScopeEntity> entities = sysRoleMenuDataScopeRepository.findByRoleIdAndDeleteStatusFalse(roleId);
        return entities.collectList().map(sysRoleMenuDataScopeViewMapStruct::toViews);
    }

    public Mono<SysRoleMenuDataScopeView> findByRoleIdAndMenuId(Long roleId, Long menuId) {
        return sysRoleMenuDataScopeRepository.findByRoleIdAndMenuIdAndDeleteStatusFalse(roleId, menuId)
                .single()
                .map(sysRoleMenuDataScopeViewMapStruct::toView);
    }
}
