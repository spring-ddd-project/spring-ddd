package com.springddd.application.service.dept;

import com.springddd.domain.dept.DeleteSysDeptByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysDeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysDeptByIdsDomainServiceImpl implements DeleteSysDeptByIdsDomainService {

    private final SysDeptRepository sysDeptRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> deleteByIds(List<Long> ids) {
        return sysDeptRepository.deleteAllById(ids);
    }
}
