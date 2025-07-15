package com.springddd.application.service.dept;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.dept.DeleteSysDeptByIdDomainService;
import com.springddd.domain.dept.DeptId;
import com.springddd.domain.dept.SysDeptDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysDeptByIdDomainServiceImpl implements DeleteSysDeptByIdDomainService {

    private final SysDeptDomainRepository sysDeptDomainRepository;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return Flux.fromIterable(ids)
                .flatMap(id -> sysDeptDomainRepository.load(new DeptId(id))
                        .flatMap(domain -> {
                            domain.delete();
                            return sysDeptDomainRepository.save(domain);
                        }), SecurityUtils.concurrency()).then();
    }
}
