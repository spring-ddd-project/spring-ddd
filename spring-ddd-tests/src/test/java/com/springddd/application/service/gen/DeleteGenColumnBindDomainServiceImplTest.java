package com.springddd.application.service.gen;

import com.springddd.domain.gen.ColumnBindId;
import com.springddd.domain.gen.GenColumnBindDomain;
import com.springddd.domain.gen.GenColumnBindDomainRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteGenColumnBindDomainServiceImplTest {

    @Mock
    private GenColumnBindDomainRepository domainRepository;

    @InjectMocks
    private DeleteGenColumnBindDomainServiceImpl service;

    @Test
    @DisplayName("deleteByIds 应删除指定列绑定")
    void deleteByIds_shouldDelete() {
        GenColumnBindDomain domain = mock(GenColumnBindDomain.class);
        when(domainRepository.load(new ColumnBindId(1L))).thenReturn(Mono.just(domain));
        when(domainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();

        verify(domain).delete();
        verify(domainRepository).save(domain);
    }
}
