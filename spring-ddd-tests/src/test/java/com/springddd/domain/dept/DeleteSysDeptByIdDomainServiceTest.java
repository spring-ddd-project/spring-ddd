package com.springddd.domain.dept;

import com.springddd.application.service.dept.DeleteSysDeptByIdDomainServiceImpl;
import com.springddd.application.service.dept.SysDeptQueryService;
import com.springddd.application.service.dept.dto.SysDeptView;
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

    @BeforeEach
    void setUp() {
        mockDomain = new SysDeptDomain();
        mockDomain.setId(new DeptId(1L));
        mockDomain.setDeptBasicInfo(new DeptBasicInfo("Test Dept"));
        mockDomain.setDeleteStatus(false);
    }

    @Test
    void deleteByIds_shouldDeleteSingleId() {
        List<Long> ids = Collections.singletonList(1L);
        List<SysDeptView> deptViews = Collections.singletonList(createDeptView(1L, null));

        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(deptViews));
        when(sysDeptDomainRepository.load(new DeptId(1L))).thenReturn(Mono.just(mockDomain));
        when(sysDeptDomainRepository.save(any(SysDeptDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysDeptByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDeptDomainRepository).load(new DeptId(1L));
        verify(sysDeptDomainRepository).save(any(SysDeptDomain.class));
    }

    @Test
    void deleteByIds_shouldDeleteMultipleIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<SysDeptView> deptViews = Arrays.asList(
                createDeptView(1L, null),
                createDeptView(2L, 1L),
                createDeptView(3L, 1L)
        );

        SysDeptDomain domain1 = new SysDeptDomain();
        domain1.setId(new DeptId(1L));
        domain1.setDeleteStatus(false);

        SysDeptDomain domain2 = new SysDeptDomain();
        domain2.setId(new DeptId(2L));
        domain2.setDeleteStatus(false);

        SysDeptDomain domain3 = new SysDeptDomain();
        domain3.setId(new DeptId(3L));
        domain3.setDeleteStatus(false);

        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(deptViews));
        when(sysDeptDomainRepository.load(new DeptId(1L))).thenReturn(Mono.just(domain1));
        when(sysDeptDomainRepository.load(new DeptId(2L))).thenReturn(Mono.just(domain2));
        when(sysDeptDomainRepository.load(new DeptId(3L))).thenReturn(Mono.just(domain3));
        when(sysDeptDomainRepository.save(any(SysDeptDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysDeptByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDeptDomainRepository).load(new DeptId(1L));
        verify(sysDeptDomainRepository).load(new DeptId(2L));
        verify(sysDeptDomainRepository).load(new DeptId(3L));
    }

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(Collections.emptyList()));

        StepVerifier.create(deleteSysDeptByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDeptDomainRepository, never()).load(any());
        verify(sysDeptDomainRepository, never()).save(any());
    }

    @Test
    void deleteByIds_shouldSetDeleteStatusToTrue() {
        List<Long> ids = Collections.singletonList(1L);
        List<SysDeptView> deptViews = Collections.singletonList(createDeptView(1L, null));

        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(deptViews));
        when(sysDeptDomainRepository.load(new DeptId(1L))).thenReturn(Mono.just(mockDomain));
        when(sysDeptDomainRepository.save(any(SysDeptDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysDeptByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDeptDomainRepository).save(argThat(domain -> domain.getDeleteStatus() == true));
    }

    private SysDeptView createDeptView(Long id, Long parentId) {
        SysDeptView view = new SysDeptView();
        view.setId(id);
        view.setParentId(parentId);
        view.setDeleteStatus(false);
        return view;
    }
}
