package com.springddd.application.service.menu;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.menu.DeleteSysMenuByIdDomainService;
import com.springddd.domain.menu.MenuId;
import com.springddd.domain.menu.SysMenuDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysMenuByIdDomainServiceImpl implements DeleteSysMenuByIdDomainService {

    private final SysMenuDomainRepository sysMenuDomainRepository;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> sysMenuDomainRepository.load(new MenuId(id))
                        .flatMap(domain -> {
                            domain.delete();
                            return sysMenuDomainRepository.save(domain);
                        }), SecurityUtils.concurrency()).then();
    }
}
