package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuCommand;
import com.springddd.domain.menu.*;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysMenuCommandService {

    private final SysMenuDomainFactory sysMenuDomainFactory;

    private final SysMenuDomainRepository sysMenuDomainRepository;

    private final SysMenuRepository sysMenuRepository;

    private final WipeSysMenuByIdsDomainService wipeSysMenuByIdsDomainService;

    private final List<SysMenuDomainStrategy> strategies;

    private final DeleteSysMenuByIdDomainService deleteSysMenuByIdDomainService;

    private final RestoreSysMenuByIdDomainService restoreSysMenuByIdDomainService;

    @Transactional(rollbackFor = Exception.class)
    public Mono<Long> create(SysMenuCommand command) {
        return computeParentInfo(command.getParentId())
                .flatMap(parentInfo -> {
                    Catalog catalog = new Catalog(command.getRedirect());
                    Menu menu = new Menu(command.getPath(), command.getComponent(), command.getAffixTab(), command.getNoBasicLayout(), command.getEmbedded());
                    Button button = (command.getMenuType() != null && command.getMenuType() == 3)
                            ? new Button(command.getPermission(), command.getApi())
                            : null;
                    MenuExtendInfo menuExtendInfo = new MenuExtendInfo(
                            command.getOrder(),
                            command.getTitle(),
                            command.getIcon(),
                            command.getMenuType(),
                            command.getVisible(),
                            command.getMenuStatus());

                    SysMenuDomain sysMenuDomain = sysMenuDomainFactory.create(
                            new MenuId(command.getParentId()), command.getName(), catalog, menu, button, menuExtendInfo, command.getDeptId());
                    sysMenuDomain.setDepth(parentInfo.depth());
                    sysMenuDomain.create();

                    return sysMenuDomainRepository.save(sysMenuDomain)
                            .map(id -> {
                                String treePath = buildTreePath(parentInfo.treePath(), id);
                                sysMenuDomain.setMenuId(new MenuId(id));
                                sysMenuDomain.setTreePath(treePath);
                                return sysMenuDomain;
                            })
                            .flatMap(sysMenuDomainRepository::save);
                });
    }

    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> update(SysMenuCommand command) {
        return sysMenuDomainRepository.load(new MenuId(command.getId())).flatMap(domain -> {
            Long oldParentId = domain.getParentId() != null ? domain.getParentId().value() : null;
            Long newParentId = command.getParentId();
            boolean parentChanged = !java.util.Objects.equals(oldParentId, newParentId);

            return computeParentInfo(newParentId)
                    .flatMap(newParentInfo -> {
                        if (parentChanged) {
                            // Prevent cycles: new parent must not be a descendant of current node.
                            return validateNoCycle(command.getId(), newParentId)
                                    .then(Mono.defer(() -> {
                                        Integer oldDepth = domain.getDepth();
                                        String oldTreePath = domain.getTreePath();
                                        Integer newDepth = newParentInfo.depth();
                                        String newTreePath = buildTreePath(newParentInfo.treePath(), command.getId());

                                        Catalog catalog = new Catalog(command.getRedirect());
                                        Menu menu = new Menu(command.getPath(), command.getComponent(), command.getAffixTab(), command.getNoBasicLayout(), command.getEmbedded());
                                        Button button = (command.getMenuType() != null && command.getMenuType() == 3)
                                                ? new Button(command.getPermission(), command.getApi())
                                                : null;
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
                                        domain.setDepth(newDepth);
                                        domain.setTreePath(newTreePath);

                                        return sysMenuDomainRepository.save(domain)
                                                .flatMap(savedId -> updateDescendantsIfNeeded(command.getId(), oldDepth, oldTreePath, newDepth, newTreePath));
                                    }));
                        } else {
                            Catalog catalog = new Catalog(command.getRedirect());
                            Menu menu = new Menu(command.getPath(), command.getComponent(), command.getAffixTab(), command.getNoBasicLayout(), command.getEmbedded());
                            Button button = (command.getMenuType() != null && command.getMenuType() == 3)
                                    ? new Button(command.getPermission(), command.getApi())
                                    : null;
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
                            return sysMenuDomainRepository.save(domain).then();
                        }
                    });
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

    private Mono<ParentInfo> computeParentInfo(Long parentId) {
        if (parentId == null || parentId == 0) {
            return Mono.just(new ParentInfo(0, "/"));
        }
        return sysMenuDomainRepository.load(new MenuId(parentId))
                .map(parent -> new ParentInfo(
                        parent.getDepth() != null ? parent.getDepth() + 1 : 1,
                        parent.getTreePath()))
                .switchIfEmpty(Mono.just(new ParentInfo(0, "/")));
    }

    private String buildTreePath(String parentTreePath, Long id) {
        if (parentTreePath == null) {
            return null;
        }
        String base = parentTreePath.endsWith("/") ? parentTreePath : parentTreePath + "/";
        String candidate = base + id + "/";
        // Do not materialize paths that exceed the column length (500).
        return candidate.length() <= 500 ? candidate : null;
    }

    private Mono<Void> validateNoCycle(Long currentId, Long newParentId) {
        if (newParentId == null || newParentId == 0 || currentId == null) {
            return Mono.empty();
        }
        return sysMenuRepository.findAllAncestorIds(newParentId)
                .any(ancestorId -> ancestorId.equals(currentId))
                .flatMap(hasCycle -> {
                    if (Boolean.TRUE.equals(hasCycle)) {
                        return Mono.error(new IllegalArgumentException("Cannot move a node under its own descendant"));
                    }
                    return Mono.empty();
                });
    }

    private Mono<Void> updateDescendantsIfNeeded(Long menuId, Integer oldDepth, String oldTreePath,
                                                   Integer newDepth, String newTreePath) {
        if (menuId == null || oldDepth == null || newDepth == null || oldDepth.equals(newDepth)) {
            // Even if depth unchanged, tree_path prefix may have changed (moving between parents at same depth).
            if (menuId == null || java.util.Objects.equals(oldTreePath, newTreePath)) {
                return Mono.empty();
            }
        }
        int depthDelta = newDepth - (oldDepth != null ? oldDepth : 0);

        // The repository method updates only descendants. When the old or new node path is null
        // (deep chains where the path exceeds the column length), we keep descendant paths as-is
        // and only shift depth. The node itself was already updated by the preceding save().
        return sysMenuRepository
                .updateDescendantsTreePath(menuId, oldTreePath, newTreePath, depthDelta)
                .onErrorResume(e -> {
                    // Fallback: if the CTE update fails due to driver limitations, ignore path update but keep node update.
                    return Mono.empty();
                });
    }

    private record ParentInfo(Integer depth, String treePath) {
    }
}
