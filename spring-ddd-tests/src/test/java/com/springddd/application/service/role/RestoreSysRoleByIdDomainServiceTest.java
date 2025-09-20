package com.springddd.application.service.role;

import com.springddd.domain.role.*;
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
class RestoreSysRoleByIdDomainServiceTest {

    @Mock
    private SysRoleDomainRepository sysRoleDomainRepository;

    @InjectMocks
    private RestoreSysRoleByIdDomainServiceImpl restoreSysRoleByIdDomainService;

    private SysRoleDomain mockDomain;

    @BeforeEach
    void setUp() {
        mockDomain = new SysRoleDomain();
        mockDomain.setRoleId(new RoleId(1L));
        mockDomain.setRoleBasicInfo(new RoleBasicInfo("Admin", "ADMIN", 1, true));
        mockDomain.setRoleExtendInfo(new RoleExtendInfo("desc", true));
        mockDomain.setDeleteStatus(true);
    }

    @Test
    void restoreByIds_shouldRestoreDeletedRoles() {
        List<Long> ids = Arrays.asList(1L);

        when(sysRoleDomainRepository.load(any(RoleId.class))).thenReturn(Mono.just(mockDomain));
        when(sysRoleDomainRepository.save(any(SysRoleDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysRoleByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysRoleDomainRepository).load(any(RoleId.class));
        verify(sysRoleDomainRepository).save(any(SysRoleDomain.class));
    }

    @Test
    void restoreByIds_shouldHandleMultipleIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        SysRoleDomain domain1 = new SysRoleDomain();
        domain1.setRoleId(new RoleId(1L));
        SysRoleDomain domain2 = new SysRoleDomain();
        domain2.setRoleId(new RoleId(2L));
        SysRoleDomain domain3 = new SysRoleDomain();
        domain3.setRoleId(new RoleId(3L));

        when(sysRoleDomainRepository.load(new RoleId(1L))).thenReturn(Mono.just(domain1));
        when(sysRoleDomainRepository.load(new RoleId(2L))).thenReturn(Mono.just(domain2));
        when(sysRoleDomainRepository.load(new RoleId(3L))).thenReturn(Mono.just(domain3));
        when(sysRoleDomainRepository.save(any(SysRoleDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysRoleByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysRoleDomainRepository, times(3)).load(any(RoleId.class));
        verify(sysRoleDomainRepository, times(3)).save(any(SysRoleDomain.class));
    }

    @Test
    void restoreByIds_shouldHandleEmptyIdsList() {
        List<Long> ids = Collections.emptyList();

        StepVerifier.create(restoreSysRoleByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysRoleDomainRepository, never()).load(any());
        verify(sysRoleDomainRepository, never()).save(any());
    }

    @Test
    void restoreByIds_shouldHandleRoleNotFound() {
        List<Long> ids = Arrays.asList(999L);

        when(sysRoleDomainRepository.load(any(RoleId.class))).thenReturn(Mono.empty());

        StepVerifier.create(restoreSysRoleByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysRoleDomainRepository).load(any(RoleId.class));
        verify(sysRoleDomainRepository, never()).save(any());
    }

    @Test
    void restoreByIds_shouldSetDeleteStatusFalse() {
        List<Long> ids = Arrays.asList(1L);
        mockDomain.setDeleteStatus(true);

        when(sysRoleDomainRepository.load(any(RoleId.class))).thenReturn(Mono.just(mockDomain));
        when(sysRoleDomainRepository.save(any(SysRoleDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysRoleByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysRoleDomainRepository).save(argThat(domain -> !domain.getDeleteStatus()));
    }
}
