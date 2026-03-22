package com.springddd.application.service.gen;

import com.springddd.application.service.gen.RestoreGenTemplateDomainServiceImpl;
import com.springddd.domain.gen.GenTemplateDomain;
import com.springddd.domain.gen.GenTemplateDomainRepository;
import com.springddd.domain.gen.TemplateId;
import com.springddd.domain.gen.TemplateInfo;
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
class RestoreGenTemplateDomainServiceImplTest {

    @Mock
    private GenTemplateDomainRepository genTemplateDomainRepository;

    @InjectMocks
    private RestoreGenTemplateDomainServiceImpl restoreGenTemplateDomainService;

    private GenTemplateDomain mockDomain;

    @BeforeEach
    void setUp() {
        mockDomain = new GenTemplateDomain();
        mockDomain.setId(new TemplateId(1L));
        mockDomain.setTemplateInfo(new TemplateInfo("test", "content"));
        mockDomain.setDeleteStatus(true);
    }

    @Test
    void restoreByIds_shouldRestoreSingleId() {
        List<Long> ids = Collections.singletonList(1L);

        when(genTemplateDomainRepository.load(new TemplateId(1L))).thenReturn(Mono.just(mockDomain));
        when(genTemplateDomainRepository.save(any(GenTemplateDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreGenTemplateDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(genTemplateDomainRepository).load(new TemplateId(1L));
        verify(genTemplateDomainRepository).save(any(GenTemplateDomain.class));
    }

    @Test
    void restoreByIds_shouldRestoreMultipleIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        GenTemplateDomain domain1 = new GenTemplateDomain();
        domain1.setId(new TemplateId(1L));
        domain1.setDeleteStatus(true);

        GenTemplateDomain domain2 = new GenTemplateDomain();
        domain2.setId(new TemplateId(2L));
        domain2.setDeleteStatus(true);

        GenTemplateDomain domain3 = new GenTemplateDomain();
        domain3.setId(new TemplateId(3L));
        domain3.setDeleteStatus(true);

        when(genTemplateDomainRepository.load(new TemplateId(1L))).thenReturn(Mono.just(domain1));
        when(genTemplateDomainRepository.load(new TemplateId(2L))).thenReturn(Mono.just(domain2));
        when(genTemplateDomainRepository.load(new TemplateId(3L))).thenReturn(Mono.just(domain3));
        when(genTemplateDomainRepository.save(any(GenTemplateDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreGenTemplateDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(genTemplateDomainRepository).load(new TemplateId(1L));
        verify(genTemplateDomainRepository).load(new TemplateId(2L));
        verify(genTemplateDomainRepository).load(new TemplateId(3L));
    }

    @Test
    void restoreByIds_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        StepVerifier.create(restoreGenTemplateDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(genTemplateDomainRepository, never()).load(any());
        verify(genTemplateDomainRepository, never()).save(any());
    }

    @Test
    void restoreByIds_shouldContinueWhenDomainNotFound() {
        List<Long> ids = Arrays.asList(1L, 999L);

        GenTemplateDomain domain1 = new GenTemplateDomain();
        domain1.setId(new TemplateId(1L));
        domain1.setDeleteStatus(true);

        when(genTemplateDomainRepository.load(new TemplateId(1L))).thenReturn(Mono.just(domain1));
        when(genTemplateDomainRepository.load(new TemplateId(999L))).thenReturn(Mono.empty());
        when(genTemplateDomainRepository.save(any(GenTemplateDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreGenTemplateDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(genTemplateDomainRepository).load(new TemplateId(1L));
        verify(genTemplateDomainRepository).load(new TemplateId(999L));
    }

    @Test
    void restoreByIds_shouldSetDeleteStatusToFalse() {
        List<Long> ids = Collections.singletonList(1L);

        when(genTemplateDomainRepository.load(new TemplateId(1L))).thenReturn(Mono.just(mockDomain));
        when(genTemplateDomainRepository.save(any(GenTemplateDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreGenTemplateDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(genTemplateDomainRepository).save(argThat(domain -> domain.getDeleteStatus() == false));
    }
}
