package com.springddd.application.service.post;

import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.domain.post.PostId;
import com.springddd.domain.post.RestoreSysPostByIdsDomainService;
import com.springddd.domain.post.SysPostDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestoreSysPostByIdsDomainServiceImpl implements RestoreSysPostByIdsDomainService {

    private final SysPostDomainRepository sysPostDomainRepository;

    @Override
    public Mono<Void> restoreByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> sysPostDomainRepository.load(new PostId(id))
                        .flatMap(domain -> {
                            domain.restore();
                            return sysPostDomainRepository.save(domain);
                        }), ReactiveSecurityUtils.concurrency()).then();
    }
}
