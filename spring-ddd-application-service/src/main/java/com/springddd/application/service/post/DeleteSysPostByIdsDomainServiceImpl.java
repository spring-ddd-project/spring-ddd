package com.springddd.application.service.post;

import com.springddd.application.service.post.dto.SysPostView;
import com.springddd.domain.auth.ReactiveSecurityUtils;
import com.springddd.domain.post.DeleteSysPostByIdsDomainService;
import com.springddd.domain.post.PostId;
import com.springddd.domain.post.SysPostDomainRepository;
import com.springddd.domain.util.ReactiveTreeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DeleteSysPostByIdsDomainServiceImpl implements DeleteSysPostByIdsDomainService {

    private final SysPostDomainRepository sysPostDomainRepository;

    private final SysPostQueryService sysPostQueryService;

    @Override
    public Mono<Void> deleteByIds(List<Long> ids) {
        return sysPostQueryService.queryAllPost()
                .flatMapMany(ds -> Flux.fromIterable(ids)
                        .flatMap(id -> {
                            List<SysPostView> allChildren = ReactiveTreeUtils.findAllChildrenFrom(id, ds, SysPostView::getId, SysPostView::getParentId);
                            return Flux.fromIterable(allChildren).map(SysPostView::getId);
                        }).distinct()).flatMap(id -> sysPostDomainRepository.load(new PostId(id)).flatMap(domain -> {
                    domain.delete();
                    return sysPostDomainRepository.save(domain);
                }), ReactiveSecurityUtils.concurrency()).then();
    }
}
