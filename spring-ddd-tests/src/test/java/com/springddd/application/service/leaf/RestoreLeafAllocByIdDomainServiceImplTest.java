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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestoreLeafAllocByIdDomainServiceImplTest {

    @Mock
    private LeafAllocDomainRepository leafAllocDomainRepository;

    @InjectMocks
    private RestoreLeafAllocByIdDomainServiceImpl service;

    @Test
    void restore_shouldComplete_whenValidId() {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setDeleteStatus(true);
        when(leafAllocDomainRepository.load(any(LeafAllocId.class))).thenReturn(Mono.just(domain));
        when(leafAllocDomainRepository.save(any(LeafAllocDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.restore(new LeafAllocId(1L)))
                .verifyComplete();

        assertFalse(domain.getDeleteStatus());
    }

    @Test
    void restore_shouldError_whenNotFound() {
        when(leafAllocDomainRepository.load(any(LeafAllocId.class))).thenReturn(Mono.empty());

        StepVerifier.create(service.restore(new LeafAllocId(1L)))
                .expectError(LeafAllocNotFoundException.class)
                .verify();
    }
}
