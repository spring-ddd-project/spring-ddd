package com.springddd.application.service.user;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.user.RestoreSysUserByIdDomainService;
import com.springddd.domain.user.SysUserDomainRepository;
import com.springddd.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestoreSysUserByIdDomainServiceImpl implements RestoreSysUserByIdDomainService {

    private final SysUserDomainRepository sysUserDomainRepository;

    @Override
    public Mono<Void> restoreByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> sysUserDomainRepository.load(new UserId(id))
                        .flatMap(domain -> {
                            domain.restore();
                            return sysUserDomainRepository.save(domain);
                        }), SecurityUtils.concurrency())
                .then();
    }
}
