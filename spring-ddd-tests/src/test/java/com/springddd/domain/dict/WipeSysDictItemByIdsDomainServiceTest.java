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
class WipeSysDictItemByIdsDomainServiceTest {

    @Mock
    private SysDictItemDomainRepository repository;

    @InjectMocks
    private WipeSysDictItemByIdsDomainService domainService;

    @Test
    void deleteByIds_shouldDeleteEntitiesPermanently() {
        SysDictItemDomain domain = new SysDictItemDomain();
        domain.setId(new DictItemId(1L));

        when(repository.load(any(DictItemId.class))).thenReturn(Mono.just(domain));
        when(repository.save(any(SysDictItemDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(domainService.deleteByIds(Arrays.asList(1L, 2L)))
                .verifyComplete();

        verify(repository, times(2)).load(any(DictItemId.class));
        verify(repository, times(2)).save(any(SysDictItemDomain.class));
    }

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        StepVerifier.create(domainService.deleteByIds(Arrays.asList()))
                .verifyComplete();

        verify(repository, never()).load(any(DictItemId.class));
        verify(repository, never()).save(any(SysDictItemDomain.class));
    }

    @Test
    void deleteByIds_shouldHandleNonExistingId() {
        when(repository.load(any(DictItemId.class))).thenReturn(Mono.empty());

        StepVerifier.create(domainService.deleteByIds(Arrays.asList(999L)))
                .verifyComplete();

        verify(repository, times(1)).load(any(DictItemId.class));
        verify(repository, never()).save(any(SysDictItemDomain.class));
    }
}
