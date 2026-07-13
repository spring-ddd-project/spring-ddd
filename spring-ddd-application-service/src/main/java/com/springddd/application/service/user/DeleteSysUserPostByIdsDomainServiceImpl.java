package com.springddd.application.service.user;

import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.domain.user.DeleteSysUserPostByIdsDomainService;
import com.springddd.domain.user.UserPostId;
import com.springddd.domain.user.SysUserPostDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysUserPostByIdsDomainServiceImpl implements DeleteSysUserPostByIdsDomainService {

    private final SysUserPostDomainRepository sysUserPostDomainRepository;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> sysUserPostDomainRepository.load(new UserPostId(id))
                        .flatMap(domain -> {
                            domain.delete();
                            return sysUserPostDomainRepository.save(domain);
                        }), ReactiveSecurityUtils.concurrency()).then();
    }
}
