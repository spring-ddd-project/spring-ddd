package com.springddd.application.service.menu;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.menu.MenuId;
import com.springddd.domain.menu.RestoreSysMenuByIdDomainService;
import com.springddd.domain.menu.SysMenuDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestoreSysMenuByIdDomainServiceImpl implements RestoreSysMenuByIdDomainService {

    private final SysMenuDomainRepository sysMenuDomainRepository;

    @Override
    public Mono<Void> restoreByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> sysMenuDomainRepository.load(new MenuId(id))
                        .flatMap(domain -> {
                            domain.restore();
                            return sysMenuDomainRepository.save(domain);
                        }), SecurityUtils.concurrency()).then();
    }
}
