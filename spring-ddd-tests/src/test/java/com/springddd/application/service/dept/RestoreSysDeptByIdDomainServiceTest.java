package com.springddd.application.service.dept;

import com.springddd.domain.dept.*;
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
        mockDomain.setDeptBasicInfo(new DeptBasicInfo("Test"));
        mockDomain.setDeptExtendInfo(new DeptExtendInfo(1, true));
        mockDomain.setDeleteStatus(true);
    }

    @Test
    void restoreByIds_shouldRestoreDepartment() {
        List<Long> ids = Arrays.asList(1L);

        when(sysDeptDomainRepository.load(any(DeptId.class))).thenReturn(Mono.just(mockDomain));
        when(sysDeptDomainRepository.save(any(SysDeptDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysDeptByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysDeptDomainRepository).load(any(DeptId.class));
        verify(sysDeptDomainRepository).save(any(SysDeptDomain.class));
    }

    @Test
    void restoreByIds_shouldRestoreMultipleDepartments() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        SysDeptDomain domain1 = new SysDeptDomain();
        domain1.setDeleteStatus(true);
        SysDeptDomain domain2 = new SysDeptDomain();
        domain2.setDeleteStatus(true);
        SysDeptDomain domain3 = new SysDeptDomain();
        domain3.setDeleteStatus(true);

        when(sysDeptDomainRepository.load(new DeptId(1L))).thenReturn(Mono.just(domain1));
        when(sysDeptDomainRepository.load(new DeptId(2L))).thenReturn(Mono.just(domain2));
        when(sysDeptDomainRepository.load(new DeptId(3L))).thenReturn(Mono.just(domain3));
        when(sysDeptDomainRepository.save(any(SysDeptDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysDeptByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysDeptDomainRepository, times(3)).load(any(DeptId.class));
        verify(sysDeptDomainRepository, times(3)).save(any(SysDeptDomain.class));
    }

    @Test
    void restoreByIds_shouldHandleEmptyIdsList() {
        List<Long> ids = Collections.emptyList();

        StepVerifier.create(restoreSysDeptByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysDeptDomainRepository, never()).load(any());
    }

    @Test
    void restoreByIds_shouldHandleDepartmentNotFound() {
        List<Long> ids = Arrays.asList(999L);

        when(sysDeptDomainRepository.load(any(DeptId.class))).thenReturn(Mono.empty());

        StepVerifier.create(restoreSysDeptByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysDeptDomainRepository).load(any(DeptId.class));
        verify(sysDeptDomainRepository, never()).save(any());
    }
}
