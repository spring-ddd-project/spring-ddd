package com.springddd.application.service.dict;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.dict.DeleteSysDictItemByIdDomainService;
import com.springddd.domain.dict.DictItemId;
import com.springddd.domain.dict.SysDictItemDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysDictItemByIdDomainServiceImpl implements DeleteSysDictItemByIdDomainService {

    private final SysDictItemDomainRepository sysDictItemDomainRepository;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> sysDictItemDomainRepository.load(new DictItemId(id))
                        .flatMap(domain -> {
                            domain.delete();
                            return sysDictItemDomainRepository.save(domain);
                        }), SecurityUtils.concurrency()).then();
    }
}
