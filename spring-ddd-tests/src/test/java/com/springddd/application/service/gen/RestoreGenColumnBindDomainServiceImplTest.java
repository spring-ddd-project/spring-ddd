package com.springddd.application.service.gen;

import com.springddd.domain.gen.ColumnBindId;
import com.springddd.domain.gen.GenColumnBindDomain;
import com.springddd.domain.gen.GenColumnBindDomainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestoreGenColumnBindDomainServiceImplTest {

    @Mock
    private GenColumnBindDomainRepository domainRepository;

    private RestoreGenColumnBindDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new RestoreGenColumnBindDomainServiceImpl(domainRepository);
    }

    @Test
    void restoreByIds_shouldComplete_whenValidIds() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.setDeleteStatus(true);
        when(domainRepository.load(any(ColumnBindId.class))).thenReturn(Mono.just(domain));
        when(domainRepository.save(any(GenColumnBindDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.restoreByIds(List.of(1L)))
                .verifyComplete();
    }
}
