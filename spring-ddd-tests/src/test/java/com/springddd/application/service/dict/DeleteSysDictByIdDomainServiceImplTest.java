package com.springddd.application.service.dict;

import com.springddd.application.service.dict.DeleteSysDictByIdDomainServiceImpl;
import com.springddd.domain.dict.DictBasicInfo;
import com.springddd.domain.dict.DictExtendInfo;
import com.springddd.domain.dict.DictId;
import com.springddd.domain.dict.SysDictDomain;
import com.springddd.domain.dict.SysDictDomainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteSysDictByIdDomainServiceImplTest {

    @Mock
    private SysDictDomainRepository sysDictDomainRepository;

    @InjectMocks
    private DeleteSysDictByIdDomainServiceImpl deleteSysDictByIdDomainService;

    private SysDictDomain mockDomain;

    @BeforeEach
    void setUp() {
        mockDomain = new SysDictDomain();
        mockDomain.setDictId(new DictId(1L));
        mockDomain.setDictBasicInfo(new DictBasicInfo("Test Dict", "TEST_DICT"));
        mockDomain.setDictExtendInfo(new DictExtendInfo(1, true));
        mockDomain.setDeleteStatus(false);
    }

    @Test
    void deleteByIds_shouldDeleteSingleId() {
        List<Long> ids = Collections.singletonList(1L);

        when(sysDictDomainRepository.load(new DictId(1L))).thenReturn(Mono.just(mockDomain));
        when(sysDictDomainRepository.save(any(SysDictDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysDictByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDictDomainRepository).load(new DictId(1L));
        verify(sysDictDomainRepository).save(any(SysDictDomain.class));
    }

    @Test
    void deleteByIds_shouldDeleteMultipleIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        SysDictDomain domain1 = new SysDictDomain();
        domain1.setDictId(new DictId(1L));
        domain1.setDeleteStatus(false);

        SysDictDomain domain2 = new SysDictDomain();
        domain2.setDictId(new DictId(2L));
        domain2.setDeleteStatus(false);

        SysDictDomain domain3 = new SysDictDomain();
        domain3.setDictId(new DictId(3L));
        domain3.setDeleteStatus(false);

        when(sysDictDomainRepository.load(new DictId(1L))).thenReturn(Mono.just(domain1));
        when(sysDictDomainRepository.load(new DictId(2L))).thenReturn(Mono.just(domain2));
        when(sysDictDomainRepository.load(new DictId(3L))).thenReturn(Mono.just(domain3));
        when(sysDictDomainRepository.save(any(SysDictDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysDictByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDictDomainRepository).load(new DictId(1L));
        verify(sysDictDomainRepository).load(new DictId(2L));
        verify(sysDictDomainRepository).load(new DictId(3L));
    }

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        StepVerifier.create(deleteSysDictByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDictDomainRepository, never()).load(any());
        verify(sysDictDomainRepository, never()).save(any());
    }

    @Test
    void deleteByIds_shouldContinueWhenDomainNotFound() {
        List<Long> ids = Arrays.asList(1L, 999L);

        SysDictDomain domain1 = new SysDictDomain();
        domain1.setDictId(new DictId(1L));
        domain1.setDeleteStatus(false);

        when(sysDictDomainRepository.load(new DictId(1L))).thenReturn(Mono.just(domain1));
        when(sysDictDomainRepository.load(new DictId(999L))).thenReturn(Mono.empty());
        when(sysDictDomainRepository.save(any(SysDictDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysDictByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDictDomainRepository).load(new DictId(1L));
        verify(sysDictDomainRepository).load(new DictId(999L));
    }

    @Test
    void deleteByIds_shouldSetDeleteStatusToTrue() {
        List<Long> ids = Collections.singletonList(1L);

        when(sysDictDomainRepository.load(new DictId(1L))).thenReturn(Mono.just(mockDomain));
        when(sysDictDomainRepository.save(any(SysDictDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysDictByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDictDomainRepository).save(argThat(domain -> domain.getDeleteStatus() == true));
    }
}
