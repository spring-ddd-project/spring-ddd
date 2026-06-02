package com.springddd.application.service.dict;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.dict.DeleteSysDictByIdDomainService;
import com.springddd.domain.dict.DictId;
import com.springddd.domain.dict.SysDictDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysDictByIdDomainServiceImpl implements DeleteSysDictByIdDomainService {

    private final SysDictDomainRepository sysDictDomainRepository;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return Flux.fromIterable(ids).flatMap(id -> sysDictDomainRepository.load(new DictId(id))
                .flatMap(domain -> {
                    domain.delete();
                    return sysDictDomainRepository.save(domain);
                }), SecurityUtils.concurrency()).then();
    }
}
