package com.springddd.application.service.gen;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.gen.DeleteGenTemplateDomainService;
import com.springddd.domain.gen.GenTemplateDomainRepository;
import com.springddd.domain.gen.TemplateId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteGenTemplateDomainServiceImpl implements DeleteGenTemplateDomainService {

    private final GenTemplateDomainRepository genTemplateDomainRepository;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> genTemplateDomainRepository.load(new TemplateId(id))
                        .flatMap(domain -> {
                            domain.delete();
                            return genTemplateDomainRepository.save(domain);
                        }), SecurityUtils.concurrency())
                .then();
    }
}
