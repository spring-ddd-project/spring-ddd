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
class DeleteLeafAllocByIdDomainServiceImplTest {

    @Mock
    private LeafAllocDomainRepository leafAllocDomainRepository;

    @InjectMocks
    private DeleteLeafAllocByIdDomainServiceImpl service;

    @Test
    void delete_shouldComplete_whenValidId() {
        LeafAllocDomain domain = new LeafAllocDomain();
        when(leafAllocDomainRepository.load(any(LeafAllocId.class))).thenReturn(Mono.just(domain));
        when(leafAllocDomainRepository.save(any(LeafAllocDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.delete(new LeafAllocId(1L)))
                .verifyComplete();

        assertTrue(domain.getDeleteStatus());
    }

    @Test
    void delete_shouldError_whenNotFound() {
        when(leafAllocDomainRepository.load(any(LeafAllocId.class))).thenReturn(Mono.empty());

        StepVerifier.create(service.delete(new LeafAllocId(1L)))
                .expectError(LeafAllocNotFoundException.class)
                .verify();
    }
}
