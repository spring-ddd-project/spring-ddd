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
class DeleteGenColumnBindDomainServiceImplTest {

    @Mock
    private GenColumnBindDomainRepository domainRepository;

    private DeleteGenColumnBindDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new DeleteGenColumnBindDomainServiceImpl(domainRepository);
    }

    @Test
    void deleteByIds_shouldComplete_whenValidIds() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        when(domainRepository.load(any(ColumnBindId.class))).thenReturn(Mono.just(domain));
        when(domainRepository.save(any(GenColumnBindDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();
    }
}
