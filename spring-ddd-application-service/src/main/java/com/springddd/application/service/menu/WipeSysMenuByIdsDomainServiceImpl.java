package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.domain.menu.WipeSysMenuByIdsDomainService;
import com.springddd.domain.util.ReactiveTreeUtils;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeSysMenuByIdsDomainServiceImpl implements WipeSysMenuByIdsDomainService {

    private final SysMenuRepository sysMenuRepository;

    private final SysMenuQueryService sysMenuQueryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> deleteByIds(List<Long> ids) {
        return sysMenuQueryService.queryAllMenu()
                .flatMapMany(menus ->
                        Flux.fromIterable(ids)
                                .flatMap(id -> {
                                    List<SysMenuView> allChildren = ReactiveTreeUtils.findAllChildrenFrom(
                                            id, menus, SysMenuView::getId, SysMenuView::getParentId);
                                    return Flux.fromIterable(allChildren)
                                            .map(SysMenuView::getId);
                                }).distinct())
                .flatMap(sysMenuRepository::deleteById).then();
    }
}
