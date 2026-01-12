package com.springddd.application.service.dept;

import com.springddd.domain.dept.WipeSysDeptByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysDeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeSysDeptByIdsDomainServiceImpl implements WipeSysDeptByIdsDomainService {

    private final SysDeptRepository sysDeptRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> deleteByIds(List<Long> ids) {
        return sysDeptRepository.deleteAllById(ids);
    }
}
