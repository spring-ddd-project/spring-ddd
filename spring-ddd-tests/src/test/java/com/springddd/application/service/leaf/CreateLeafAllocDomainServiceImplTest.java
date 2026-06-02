package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.LeafAllocDomain;
import com.springddd.domain.leaf.LeafAllocDomainRepository;
import com.springddd.domain.leaf.LeafAllocId;
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
class CreateLeafAllocDomainServiceImplTest {

    @Mock
    private LeafAllocDomainRepository leafAllocDomainRepository;

    @InjectMocks
    private CreateLeafAllocDomainServiceImpl service;

    @Test
    void create_shouldReturnDomainWithId_whenValidDomain() {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setBizTag(new com.springddd.domain.leaf.BizTag("test"));
        domain.setMaxId(new com.springddd.domain.leaf.MaxId(1000L));
        domain.setStep(new com.springddd.domain.leaf.Step(100));

        when(leafAllocDomainRepository.save(any(LeafAllocDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.create(domain))
                .assertNext(result -> {
                    assertNotNull(result.getLeafAllocId());
                    assertEquals(1L, result.getLeafAllocId().value());
                })
                .verifyComplete();
    }
}
