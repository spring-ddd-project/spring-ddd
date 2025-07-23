package com.springddd.application.service.gen;

import com.springddd.domain.gen.WipeGenInfoByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.GenInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeGenInfoByIdsDomainServiceImpl implements WipeGenInfoByIdsDomainService {

    private final GenInfoRepository genInfoRepository;

    @Override
    public Mono<Void> wipeByIds(List<Long> ids) {
        return genInfoRepository.deleteAllById(ids);
    }
}
