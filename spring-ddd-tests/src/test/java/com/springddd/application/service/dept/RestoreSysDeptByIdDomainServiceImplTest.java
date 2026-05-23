package com.springddd.application.service.dept;

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
class RestoreSysDeptByIdDomainServiceImplTest {

    @Mock
    private SysDeptDomainRepository sysDeptDomainRepository;

    @InjectMocks
    private RestoreSysDeptByIdDomainServiceImpl service;

    @Test
    @DisplayName("restoreByIds 应加载 domain 并调用 restore 和 save")
    void restoreByIds_shouldRestoreAndSave() {
        SysDeptDomain domain = mock(SysDeptDomain.class);
        when(sysDeptDomainRepository.load(new DeptId(1L))).thenReturn(Mono.just(domain));
        when(sysDeptDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.restoreByIds(List.of(1L)))
                .verifyComplete();

        verify(domain).restore();
        verify(sysDeptDomainRepository).save(domain);
    }

    @Test
    @DisplayName("restoreByIds 应处理多个 ids")
    void restoreByIds_shouldHandleMultipleIds() {
        SysDeptDomain domain1 = mock(SysDeptDomain.class);
        SysDeptDomain domain2 = mock(SysDeptDomain.class);
        when(sysDeptDomainRepository.load(new DeptId(1L))).thenReturn(Mono.just(domain1));
        when(sysDeptDomainRepository.load(new DeptId(2L))).thenReturn(Mono.just(domain2));
        when(sysDeptDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(service.restoreByIds(List.of(1L, 2L)))
                .verifyComplete();

        verify(domain1).restore();
        verify(domain2).restore();
        verify(sysDeptDomainRepository, times(2)).save(any());
    }
}
