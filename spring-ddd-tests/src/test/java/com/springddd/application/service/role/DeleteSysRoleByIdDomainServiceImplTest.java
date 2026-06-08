package com.springddd.application.service.role;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.role.DeleteSysRoleByIdDomainService;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.SysRoleDomain;
import com.springddd.domain.role.SysRoleDomainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteSysRoleByIdDomainServiceImplTest {

    @Mock
    private SysRoleDomainRepository sysRoleDomainRepository;

    private DeleteSysRoleByIdDomainService deleteSysRoleByIdDomainService;

    @BeforeEach
    void setUp() {
        deleteSysRoleByIdDomainService = new DeleteSysRoleByIdDomainServiceImpl(
                sysRoleDomainRepository
        );
    }

    @Test
    void deleteByIds_shouldDeleteSingleRole() {
        Long roleId = 1L;
        List<Long> ids = Arrays.asList(roleId);

        SysRoleDomain domain = new SysRoleDomain();
        domain.setDeleteStatus(false);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::concurrency).thenReturn(1);

            when(sysRoleDomainRepository.load(new RoleId(roleId))).thenReturn(Mono.just(domain));
            when(sysRoleDomainRepository.save(any(SysRoleDomain.class))).thenReturn(Mono.just(1L));

            Mono<Void> result = deleteSysRoleByIdDomainService.deleteByIds(ids);

            StepVerifier.create(result)
                    .verifyComplete();

            verify(sysRoleDomainRepository).load(new RoleId(roleId));
            verify(sysRoleDomainRepository).save(any(SysRoleDomain.class));
        }
    }

    @Test
    void deleteByIds_shouldDeleteMultipleRoles() {
        Long roleId1 = 1L;
        Long roleId2 = 2L;
        List<Long> ids = Arrays.asList(roleId1, roleId2);

        SysRoleDomain domain1 = new SysRoleDomain();
        domain1.setDeleteStatus(false);
        SysRoleDomain domain2 = new SysRoleDomain();
        domain2.setDeleteStatus(false);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::concurrency).thenReturn(1);

            when(sysRoleDomainRepository.load(new RoleId(roleId1))).thenReturn(Mono.just(domain1));
            when(sysRoleDomainRepository.load(new RoleId(roleId2))).thenReturn(Mono.just(domain2));
            when(sysRoleDomainRepository.save(any(SysRoleDomain.class))).thenReturn(Mono.just(1L));

            Mono<Void> result = deleteSysRoleByIdDomainService.deleteByIds(ids);

            StepVerifier.create(result)
                    .verifyComplete();

            verify(sysRoleDomainRepository, times(2)).load(any(RoleId.class));
            verify(sysRoleDomainRepository, times(2)).save(any(SysRoleDomain.class));
        }
    }

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        List<Long> ids = Arrays.asList();

        Mono<Void> result = deleteSysRoleByIdDomainService.deleteByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();

        verifyNoInteractions(sysRoleDomainRepository);
    }

    @Test
    void deleteByIds_shouldContinue_whenRoleNotFound() {
        Long roleId1 = 1L;
        Long roleId2 = 2L;
        List<Long> ids = Arrays.asList(roleId1, roleId2);

        SysRoleDomain domain1 = new SysRoleDomain();
        domain1.setDeleteStatus(false);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::concurrency).thenReturn(1);

            when(sysRoleDomainRepository.load(new RoleId(roleId1))).thenReturn(Mono.just(domain1));
            when(sysRoleDomainRepository.load(new RoleId(roleId2))).thenReturn(Mono.empty());
            when(sysRoleDomainRepository.save(any(SysRoleDomain.class))).thenReturn(Mono.just(1L));

            Mono<Void> result = deleteSysRoleByIdDomainService.deleteByIds(ids);

            StepVerifier.create(result)
                    .verifyComplete();

            verify(sysRoleDomainRepository).load(new RoleId(roleId1));
            verify(sysRoleDomainRepository).save(any(SysRoleDomain.class));
        }
    }

    @Test
    void deleteByIds_shouldMarkDomainAsDeleted() {
        Long roleId = 1L;
        List<Long> ids = Arrays.asList(roleId);

        SysRoleDomain domain = new SysRoleDomain();
        domain.setDeleteStatus(false);

        try (MockedStatic<SecurityUtils> mockedSecurityUtils = mockStatic(SecurityUtils.class)) {
            mockedSecurityUtils.when(SecurityUtils::concurrency).thenReturn(1);

            when(sysRoleDomainRepository.load(new RoleId(roleId))).thenReturn(Mono.just(domain));
            when(sysRoleDomainRepository.save(any(SysRoleDomain.class))).thenAnswer(invocation -> {
                SysRoleDomain savedDomain = invocation.getArgument(0);
                return Mono.just(1L);
            });

            Mono<Void> result = deleteSysRoleByIdDomainService.deleteByIds(ids);

            StepVerifier.create(result)
                    .verifyComplete();

            verify(sysRoleDomainRepository).save(argThat(d -> d.getDeleteStatus() == true));
        }
    }
}
