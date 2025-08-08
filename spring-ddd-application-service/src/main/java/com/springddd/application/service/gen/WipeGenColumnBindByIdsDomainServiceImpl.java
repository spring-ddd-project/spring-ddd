package com.springddd.application.service.gen;

import com.springddd.domain.gen.WipeGenColumnBindByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnBindRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeGenColumnBindByIdsDomainServiceImpl implements WipeGenColumnBindByIdsDomainService {

    private final GenColumnBindRepository genColumnBindRepository;

    @Override
    public Mono<Void> wipeByIds(List<Long> ids) {
        return genColumnBindRepository.deleteAllById(ids);
    }
}
