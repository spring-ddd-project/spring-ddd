package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptView;
import com.springddd.infrastructure.persistence.r2dbc.SysDeptRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysDeptByIdsDomainServiceImplTest {

    @Mock
    private SysDeptRepository sysDeptRepository;

    @Mock
    private SysDeptQueryService sysDeptQueryService;

    @InjectMocks
    private WipeSysDeptByIdsDomainServiceImpl service;

    @Test
    @DisplayName("deleteByIds 应查询所有部门并物理删除指定部门及其子部门")
    void deleteByIds_shouldWipeAndChildren() {
        SysDeptView parent = new SysDeptView();
        parent.setId(1L);
        parent.setParentId(null);

        SysDeptView child = new SysDeptView();
        child.setId(2L);
        child.setParentId(1L);

        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(List.of(parent, child)));
        when(sysDeptRepository.deleteById(eq(1L))).thenReturn(Mono.empty());
        when(sysDeptRepository.deleteById(eq(2L))).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();

        verify(sysDeptRepository).deleteById(eq(1L));
        verify(sysDeptRepository).deleteById(eq(2L));
    }
}
