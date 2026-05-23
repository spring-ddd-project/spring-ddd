package com.springddd.application.service.dept;

import com.springddd.application.service.dept.dto.SysDeptView;
import com.springddd.domain.dept.DeptId;
import com.springddd.domain.dept.SysDeptDomain;
import com.springddd.domain.dept.SysDeptDomainRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteSysDeptByIdDomainServiceImplTest {

    @Mock
    private SysDeptDomainRepository sysDeptDomainRepository;

    @Mock
    private SysDeptQueryService sysDeptQueryService;

    @InjectMocks
    private DeleteSysDeptByIdDomainServiceImpl service;

    @Test
    @DisplayName("deleteByIds 应查询所有部门并删除指定部门及其子部门")
    void deleteByIds_shouldDeleteAndChildren() {
        SysDeptView parent = new SysDeptView();
        parent.setId(1L);
        parent.setParentId(null);

        SysDeptView child = new SysDeptView();
        child.setId(2L);
        child.setParentId(1L);

        when(sysDeptQueryService.queryAllDept()).thenReturn(Mono.just(List.of(parent, child)));

        SysDeptDomain domain1 = mock(SysDeptDomain.class);
        SysDeptDomain domain2 = mock(SysDeptDomain.class);
        when(sysDeptDomainRepository.load(new DeptId(1L))).thenReturn(Mono.just(domain1));
        when(sysDeptDomainRepository.load(new DeptId(2L))).thenReturn(Mono.just(domain2));
        when(sysDeptDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();

        verify(domain1).delete();
        verify(domain2).delete();
        verify(sysDeptDomainRepository, times(2)).save(any());
    }
}
