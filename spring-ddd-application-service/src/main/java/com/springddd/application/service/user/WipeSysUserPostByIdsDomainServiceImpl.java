package com.springddd.application.service.user;

import com.springddd.domain.user.WipeSysUserPostByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysUserPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeSysUserPostByIdsDomainServiceImpl implements WipeSysUserPostByIdsDomainService {

    private final SysUserPostRepository sysUserPostRepository;

    @Override
    public Mono<Void> wipeByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(sysUserPostRepository::deleteById).then();
    }
}
