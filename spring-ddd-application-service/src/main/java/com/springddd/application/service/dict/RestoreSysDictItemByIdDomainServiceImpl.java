package com.springddd.application.service.dict;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.dict.DictItemId;
import com.springddd.domain.dict.RestoreSysDictItemByIdDomainService;
import com.springddd.domain.dict.SysDictItemDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RestoreSysDictItemByIdDomainServiceImpl implements RestoreSysDictItemByIdDomainService {

    private final SysDictItemDomainRepository sysDictItemDomainRepository;

    @Override
    public Mono<Void> restoreByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> sysDictItemDomainRepository.load(new DictItemId(id))
                        .flatMap(domain -> {
                            domain.restore();
                            return sysDictItemDomainRepository.save(domain);
                        }), SecurityUtils.concurrency()).then();
    }
}
