package com.springddd.application.service.user;

import com.springddd.domain.user.*;
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
class RestoreSysUserByIdDomainServiceTest {

    @Mock
    private SysUserDomainRepository sysUserDomainRepository;

    @InjectMocks
    private RestoreSysUserByIdDomainServiceImpl restoreSysUserByIdDomainService;

    private SysUserDomain mockDomain;

    @BeforeEach
    void setUp() {
        mockDomain = new SysUserDomain();
        mockDomain.setUserId(new UserId(1L));
        Account account = new Account();
        account.setUsername(new Username("testuser"));
        account.setPassword(new Password("password"));
        account.setLockStatus(false);
        mockDomain.setAccount(account);
        mockDomain.setDeptId(1L);
        mockDomain.setDeleteStatus(true); // Initially deleted
    }

    @Test
    void restoreByIds_shouldRestoreUsers() {
        List<Long> ids = Arrays.asList(1L, 2L);

        when(sysUserDomainRepository.load(new UserId(1L))).thenReturn(Mono.just(mockDomain));
        when(sysUserDomainRepository.load(new UserId(2L))).thenReturn(Mono.empty());
        when(sysUserDomainRepository.save(any(SysUserDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysUserByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysUserDomainRepository).load(new UserId(1L));
        verify(sysUserDomainRepository).save(any(SysUserDomain.class));
    }

    @Test
    void restoreByIds_shouldHandleEmptyIdsList() {
        List<Long> ids = Collections.emptyList();

        StepVerifier.create(restoreSysUserByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysUserDomainRepository, never()).load(any());
        verify(sysUserDomainRepository, never()).save(any());
    }

    @Test
    void restoreByIds_shouldHandleUserNotFound() {
        List<Long> ids = Arrays.asList(999L);

        when(sysUserDomainRepository.load(new UserId(999L))).thenReturn(Mono.empty());

        StepVerifier.create(restoreSysUserByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysUserDomainRepository).load(new UserId(999L));
        verify(sysUserDomainRepository, never()).save(any());
    }

    @Test
    void restoreByIds_shouldSetDeleteStatusToFalse() {
        List<Long> ids = Arrays.asList(1L);

        when(sysUserDomainRepository.load(new UserId(1L))).thenReturn(Mono.just(mockDomain));
        when(sysUserDomainRepository.save(any(SysUserDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysUserByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysUserDomainRepository).save(argThat(domain -> !domain.getDeleteStatus()));
    }

    @Test
    void restoreByIds_shouldRestoreMultipleUsers() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        SysUserDomain domain1 = createMockDomain(1L);
        SysUserDomain domain2 = createMockDomain(2L);

        when(sysUserDomainRepository.load(new UserId(1L))).thenReturn(Mono.just(domain1));
        when(sysUserDomainRepository.load(new UserId(2L))).thenReturn(Mono.just(domain2));
        when(sysUserDomainRepository.load(new UserId(3L))).thenReturn(Mono.empty());
        when(sysUserDomainRepository.save(any(SysUserDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysUserByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysUserDomainRepository).load(new UserId(1L));
        verify(sysUserDomainRepository).load(new UserId(2L));
        verify(sysUserDomainRepository).load(new UserId(3L));
        verify(sysUserDomainRepository, times(2)).save(any(SysUserDomain.class));
    }

    private SysUserDomain createMockDomain(Long userId) {
        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(new UserId(userId));
        Account account = new Account();
        account.setUsername(new Username("user" + userId));
        account.setPassword(new Password("password"));
        account.setLockStatus(false);
        domain.setAccount(account);
        domain.setDeptId(1L);
        domain.setDeleteStatus(true);
        return domain;
    }
}
