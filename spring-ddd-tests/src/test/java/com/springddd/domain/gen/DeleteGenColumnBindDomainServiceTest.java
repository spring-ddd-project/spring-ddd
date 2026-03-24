package com.springddd.domain.gen;

import com.springddd.application.service.gen.DeleteGenColumnBindDomainServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteGenColumnBindDomainServiceTest {

    @Mock
    private GenColumnBindDomainRepository domainRepository;

    @InjectMocks
    private DeleteGenColumnBindDomainServiceImpl deleteGenColumnBindDomainService;

    @Test
    void deleteByIds_shouldDeleteByIds() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.setDeleteStatus(false);

        when(domainRepository.load(any(ColumnBindId.class))).thenReturn(Mono.just(domain));
        when(domainRepository.save(any(GenColumnBindDomain.class))).thenReturn(Mono.just(1L));

        List<Long> ids = Arrays.asList(1L);

        StepVerifier.create(deleteGenColumnBindDomainService.deleteByIds(ids))
                .verifyComplete();
    }
}
