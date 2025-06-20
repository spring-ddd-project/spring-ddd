package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuCommand;
import com.springddd.domain.menu.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysMenuCommandService {

    private final SysMenuDomainFactory sysMenuDomainFactory;

    private final SysMenuDomainRepository sysMenuDomainRepository;

    private final DeleteSysMenuByIdsDomainService deleteSysMenuByIdsDomainService;

    public Mono<Long> create(SysMenuCommand command) {
        MenuBasicInfo menuBasicInfo = new MenuBasicInfo();
        menuBasicInfo.setMenuName(new MenuName(command.getName()));
        menuBasicInfo.setMenuPermission(new MenuPermission(command.getPermission()));
        menuBasicInfo.setMenuIcon(new MenuIcon(command.getIcon()));
        menuBasicInfo.setMenuPath(new MenuPath(command.getPath()));
        menuBasicInfo.setMenuType(new MenuType(command.getMenuType()));

        MenuExtendInfo menuExtendInfo = new MenuExtendInfo();
        menuExtendInfo.setSortOrder(command.getSortOrder());
        menuExtendInfo.setVisible(command.getVisible());
        menuExtendInfo.setEmbedded(command.getEmbedded());
        menuExtendInfo.setMenuStatus(command.getMenuStatus());

        SysMenuDomain sysMenuDomain = sysMenuDomainFactory.create(
                new MenuId(command.getParentId()), menuBasicInfo, menuExtendInfo, command.getDeptId());
        sysMenuDomain.create();

        return sysMenuDomainRepository.save(sysMenuDomain);
    }

    public Mono<Void> update(SysMenuCommand command) {
        return sysMenuDomainRepository.load(new MenuId(command.getId())).flatMap(domain -> {

            MenuBasicInfo menuBasicInfo = new MenuBasicInfo();
            menuBasicInfo.setMenuName(new MenuName(command.getName()));
            menuBasicInfo.setMenuPermission(new MenuPermission(command.getPermission()));
            menuBasicInfo.setMenuIcon(new MenuIcon(command.getIcon()));
            menuBasicInfo.setMenuPath(new MenuPath(command.getPath()));
            menuBasicInfo.setMenuType(new MenuType(command.getMenuType()));

            MenuExtendInfo menuExtendInfo = new MenuExtendInfo();
            menuExtendInfo.setSortOrder(command.getSortOrder());
            menuExtendInfo.setVisible(command.getVisible());
            menuExtendInfo.setEmbedded(command.getEmbedded());
            menuExtendInfo.setMenuStatus(command.getMenuStatus());

            domain.update(new MenuId(command.getParentId()), menuBasicInfo, menuExtendInfo, command.getDeptId());

            return sysMenuDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> delete(SysMenuCommand command) {
        return sysMenuDomainRepository.load(new MenuId(command.getId())).flatMap(domain -> {
            domain.delete();
            return sysMenuDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> wipe(List<Long> ids) {
        return deleteSysMenuByIdsDomainService.deleteByIds(ids);
    }
}
