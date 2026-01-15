package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptView;
import com.springddd.domain.dept.WipeSysDeptByIdsDomainService;
import com.springddd.domain.util.ReactiveTreeUtils;
import com.springddd.infrastructure.persistence.r2dbc.SysDeptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeSysDeptByIdsDomainServiceImpl implements WipeSysDeptByIdsDomainService {

    private final SysDeptRepository sysDeptRepository;

    private final SysDeptQueryService sysDeptQueryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> deleteByIds(List<Long> ids) {
        return sysDeptQueryService.queryAllDept()
                .flatMapMany(ds -> Flux.fromIterable(ids)
                        .flatMap(id -> {
                            List<SysDeptView> allChildren = ReactiveTreeUtils.findAllChildrenFrom(id, ds, SysDeptView::getId, SysDeptView::getParentId);
                            return Flux.fromIterable(allChildren)
                                    .map(SysDeptView::getId);
                        }).distinct())
                .flatMap(sysDeptRepository::deleteById).then();
    }
}
