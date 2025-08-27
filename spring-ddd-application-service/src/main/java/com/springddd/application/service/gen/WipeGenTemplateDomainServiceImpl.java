package com.springddd.application.service.gen;

import com.springddd.domain.gen.WipeGenTemplateDomainService;
import com.springddd.infrastructure.persistence.r2dbc.GenTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeGenTemplateDomainServiceImpl implements WipeGenTemplateDomainService {

    private final GenTemplateRepository genTemplateRepository;

    @Override
    public Mono<Void> wipeByIds(List<Long> ids) {
        return genTemplateRepository.deleteAllById(ids);
    }
}
