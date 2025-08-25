package com.springddd.application.service.gen;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.gen.ColumnBindId;
import com.springddd.domain.gen.DeleteGenColumnBindDomainService;
import com.springddd.domain.gen.GenColumnBindDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteGenColumnBindDomainServiceImpl implements DeleteGenColumnBindDomainService {

    private final GenColumnBindDomainRepository domainRepository;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> domainRepository.load(new ColumnBindId(id))
                        .flatMap(domain -> {
                            domain.delete();
                            return domainRepository.save(domain);
                        }), SecurityUtils.concurrency()).then();
    }
}
