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

    private final WipeSysMenuByIdsDomainService wipeSysMenuByIdsDomainService;

    private final List<SysMenuDomainStrategy> strategies;

    private final DeleteSysMenuByIdDomainService deleteSysMenuByIdDomainService;

    private final RestoreSysMenuByIdDomainService restoreSysMenuByIdDomainService;

    public Mono<Long> create(SysMenuCommand command) {
        Catalog catalog = new Catalog(command.getRedirect());
        Menu menu = new Menu(command.getName(), command.getPath(), command.getComponent(), command.getAffixTab(), command.getNoBasicLayout(), command.getEmbedded());
        Button button = new Button(command.getApi(), command.getPermission());
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
                new MenuId(command.getParentId()), catalog, menu, button, menuExtendInfo, command.getDeptId());
        sysMenuDomain.create();

        return sysMenuDomainRepository.save(sysMenuDomain);
    }

    public Mono<Void> update(SysMenuCommand command) {
        return sysMenuDomainRepository.load(new MenuId(command.getId())).flatMap(domain -> {
            Catalog catalog = new Catalog(command.getRedirect());
            Menu menu = new Menu(command.getName(), command.getPath(), command.getComponent(), command.getAffixTab(), command.getNoBasicLayout(), command.getEmbedded());
            Button button = new Button(command.getApi(), command.getPermission());
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

            domain.update(new MenuId(command.getParentId()), catalog, menu, button, menuExtendInfo, command.getDeptId());

            return sysMenuDomainRepository.save(domain);
        }).then();
    }

    public Mono<Void> delete(List<Long> ids) {
        return deleteSysMenuByIdDomainService.deleteByIds(ids);
    }

    public Mono<Void> wipe(List<Long> ids) {
        return wipeSysMenuByIdsDomainService.deleteByIds(ids);
    }

    public Mono<Void> restore(List<Long> ids) {
        return restoreSysMenuByIdDomainService.restoreByIds(ids);
    }
}
