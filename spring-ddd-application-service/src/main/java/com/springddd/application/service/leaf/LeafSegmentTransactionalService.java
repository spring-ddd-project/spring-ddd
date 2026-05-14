package com.springddd.application.service.leaf;

import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import com.springddd.infrastructure.persistence.r2dbc.LeafAllocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class LeafSegmentTransactionalService {

    private final LeafAllocRepository leafAllocRepository;

    @Transactional
    public Mono<LeafAllocEntity> updateMaxIdAndGet(String bizTag, int step) {
        return leafAllocRepository.updateMaxIdByCustomStep(bizTag, step)
                .then(leafAllocRepository.findByBizTag(bizTag));
    }
}
