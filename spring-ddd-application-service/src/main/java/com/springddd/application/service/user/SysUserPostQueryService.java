package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserPostView;
import com.springddd.application.service.user.dto.SysUserPostViewMapStruct;
import com.springddd.infrastructure.persistence.r2dbc.SysUserPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SysUserPostQueryService {

    private final SysUserPostRepository sysUserPostRepository;

    private final SysUserPostViewMapStruct sysUserPostViewMapStruct;

    public Mono<List<SysUserPostView>> listByUserId(Long userId) {
        return sysUserPostRepository.findByUserIdAndDeleteStatusFalse(userId)
                .collectList()
                .map(sysUserPostViewMapStruct::toViews);
    }
}
