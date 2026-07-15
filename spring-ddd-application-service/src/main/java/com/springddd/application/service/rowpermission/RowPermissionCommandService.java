package com.springddd.application.service.rowpermission;

import com.springddd.application.service.rowpermission.dto.RowPermissionRuleCommand;
import com.springddd.application.service.rowpermission.dto.RowPermissionSaveCommand;
import com.springddd.application.service.rowpermission.dto.RowPermissionView;
import com.springddd.infrastructure.persistence.entity.SysRowPermissionEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRowPermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RowPermissionCommandService {

    private final SysRowPermissionRepository sysRowPermissionRepository;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> save(RowPermissionSaveCommand command) {
        return sysRowPermissionRepository.findByRoleIdAndMenuIdAndDeleteStatusFalse(command.getRoleId(), command.getMenuId())
                .flatMap(entity -> {
                    entity.setDeleteStatus(true);
                    return sysRowPermissionRepository.save(entity);
                })
                .thenMany(Flux.fromIterable(command.getRules()))
                .flatMap(rule -> {
                    SysRowPermissionEntity entity = new SysRowPermissionEntity();
                    entity.setRoleId(command.getRoleId());
                    entity.setMenuId(command.getMenuId());
                    entity.setScopeType(rule.getScopeType());
                    entity.setTargetType(resolveTargetType(rule.getScopeType()));
                    entity.setTargetId(rule.getTargetId());
                    entity.setDeleteStatus(false);
                    return sysRowPermissionRepository.save(entity);
                })
                .then();
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> delete(Long id) {
        return sysRowPermissionRepository.findById(id)
                .flatMap(entity -> {
                    entity.setDeleteStatus(true);
                    return sysRowPermissionRepository.save(entity);
                })
                .then();
    }

    private Integer resolveTargetType(Integer scopeType) {
        if (scopeType == null) {
            return 0;
        }
        return switch (scopeType) {
            case 1, 2 -> 1; // dept
            case 3 -> 2;    // post
            case 4 -> 3;    // user
            default -> 0;   // all
        };
    }
}
