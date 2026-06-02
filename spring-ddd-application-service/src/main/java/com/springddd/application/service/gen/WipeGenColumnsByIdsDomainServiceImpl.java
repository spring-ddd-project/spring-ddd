package com.springddd.application.service.gen;

import com.springddd.domain.gen.WipeGenColumnsByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeGenColumnsByIdsDomainServiceImpl implements WipeGenColumnsByIdsDomainService {

    private final GenColumnsRepository genColumnsRepository;

    @Override
    public Mono<Void> wipeByIds(List<Long> ids) {
        return genColumnsRepository.deleteAllById(ids);
    }
}
