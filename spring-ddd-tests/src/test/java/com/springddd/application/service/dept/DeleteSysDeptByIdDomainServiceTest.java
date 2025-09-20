package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptView;
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
class DeleteSysDeptByIdDomainServiceTest {

    @Mock
    private SysDeptDomainRepository sysDeptDomainRepository;

    @Mock
    private SysDeptQueryService sysDeptQueryService;

    @InjectMocks
    private DeleteSysDeptByIdDomainServiceImpl deleteSysDeptByIdDomainService;

    private SysDeptDomain mockDomain;
    private SysDeptView mockView;

    @BeforeEach
    void setUp() {
        mockDomain = new SysDeptDomain();
        mockDomain.setId(new DeptId(1L));
        mockDomain.setDeptBasicInfo(new DeptBasicInfo("Test"));
        mockDomain.setDeptExtendInfo(new DeptExtendInfo(1, true));
        mockDomain.setDeleteStatus(false);

        mockView = new SysDeptView();
        mockView.setId(1L);
        mockView.setParentId(0L);
        mockView.setDeptName("Test");
        mockView.setDeleteStatus(false);
    }

    @Test
    void deleteByIds_shouldDeleteDepartmentAndChildren() {
        List<Long> ids = Arrays.asList(1L);
        List<SysDeptView> allDepts = Arrays.asList(mockView);

        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(allDepts));
        when(sysDeptDomainRepository.load(any(DeptId.class))).thenReturn(Mono.just(mockDomain));
        when(sysDeptDomainRepository.save(any(SysDeptDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysDeptByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDeptQueryService).queryAllDept();
        verify(sysDeptDomainRepository).load(any(DeptId.class));
        verify(sysDeptDomainRepository).save(any(SysDeptDomain.class));
    }

    @Test
    void deleteByIds_shouldHandleEmptyIdsList() {
        List<Long> ids = Collections.emptyList();
        List<SysDeptView> allDepts = Arrays.asList(mockView);

        // Even with empty ids, queryAllDept is still called
        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(allDepts));

        StepVerifier.create(deleteSysDeptByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        // queryAllDept is called but no domains are loaded since ids is empty
        verify(sysDeptQueryService).queryAllDept();
        verify(sysDeptDomainRepository, never()).load(any());
    }

    @Test
    void deleteByIds_shouldHandleNoDepartmentFound() {
        List<Long> ids = Arrays.asList(999L);
        List<SysDeptView> allDepts = Collections.emptyList();

        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(allDepts));

        StepVerifier.create(deleteSysDeptByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDeptDomainRepository, never()).load(any());
    }
}
