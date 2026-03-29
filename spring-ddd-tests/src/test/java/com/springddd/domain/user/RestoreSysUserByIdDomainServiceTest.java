package com.springddd.domain.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestoreSysUserByIdDomainServiceTest {

    @Mock
    private SysUserDomainRepository repository;

    @InjectMocks
    private RestoreSysUserByIdDomainService domainService;

    @Test
    void restoreByIds_shouldRestoreEntities() {
        SysUserDomain domain = new SysUserDomain();
        domain.setId(new UserId(1L));
        domain.setUserId(new UserId(1L));
        domain.delete();

        when(repository.load(any(UserId.class))).thenReturn(Mono.just(domain));
        when(repository.save(any(SysUserDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(domainService.restoreByIds(Arrays.asList(1L, 2L)))
                .verifyComplete();

        verify(repository, times(2)).load(any(UserId.class));
        verify(repository, times(2)).save(any(SysUserDomain.class));
    }

    @Test
    void restoreByIds_shouldHandleEmptyList() {
        StepVerifier.create(domainService.restoreByIds(Arrays.asList()))
                .verifyComplete();

        verify(repository, never()).load(any(UserId.class));
        verify(repository, never()).save(any(SysUserDomain.class));
    }

    @Test
    void restoreByIds_shouldHandleNonExistingId() {
        when(repository.load(any(UserId.class))).thenReturn(Mono.empty());

        StepVerifier.create(domainService.restoreByIds(Arrays.asList(999L)))
                .verifyComplete();

        verify(repository, times(1)).load(any(UserId.class));
        verify(repository, never()).save(any(SysUserDomain.class));
    }
}
