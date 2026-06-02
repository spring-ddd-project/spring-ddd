package com.springddd.application.service.leaf;

import com.springddd.domain.leaf.*;
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
class UpdateLeafAllocDomainServiceImplTest {

    @Mock
    private LeafAllocDomainRepository leafAllocDomainRepository;

    @InjectMocks
    private UpdateLeafAllocDomainServiceImpl service;

    @Test
    void update_shouldReturnUpdatedDomain_whenValidParams() {
        LeafAllocDomain domain = new LeafAllocDomain();
        domain.setLeafAllocId(new LeafAllocId(1L));
        domain.setBizTag(new BizTag("old"));
        domain.setMaxId(new MaxId(100L));
        domain.setStep(new Step(10));

        when(leafAllocDomainRepository.save(any(LeafAllocDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.update(
                domain,
                new BizTag("new"),
                new MaxId(200L),
                new Step(20),
                new Description("new desc"),
                2L
        ))
        .assertNext(result -> {
            assertEquals("new", result.getBizTag().value());
            assertEquals(200L, result.getMaxId().value());
            assertEquals(20, result.getStep().value());
            assertEquals("new desc", result.getDescription().value());
            assertEquals(2L, result.getDeptId());
        })
        .verifyComplete();
    }
}
