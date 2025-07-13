package com.springddd.application.service.dict;

import com.springddd.domain.dict.WipeSysDictByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysDictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeSysDictByIdsDomainServiceImpl implements WipeSysDictByIdsDomainService {

    private final SysDictRepository sysDictRepository;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return sysDictRepository.deleteAllById(ids);
    }
}
