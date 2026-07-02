package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.domain.menu.MenuId;
import com.springddd.domain.menu.RestoreSysMenuByIdDomainService;
import com.springddd.domain.menu.SysMenuDomainRepository;
import com.springddd.domain.util.ReactiveTreeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RestoreSysMenuByIdDomainServiceImpl implements RestoreSysMenuByIdDomainService {

    private final SysMenuDomainRepository sysMenuDomainRepository;

    private final SysMenuQueryService sysMenuQueryService;

    @Override
    public Mono<Void> restoreByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Mono.empty();
        }
        return sysMenuQueryService.queryAllMenu()
                .flatMapMany(menus ->
                        Flux.fromIterable(ids)
                                .flatMap(id -> {
                                    List<SysMenuView> allChildren = ReactiveTreeUtils.findAllChildrenFrom(
                                            id, menus, SysMenuView::getId, SysMenuView::getParentId);
                                    return Flux.fromIterable(allChildren)
                                            .filter(SysMenuView::getDeleteStatus)
                                            .map(SysMenuView::getId)
                                            .filter(Objects::nonNull);
                                }).distinct())
                .flatMap(menuId -> sysMenuDomainRepository.load(new MenuId(menuId))
                                .flatMap(domain -> {
                                    domain.restore();
                                    return sysMenuDomainRepository.save(domain);
                                }),
                        ReactiveSecurityUtils.concurrency())
                .then();
    }
}
