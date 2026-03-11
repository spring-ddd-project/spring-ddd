package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuCommand;
import com.springddd.domain.menu.*;
import com.springddd.infrastructure.persistence.factory.RepositoryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysMenuCommandService {

    private final SysMenuDomainFactory sysMenuDomainFactory;

    private final RepositoryFactory repositoryFactory;

    private final WipeSysMenuByIdsDomainService wipeSysMenuByIdsDomainService;

    private final List<SysMenuDomainStrategy> strategies;

    private final DeleteSysMenuByIdDomainService deleteSysMenuByIdDomainService;

    private final RestoreSysMenuByIdDomainService restoreSysMenuByIdDomainService;

    public Mono<Long> create(SysMenuCommand command) {
        Catalog catalog = new Catalog(command.getRedirect());
        Menu menu = new Menu(command.getPath(), command.getComponent(), command.getAffixTab(), command.getNoBasicLayout(), command.getEmbedded());
        Button button = new Button(command.getPermission(), command.getApi());
        MenuExtendInfo menuExtendInfo = new MenuExtendInfo(
                command.getOrder(),
                command.getTitle(),
                command.getIcon(),
                command.getMenuType(),
                command.getVisible(),
                command.getMenuStatus());

        SysMenuDomain sysMenuDomain = sysMenuDomainFactory.create(
                new MenuId(command.getParentId()), command.getName(), catalog, menu, button, menuExtendInfo, command.getDeptId());
        sysMenuDomain.create();

        return repositoryFactory.getSysMenuDomainRepository().save(sysMenuDomain);
    }

    public Mono<Void> update(SysMenuCommand command) {
        return repositoryFactory.getSysMenuDomainRepository().load(new MenuId(command.getId())).flatMap(domain -> {
            Catalog catalog = new Catalog(command.getRedirect());
            Menu menu = new Menu(command.getPath(), command.getComponent(), command.getAffixTab(), command.getNoBasicLayout(), command.getEmbedded());
            Button button = new Button(command.getPermission(), command.getApi());
            MenuExtendInfo menuExtendInfo = new MenuExtendInfo(
                    command.getOrder(),
                    command.getTitle(),
                    command.getIcon(),
                    command.getMenuType(),
                    command.getVisible(),
                    command.getMenuStatus());

            SysMenuDomain dummy = sysMenuDomainFactory.create(
                    new MenuId(command.getParentId()), command.getName(), catalog, menu, button, menuExtendInfo, command.getDeptId());

            domain.update(new MenuId(command.getParentId()), dummy.getName(), dummy.getCatalog(), dummy.getMenu(), dummy.getButton(), dummy.getMenuExtendInfo(), command.getDeptId());

            return repositoryFactory.getSysMenuDomainRepository().save(domain);
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





































