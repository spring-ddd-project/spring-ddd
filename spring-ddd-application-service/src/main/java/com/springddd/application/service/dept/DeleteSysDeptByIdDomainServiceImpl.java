package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.dept.DeleteSysDeptByIdDomainService;
import com.springddd.domain.dept.DeptId;
import com.springddd.domain.dept.SysDeptDomainRepository;
import com.springddd.domain.util.ReactiveTreeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysDeptByIdDomainServiceImpl implements DeleteSysDeptByIdDomainService {

    private final SysDeptDomainRepository sysDeptDomainRepository;

    private final SysDeptQueryService sysDeptQueryService;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return sysDeptQueryService.queryAllDept()
                .flatMapMany(ds -> Flux.fromIterable(ids)
                        .flatMap(id -> {
                            List<SysDeptView> allChildren = ReactiveTreeUtils.findAllChildrenFrom(id, ds, SysDeptView::getId, SysDeptView::getParentId);
                            return Flux.fromIterable(allChildren).map(SysDeptView::getId);
                        }).distinct()).flatMap(id -> sysDeptDomainRepository.load(new DeptId(id)).flatMap(domain -> {
                    domain.delete();
                    return sysDeptDomainRepository.save(domain);
                }), SecurityUtils.concurrency()).then();
    }
}
