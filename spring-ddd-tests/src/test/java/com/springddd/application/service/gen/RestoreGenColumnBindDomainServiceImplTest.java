package com.springddd.application.service.gen;

import com.springddd.application.service.gen.RestoreGenColumnBindDomainServiceImpl;
import com.springddd.domain.gen.ColumnBindId;
import com.springddd.domain.gen.GenColumnBindBasicInfo;
import com.springddd.domain.gen.GenColumnBindDomain;
import com.springddd.domain.gen.GenColumnBindDomainRepository;
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
class RestoreGenColumnBindDomainServiceImplTest {

    @Mock
    private GenColumnBindDomainRepository domainRepository;

    @InjectMocks
    private RestoreGenColumnBindDomainServiceImpl restoreGenColumnBindDomainService;

    private GenColumnBindDomain mockDomain;

    @BeforeEach
    void setUp() {
        mockDomain = new GenColumnBindDomain();
        mockDomain.setBindId(new ColumnBindId(1L));
        mockDomain.setBasicInfo(new GenColumnBindBasicInfo("columnType", "entityType", (byte) 1, (byte) 1));
        mockDomain.setDeleteStatus(true);
    }

    @Test
    void restoreByIds_shouldRestoreSingleId() {
        List<Long> ids = Collections.singletonList(1L);

        when(domainRepository.load(new ColumnBindId(1L))).thenReturn(Mono.just(mockDomain));
        when(domainRepository.save(any(GenColumnBindDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreGenColumnBindDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(domainRepository).load(new ColumnBindId(1L));
        verify(domainRepository).save(any(GenColumnBindDomain.class));
    }

    @Test
    void restoreByIds_shouldRestoreMultipleIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        GenColumnBindDomain domain1 = new GenColumnBindDomain();
        domain1.setBindId(new ColumnBindId(1L));
        domain1.setDeleteStatus(true);

        GenColumnBindDomain domain2 = new GenColumnBindDomain();
        domain2.setBindId(new ColumnBindId(2L));
        domain2.setDeleteStatus(true);

        GenColumnBindDomain domain3 = new GenColumnBindDomain();
        domain3.setBindId(new ColumnBindId(3L));
        domain3.setDeleteStatus(true);

        when(domainRepository.load(new ColumnBindId(1L))).thenReturn(Mono.just(domain1));
        when(domainRepository.load(new ColumnBindId(2L))).thenReturn(Mono.just(domain2));
        when(domainRepository.load(new ColumnBindId(3L))).thenReturn(Mono.just(domain3));
        when(domainRepository.save(any(GenColumnBindDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreGenColumnBindDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(domainRepository).load(new ColumnBindId(1L));
        verify(domainRepository).load(new ColumnBindId(2L));
        verify(domainRepository).load(new ColumnBindId(3L));
    }

    @Test
    void restoreByIds_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        StepVerifier.create(restoreGenColumnBindDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(domainRepository, never()).load(any());
        verify(domainRepository, never()).save(any());
    }

    @Test
    void restoreByIds_shouldContinueWhenDomainNotFound() {
        List<Long> ids = Arrays.asList(1L, 999L);

        GenColumnBindDomain domain1 = new GenColumnBindDomain();
        domain1.setBindId(new ColumnBindId(1L));
        domain1.setDeleteStatus(true);

        when(domainRepository.load(new ColumnBindId(1L))).thenReturn(Mono.just(domain1));
        when(domainRepository.load(new ColumnBindId(999L))).thenReturn(Mono.empty());
        when(domainRepository.save(any(GenColumnBindDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreGenColumnBindDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(domainRepository).load(new ColumnBindId(1L));
        verify(domainRepository).load(new ColumnBindId(999L));
    }

    @Test
    void restoreByIds_shouldSetDeleteStatusToFalse() {
        List<Long> ids = Collections.singletonList(1L);

        when(domainRepository.load(new ColumnBindId(1L))).thenReturn(Mono.just(mockDomain));
        when(domainRepository.save(any(GenColumnBindDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreGenColumnBindDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(domainRepository).save(argThat(domain -> domain.getDeleteStatus() == false));
    }
}
