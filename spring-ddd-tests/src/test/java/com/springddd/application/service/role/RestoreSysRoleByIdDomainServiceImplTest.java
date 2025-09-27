package com.springddd.application.service.role;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.role.RestoreSysRoleByIdDomainService;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.SysRoleDomain;
import com.springddd.domain.role.SysRoleDomainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestoreSysRoleByIdDomainServiceImplTest {

    @Mock
    private SysRoleDomainRepository sysRoleDomainRepository;

    private RestoreSysRoleByIdDomainServiceImpl restoreSysRoleByIdDomainService;

    @BeforeEach
    void setUp() {
        restoreSysRoleByIdDomainService = new RestoreSysRoleByIdDomainServiceImpl(sysRoleDomainRepository);
    }

    @Test
    void shouldRestoreByIdsSuccessfully() {
        Long roleId = 1L;
        SysRoleDomain domain = new SysRoleDomain();

        when(sysRoleDomainRepository.load(any(RoleId.class))).thenReturn(Mono.just(domain));
        when(sysRoleDomainRepository.save(any(SysRoleDomain.class))).thenReturn(Mono.just(roleId));
        when(SecurityUtils.concurrency()).thenReturn(reactor.util.concurrent.Queues.<SysRoleDomain>unbounded(16).toContextWrite());

        List<Long> ids = Arrays.asList(roleId);

        Mono<Void> result = restoreSysRoleByIdDomainService.restoreByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();

        verify(sysRoleDomainRepository).load(any(RoleId.class));
    }

    @Test
    void shouldRestoreMultipleIds() {
        Long roleId1 = 1L;
        Long roleId2 = 2L;
        SysRoleDomain domain1 = new SysRoleDomain();
        SysRoleDomain domain2 = new SysRoleDomain();

        when(sysRoleDomainRepository.load(new RoleId(roleId1))).thenReturn(Mono.just(domain1));
        when(sysRoleDomainRepository.load(new RoleId(roleId2))).thenReturn(Mono.just(domain2));
        when(sysRoleDomainRepository.save(any(SysRoleDomain.class))).thenReturn(Mono.just(1L));
        when(SecurityUtils.concurrency()).thenReturn(reactor.util.concurrent.Queues.<SysRoleDomain>unbounded(16).toContextWrite());

        List<Long> ids = Arrays.asList(roleId1, roleId2);

        Mono<Void> result = restoreSysRoleByIdDomainService.restoreByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();

        verify(sysRoleDomainRepository, times(2)).load(any(RoleId.class));
    }
}
