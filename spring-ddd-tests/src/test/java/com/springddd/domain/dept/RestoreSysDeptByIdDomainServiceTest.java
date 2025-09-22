package com.springddd.domain.dept;

import com.springddd.application.service.dept.RestoreSysDeptByIdDomainServiceImpl;
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
class RestoreSysDeptByIdDomainServiceTest {

    @Mock
    private SysDeptDomainRepository sysDeptDomainRepository;

    @InjectMocks
    private RestoreSysDeptByIdDomainServiceImpl restoreSysDeptByIdDomainService;

    private SysDeptDomain mockDomain;

    @BeforeEach
    void setUp() {
        mockDomain = new SysDeptDomain();
        mockDomain.setId(new DeptId(1L));
        mockDomain.setDeptBasicInfo(new DeptBasicInfo("Test Dept"));
        mockDomain.setDeleteStatus(true);
    }

    @Test
    void restoreByIds_shouldRestoreSingleId() {
        List<Long> ids = Collections.singletonList(1L);

        when(sysDeptDomainRepository.load(new DeptId(1L))).thenReturn(Mono.just(mockDomain));
        when(sysDeptDomainRepository.save(any(SysDeptDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysDeptByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysDeptDomainRepository).load(new DeptId(1L));
        verify(sysDeptDomainRepository).save(any(SysDeptDomain.class));
    }

    @Test
    void restoreByIds_shouldRestoreMultipleIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        SysDeptDomain domain1 = new SysDeptDomain();
        domain1.setId(new DeptId(1L));
        domain1.setDeleteStatus(true);

        SysDeptDomain domain2 = new SysDeptDomain();
        domain2.setId(new DeptId(2L));
        domain2.setDeleteStatus(true);

        SysDeptDomain domain3 = new SysDeptDomain();
        domain3.setId(new DeptId(3L));
        domain3.setDeleteStatus(true);

        when(sysDeptDomainRepository.load(new DeptId(1L))).thenReturn(Mono.just(domain1));
        when(sysDeptDomainRepository.load(new DeptId(2L))).thenReturn(Mono.just(domain2));
        when(sysDeptDomainRepository.load(new DeptId(3L))).thenReturn(Mono.just(domain3));
        when(sysDeptDomainRepository.save(any(SysDeptDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysDeptByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysDeptDomainRepository).load(new DeptId(1L));
        verify(sysDeptDomainRepository).load(new DeptId(2L));
        verify(sysDeptDomainRepository).load(new DeptId(3L));
    }

    @Test
    void restoreByIds_shouldSetDeleteStatusToFalse() {
        List<Long> ids = Collections.singletonList(1L);

        when(sysDeptDomainRepository.load(new DeptId(1L))).thenReturn(Mono.just(mockDomain));
        when(sysDeptDomainRepository.save(any(SysDeptDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysDeptByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysDeptDomainRepository).save(argThat(domain -> domain.getDeleteStatus() == false));
    }

    @Test
    void restoreByIds_shouldContinueWhenDomainNotFound() {
        List<Long> ids = Arrays.asList(1L, 999L);

        SysDeptDomain domain1 = new SysDeptDomain();
        domain1.setId(new DeptId(1L));
        domain1.setDeleteStatus(true);

        when(sysDeptDomainRepository.load(new DeptId(1L))).thenReturn(Mono.just(domain1));
        when(sysDeptDomainRepository.load(new DeptId(999L))).thenReturn(Mono.empty());
        when(sysDeptDomainRepository.save(any(SysDeptDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysDeptByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysDeptDomainRepository).load(new DeptId(1L));
        verify(sysDeptDomainRepository).load(new DeptId(999L));
    }
}
