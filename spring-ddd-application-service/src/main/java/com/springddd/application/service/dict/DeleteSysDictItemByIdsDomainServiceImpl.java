package com.springddd.application.service.dict;

import com.springddd.domain.dict.DeleteSysDictItemByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysDictItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysDictItemByIdsDomainServiceImpl implements DeleteSysDictItemByIdsDomainService {

    private final SysDictItemRepository sysDictItemRepository;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return sysDictItemRepository.deleteAllById(ids);
    }
}
