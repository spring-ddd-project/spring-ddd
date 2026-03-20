package com.springddd.domain.dept;

import com.springddd.application.service.dept.SysDeptQueryService;
import com.springddd.application.service.dept.WipeSysDeptByIdsDomainServiceImpl;
import com.springddd.application.service.dept.dto.SysDeptView;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysDeptByIdsDomainServiceTest {

    @Mock
    private SysDeptRepository sysDeptRepository;

    @Mock
    private SysDeptQueryService sysDeptQueryService;

    @InjectMocks
    private WipeSysDeptByIdsDomainServiceImpl wipeSysDeptByIdsDomainService;

    @Test
    void deleteByIds_shouldDeleteSingleId() {
        List<Long> ids = Collections.singletonList(1L);
        List<SysDeptView> deptViews = Collections.singletonList(createDeptView(1L, null));

        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(deptViews));
        when(sysDeptRepository.deleteById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysDeptByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDeptRepository).deleteById(1L);
    }

    @Test
    void deleteByIds_shouldDeleteMultipleIds() {
        List<Long> ids = Collections.singletonList(1L);
        List<SysDeptView> deptViews = Arrays.asList(
                createDeptView(1L, null),
                createDeptView(2L, 1L),
                createDeptView(3L, 1L)
        );

        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(deptViews));
        when(sysDeptRepository.deleteById(1L)).thenReturn(Mono.empty());
        when(sysDeptRepository.deleteById(2L)).thenReturn(Mono.empty());
        when(sysDeptRepository.deleteById(3L)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysDeptByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDeptRepository).deleteById(1L);
        verify(sysDeptRepository).deleteById(2L);
        verify(sysDeptRepository).deleteById(3L);
    }

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(Collections.emptyList()));

        StepVerifier.create(wipeSysDeptByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDeptRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteByIds_shouldDeleteParentAndAllChildren() {
        List<Long> ids = Collections.singletonList(1L);
        List<SysDeptView> deptViews = Arrays.asList(
                createDeptView(1L, null),
                createDeptView(2L, 1L),
                createDeptView(3L, 1L),
                createDeptView(4L, 2L)
        );

        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(deptViews));
        when(sysDeptRepository.deleteById(1L)).thenReturn(Mono.empty());
        when(sysDeptRepository.deleteById(2L)).thenReturn(Mono.empty());
        when(sysDeptRepository.deleteById(3L)).thenReturn(Mono.empty());
        when(sysDeptRepository.deleteById(4L)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysDeptByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDeptRepository).deleteById(1L);
        verify(sysDeptRepository).deleteById(2L);
        verify(sysDeptRepository).deleteById(3L);
        verify(sysDeptRepository).deleteById(4L);
    }

    private SysDeptView createDeptView(Long id, Long parentId) {
        SysDeptView view = new SysDeptView();
        view.setId(id);
        view.setParentId(parentId);
        view.setDeleteStatus(false);
        return view;
    }
}
