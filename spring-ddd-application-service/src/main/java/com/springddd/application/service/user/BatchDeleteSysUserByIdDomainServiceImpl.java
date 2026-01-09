package com.springddd.application.service.user;

import com.springddd.domain.user.BatchDeleteSysUserByIdDomainService;
import com.springddd.domain.user.SysUserDomainRepository;
import com.springddd.domain.user.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BatchDeleteSysUserByIdDomainServiceImpl implements BatchDeleteSysUserByIdDomainService {

    private final SysUserDomainRepository sysUserDomainRepository;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> sysUserDomainRepository.load(new UserId(id))
                        .flatMap(domain -> {
                            domain.delete();
                            return sysUserDomainRepository.save(domain);
                        }), Runtime.getRuntime().availableProcessors() * 2)
                .then();
    }
}
