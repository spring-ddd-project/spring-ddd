package com.springddd.application.service.rowpermission;

import com.springddd.application.service.rowpermission.dto.RowPermissionView;
import com.springddd.infrastructure.persistence.entity.SysRowPermissionEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRowPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RowPermissionQueryService {

    private final SysRowPermissionRepository sysRowPermissionRepository;

    public Mono<List<RowPermissionView>> list(Long roleId, Long menuId) {
        return sysRowPermissionRepository.findByRoleIdAndMenuIdAndDeleteStatusFalse(roleId, menuId)
                .map(this::toView)
                .collectList();
    }

    private RowPermissionView toView(SysRowPermissionEntity entity) {
        RowPermissionView view = new RowPermissionView();
        view.setId(entity.getId());
        view.setRoleId(entity.getRoleId());
        view.setMenuId(entity.getMenuId());
        view.setScopeType(entity.getScopeType());
        view.setTargetId(entity.getTargetId());
        return view;
    }
}
