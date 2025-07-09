package com.springddd.application.service.gen;

import com.springddd.domain.gen.WipeGenProjectInfoByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.GenProjectInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeGenProjectInfoByIdsDomainServiceImpl implements WipeGenProjectInfoByIdsDomainService {

    private final GenProjectInfoRepository genProjectInfoRepository;

    @Override
    public Mono<Void> wipeByIds(List<Long> ids) {
        return genProjectInfoRepository.deleteAllById(ids);
    }
}
