package com.springddd.application.service.post;

import com.springddd.application.service.post.dto.SysPostView;
import com.springddd.domain.post.WipeSysPostByIdsDomainService;
import com.springddd.domain.util.ReactiveTreeUtils;
import com.springddd.infrastructure.persistence.r2dbc.SysPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WipeSysPostByIdsDomainServiceImpl implements WipeSysPostByIdsDomainService {

    private final SysPostRepository sysPostRepository;

    private final SysPostQueryService sysPostQueryService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Mono<Void> wipeByIds(List<Long> ids) {
        return sysPostQueryService.queryAllPost()
                .flatMapMany(ds -> Flux.fromIterable(ids)
                        .flatMap(id -> {
                            List<SysPostView> allChildren = ReactiveTreeUtils.findAllChildrenFrom(id, ds, SysPostView::getId, SysPostView::getParentId);
                            return Flux.fromIterable(allChildren)
                                    .map(SysPostView::getId);
                        }).distinct())
                .flatMap(sysPostRepository::deleteById).then();
    }
}
