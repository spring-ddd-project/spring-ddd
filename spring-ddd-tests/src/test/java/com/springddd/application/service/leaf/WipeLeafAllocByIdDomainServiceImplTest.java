package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.LeafAllocDomain;
import com.springddd.domain.leaf.LeafAllocDomainRepository;
import com.springddd.domain.leaf.LeafAllocId;
import com.springddd.domain.leaf.exception.LeafAllocNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WipeLeafAllocByIdDomainServiceImplTest {

    @Mock
    private LeafAllocDomainRepository leafAllocDomainRepository;

    @InjectMocks
    private WipeLeafAllocByIdDomainServiceImpl service;

    @Test
    void wipe_shouldComplete_whenValidId() {
        LeafAllocDomain domain = new LeafAllocDomain();
        when(leafAllocDomainRepository.load(any(LeafAllocId.class))).thenReturn(Mono.just(domain));

        StepVerifier.create(service.wipe(new LeafAllocId(1L)))
                .verifyComplete();
    }

    @Test
    void wipe_shouldError_whenNotFound() {
        when(leafAllocDomainRepository.load(any(LeafAllocId.class))).thenReturn(Mono.empty());

        StepVerifier.create(service.wipe(new LeafAllocId(1L)))
                .expectError(LeafAllocNotFoundException.class)
                .verify();
    }
}
