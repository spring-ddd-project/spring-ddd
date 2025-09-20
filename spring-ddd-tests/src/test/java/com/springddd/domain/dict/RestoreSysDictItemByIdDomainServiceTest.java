package com.springddd.domain.dict;

import com.springddd.application.service.dict.RestoreSysDictItemByIdDomainServiceImpl;
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
class RestoreSysDictItemByIdDomainServiceTest {

    @Mock
    private SysDictItemDomainRepository sysDictItemDomainRepository;

    @InjectMocks
    private RestoreSysDictItemByIdDomainServiceImpl restoreSysDictItemByIdDomainService;

    private SysDictItemDomain mockDomain;

    @BeforeEach
    void setUp() {
        mockDomain = new SysDictItemDomain();
        mockDomain.setItemId(new DictItemId(1L));
        mockDomain.setDictId(new DictId(1L));
        mockDomain.setItemBasicInfo(new DictItemBasicInfo("Test Item", 1));
        mockDomain.setItemExtendInfo(new DictItemExtendInfo(1, true));
        mockDomain.setDeleteStatus(true);
    }

    @Test
    void restoreByIds_shouldRestoreSingleId() {
        List<Long> ids = Collections.singletonList(1L);

        when(sysDictItemDomainRepository.load(new DictItemId(1L))).thenReturn(Mono.just(mockDomain));
        when(sysDictItemDomainRepository.save(any(SysDictItemDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysDictItemByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysDictItemDomainRepository).load(new DictItemId(1L));
        verify(sysDictItemDomainRepository).save(any(SysDictItemDomain.class));
    }

    @Test
    void restoreByIds_shouldRestoreMultipleIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        SysDictItemDomain domain1 = new SysDictItemDomain();
        domain1.setItemId(new DictItemId(1L));
        domain1.setDeleteStatus(true);

        SysDictItemDomain domain2 = new SysDictItemDomain();
        domain2.setItemId(new DictItemId(2L));
        domain2.setDeleteStatus(true);

        SysDictItemDomain domain3 = new SysDictItemDomain();
        domain3.setItemId(new DictItemId(3L));
        domain3.setDeleteStatus(true);

        when(sysDictItemDomainRepository.load(new DictItemId(1L))).thenReturn(Mono.just(domain1));
        when(sysDictItemDomainRepository.load(new DictItemId(2L))).thenReturn(Mono.just(domain2));
        when(sysDictItemDomainRepository.load(new DictItemId(3L))).thenReturn(Mono.just(domain3));
        when(sysDictItemDomainRepository.save(any(SysDictItemDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysDictItemByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysDictItemDomainRepository).load(new DictItemId(1L));
        verify(sysDictItemDomainRepository).load(new DictItemId(2L));
        verify(sysDictItemDomainRepository).load(new DictItemId(3L));
    }

    @Test
    void restoreByIds_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        StepVerifier.create(restoreSysDictItemByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysDictItemDomainRepository, never()).load(any());
        verify(sysDictItemDomainRepository, never()).save(any());
    }

    @Test
    void restoreByIds_shouldContinueWhenDomainNotFound() {
        List<Long> ids = Arrays.asList(1L, 999L);

        SysDictItemDomain domain1 = new SysDictItemDomain();
        domain1.setItemId(new DictItemId(1L));
        domain1.setDeleteStatus(true);

        when(sysDictItemDomainRepository.load(new DictItemId(1L))).thenReturn(Mono.just(domain1));
        when(sysDictItemDomainRepository.load(new DictItemId(999L))).thenReturn(Mono.empty());
        when(sysDictItemDomainRepository.save(any(SysDictItemDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysDictItemByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysDictItemDomainRepository).load(new DictItemId(1L));
        verify(sysDictItemDomainRepository).load(new DictItemId(999L));
    }

    @Test
    void restoreByIds_shouldSetDeleteStatusToFalse() {
        List<Long> ids = Collections.singletonList(1L);

        when(sysDictItemDomainRepository.load(new DictItemId(1L))).thenReturn(Mono.just(mockDomain));
        when(sysDictItemDomainRepository.save(any(SysDictItemDomain.class))).thenAnswer(invocation -> {
            SysDictItemDomain savedDomain = invocation.getArgument(0);
            return Mono.just(1L);
        });

        StepVerifier.create(restoreSysDictItemByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysDictItemDomainRepository).save(argThat(domain -> domain.getDeleteStatus() == false));
    }
}
