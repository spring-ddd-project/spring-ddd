package com.springddd.application.service.dict;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.dict.DeleteSysDictItemByIdDomainService;
import com.springddd.domain.dict.DictItemId;
import com.springddd.domain.dict.SysDictItemDomain;
import com.springddd.domain.dict.SysDictItemDomainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteSysDictItemByIdDomainServiceImplTest {

    @Mock
    private SysDictItemDomainRepository sysDictItemDomainRepository;

    private DeleteSysDictItemByIdDomainServiceImpl deleteSysDictItemByIdDomainService;

    @BeforeEach
    void setUp() {
        deleteSysDictItemByIdDomainService = new DeleteSysDictItemByIdDomainServiceImpl(sysDictItemDomainRepository);
    }

    @Test
    void shouldDeleteByIdsSuccessfully() {
        Long dictItemId = 1L;
        SysDictItemDomain domain = new SysDictItemDomain();

        when(sysDictItemDomainRepository.load(any(DictItemId.class))).thenReturn(Mono.just(domain));
        when(sysDictItemDomainRepository.save(any(SysDictItemDomain.class))).thenReturn(Mono.just(dictItemId));
        when(SecurityUtils.concurrency()).thenReturn(reactor.util.concurrent.Queues.<SysDictItemDomain>unbounded(16).toContextWrite());

        List<Long> ids = Arrays.asList(dictItemId);

        Mono<Void> result = deleteSysDictItemByIdDomainService.deleteByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();

        verify(sysDictItemDomainRepository).load(any(DictItemId.class));
    }

    @Test
    void shouldDeleteMultipleDictItems() {
        Long dictItemId1 = 1L;
        Long dictItemId2 = 2L;
        SysDictItemDomain domain1 = new SysDictItemDomain();
        SysDictItemDomain domain2 = new SysDictItemDomain();

        when(sysDictItemDomainRepository.load(any(DictItemId.class))).thenReturn(Mono.just(domain1), Mono.just(domain2));
        when(sysDictItemDomainRepository.save(any(SysDictItemDomain.class))).thenReturn(Mono.just(1L));
        when(SecurityUtils.concurrency()).thenReturn(reactor.util.concurrent.Queues.<SysDictItemDomain>unbounded(16).toContextWrite());

        List<Long> ids = Arrays.asList(dictItemId1, dictItemId2);

        Mono<Void> result = deleteSysDictItemByIdDomainService.deleteByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();

        verify(sysDictItemDomainRepository, times(2)).load(any(DictItemId.class));
    }
}
