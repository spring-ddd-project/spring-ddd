package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptView;
import com.springddd.domain.dept.WipeSysDeptByIdsDomainService;
import com.springddd.infrastructure.persistence.r2dbc.SysDeptRepository;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysDeptByIdsDomainServiceTest {

    @Mock
    private SysDeptRepository sysDeptRepository;

    @Mock
    private SysDeptQueryService sysDeptQueryService;

    @InjectMocks
    private WipeSysDeptByIdsDomainServiceImpl wipeSysDeptByIdsDomainService;

    private SysDeptView mockView;

    @BeforeEach
    void setUp() {
        mockView = new SysDeptView();
        mockView.setId(1L);
        mockView.setParentId(0L);
        mockView.setDeptName("Test");
        mockView.setDeleteStatus(false);
    }

    @Test
    void deleteByIds_shouldWipeDepartmentAndChildren() {
        List<Long> ids = Arrays.asList(1L);
        List<SysDeptView> allDepts = Arrays.asList(mockView);

        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(allDepts));
        when(sysDeptRepository.deleteById(eq(1L))).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysDeptByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDeptQueryService).queryAllDept();
        verify(sysDeptRepository).deleteById(eq(1L));
    }

    @Test
    void deleteByIds_shouldHandleEmptyIdsList() {
        List<Long> ids = Collections.emptyList();
        List<SysDeptView> allDepts = Arrays.asList(mockView);

        // Even with empty ids, queryAllDept is still called
        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(allDepts));

        StepVerifier.create(wipeSysDeptByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDeptQueryService).queryAllDept();
        verify(sysDeptRepository, never()).deleteById(any(Long.class));
    }

    @Test
    void deleteByIds_shouldHandleNoDepartmentsFound() {
        List<Long> ids = Arrays.asList(999L);
        List<SysDeptView> allDepts = Collections.emptyList();

        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(allDepts));

        StepVerifier.create(wipeSysDeptByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDeptRepository, never()).deleteById(any(Long.class));
    }
}
