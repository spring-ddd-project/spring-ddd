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

    private final List<SysMenuDomainStrategy> strategies;

    public Mono<Long> create(SysMenuCommand command) {
        MenuBasicInfo menuBasicInfo = new MenuBasicInfo(new MenuName(command.getName()), new MenuPath(command.getPath()), new MenuComponent(command.getComponent()), new MenuRedirect(command.getRedirect()), new MenuPermission(command.getPermission()));

        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(
                command.getOrder(),
                command.getTitle(),
                command.getAffixTab(),
                command.getNoBasicLayout(),
                command.getIcon(),
                command.getMenuType(),
                command.getVisible(),
                command.getEmbedded(),
                command.getMenuStatus());

        SysMenuDomain sysMenuDomain = sysMenuDomainFactory.create(
                new MenuId(command.getParentId()), menuBasicInfo, menuExtendInfo, command.getDeptId());
        sysMenuDomain.create();

        return sysMenuDomainRepository.save(sysMenuDomain);
    }

    public Mono<Void> update(SysMenuCommand command) {
        return sysMenuDomainRepository.load(new MenuId(command.getId())).flatMap(domain -> {
            MenuBasicInfo menuBasicInfo = new MenuBasicInfo(new MenuName(command.getName()), new MenuPath(command.getPath()), new MenuComponent(command.getComponent()), new MenuRedirect(command.getRedirect()), new MenuPermission(command.getPermission()));

            MenuExtendInfo menuExtendInfo = new MenuExtendInfo(
                    command.getOrder(),
                    command.getTitle(),
                    command.getAffixTab(),
                    command.getNoBasicLayout(),
                    command.getIcon(),
                    command.getMenuType(),
                    command.getVisible(),
                    command.getEmbedded(),
                    command.getMenuStatus());

            for (SysMenuDomainStrategy strategy : strategies) {
                if (strategy.check(command.getMenuType())) {
                    SysMenuDomain domainNew = strategy.handle(menuBasicInfo, menuExtendInfo);
                    domain.setMenuBasicInfo(domainNew.getMenuBasicInfo());
                    domain.setMenuExtendInfo(domainNew.getMenuExtendInfo());
                }
            }

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
