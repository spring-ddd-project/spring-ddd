package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleCommand;
import com.springddd.domain.role.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysRoleCommandServiceTest {

    @Mock
    private SysRoleDomainRepository sysRoleDomainRepository;

    @Mock
    private SysRoleDomainFactory sysRoleDomainFactory;

    @Mock
    private WipeSysRoleByIdsDomainService wipeSysRoleByIdsDomainService;

    @Mock
    private DeleteSysRoleByIdDomainService deleteSysRoleByIdDomainService;

    @Mock
    private RestoreSysRoleByIdDomainService restoreSysRoleByIdDomainService;

    private SysRoleCommandService sysRoleCommandService;

    @BeforeEach
    void setUp() {
        sysRoleCommandService = new SysRoleCommandService(
                sysRoleDomainRepository,
                sysRoleDomainFactory,
                wipeSysRoleByIdsDomainService,
                deleteSysRoleByIdDomainService,
                restoreSysRoleByIdDomainService
        );
    }

    @Test
    void createRole_shouldReturnId_whenValidCommand() {
        SysRoleCommand command = new SysRoleCommand();
        command.setId(1L);
        command.setRoleName("Admin");
        command.setRoleCode("admin");
        command.setOwnerStatus(true);
        command.setRoleDesc("Administrator");
        command.setRoleStatus(true);

        SysRoleDomain mockDomain = new SysRoleDomain();
        when(sysRoleDomainFactory.newInstance(any(), any(), any(), any())).thenReturn(mockDomain);
        when(sysRoleDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(sysRoleCommandService.createRole(command))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void updateRole_shouldComplete_whenValidCommand() {
        SysRoleCommand command = new SysRoleCommand();
        command.setId(1L);
        command.setRoleName("UpdatedAdmin");
        command.setRoleCode("updated_admin");
        command.setOwnerStatus(false);
        command.setRoleDesc("Updated");
        command.setRoleStatus(false);

        SysRoleDomain mockDomain = new SysRoleDomain();
        when(sysRoleDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(sysRoleDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(sysRoleCommandService.updateRole(command))
                .verifyComplete();
    }

    @Test
    void deleteRole_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteSysRoleByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysRoleCommandService.deleteRole(ids))
                .verifyComplete();
    }

    @Test
    void restore_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreSysRoleByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysRoleCommandService.restore(ids))
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeSysRoleByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysRoleCommandService.wipe(ids))
                .verifyComplete();
    }
}
