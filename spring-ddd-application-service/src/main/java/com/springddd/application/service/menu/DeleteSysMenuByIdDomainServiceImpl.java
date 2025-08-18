package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuQuery;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.menu.DeleteSysMenuByIdDomainService;
import com.springddd.domain.menu.MenuId;
import com.springddd.domain.menu.SysMenuDomainRepository;
import com.springddd.domain.util.ReactiveTreeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysMenuByIdDomainServiceImpl implements DeleteSysMenuByIdDomainService {

    private final SysMenuDomainRepository sysMenuDomainRepository;

    private final SysMenuQueryService sysMenuQueryService;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Mono.empty();
        }

        // Delete this node and all its child nodes.
        return sysMenuQueryService.allMenu()
                .flatMapMany(flatList ->
                        Flux.fromIterable(ids)
                                .flatMap(id -> {
                                    List<SysMenuView> children = ReactiveTreeUtils.findAllChildrenFrom(
                                            id, flatList, SysMenuView::getId, SysMenuView::getParentId);
                                    return Flux.fromIterable(children)
                                            .map(SysMenuView::getId);
                                })
                                .distinct()
                )
                .flatMap(id -> sysMenuDomainRepository.load(new MenuId(id))
                                .flatMap(domain -> {
                                    domain.delete();
                                    return sysMenuDomainRepository.save(domain);
                                }),
                        SecurityUtils.concurrency()
                )
                .then();
    }

}
