package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LinkRoleAndMenusDomainServiceImpl implements LinkRoleAndMenusDomainService {

    private final SysRoleMenuQueryService sysRoleMenuQueryService;

    private final DeleteSysRoleMenuByIdsDomainService deleteSysRoleMenuByIdsDomainService;

    private final SysRoleMenuDomainRepository sysRoleMenuDomainRepository;

    private final SysRoleMenuDomainFactory sysRoleMenuDomainFactory;

    @Override
    public Mono<Void> link(Long roleId, List<Long> menuIds) {
        return sysRoleMenuQueryService.queryLinkRoleAndMenus(roleId).flatMap(sysRoleMenuViews -> {
            List<Long> ids = sysRoleMenuViews.stream().map(SysRoleMenuView::getId).toList();
            Mono<Void> deleted = deleteSysRoleMenuByIdsDomainService.deleteByIds(ids);

            List<Mono<Long>> saved = menuIds.stream().map(menuId -> {
                SysRoleMenuDomain domain = sysRoleMenuDomainFactory.newInstance(new RoleId(roleId), new MenuId(menuId), null, "TODO");
                domain.create();
                return sysRoleMenuDomainRepository.save(domain);
            }).toList();

            return deleted.thenMany(Mono.when(saved)).then();
        });
    }
}
