package com.springddd.domain.dict;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestoreSysDictByIdDomainServiceTest {

    @Mock
    private SysDictDomainRepository repository;

    @InjectMocks
    private RestoreSysDictByIdDomainService domainService;

    @Test
    void restoreByIds_shouldRestoreEntities() {
        SysDictDomain domain = new SysDictDomain();
        domain.setId(new DictId(1L));
        domain.delete();

        when(repository.load(any(DictId.class))).thenReturn(Mono.just(domain));
        when(repository.save(any(SysDictDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(domainService.restoreByIds(Arrays.asList(1L, 2L)))
                .verifyComplete();

        verify(repository, times(2)).load(any(DictId.class));
        verify(repository, times(2)).save(any(SysDictDomain.class));
    }

    @Test
    void restoreByIds_shouldHandleEmptyList() {
        StepVerifier.create(domainService.restoreByIds(Arrays.asList()))
                .verifyComplete();

        verify(repository, never()).load(any(DictId.class));
        verify(repository, never()).save(any(SysDictDomain.class));
    }

    @Test
    void restoreByIds_shouldHandleNonExistingId() {
        when(repository.load(any(DictId.class))).thenReturn(Mono.empty());

        StepVerifier.create(domainService.restoreByIds(Arrays.asList(999L)))
                .verifyComplete();

        verify(repository, times(1)).load(any(DictId.class));
        verify(repository, never()).save(any(SysDictDomain.class));
    }
}
