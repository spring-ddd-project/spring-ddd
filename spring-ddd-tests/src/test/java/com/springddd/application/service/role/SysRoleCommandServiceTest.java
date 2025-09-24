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
        command.setId(0L);
        command.setRoleName("Test Role");
        command.setRoleCode("TEST_ROLE");
        command.setDataScope(1);
        command.setOwnerStatus(true);
        command.setRoleDesc("Test Description");
        command.setRoleStatus(true);
        command.setDataPermission(new DataPermission());
        command.setDeptId(1L);

        SysRoleDomain mockDomain = new SysRoleDomain();
        when(sysRoleDomainFactory.newInstance(any(), any(), any(), any(), any())).thenReturn(mockDomain);
        when(sysRoleDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Long> result = sysRoleCommandService.createRole(command);

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void updateRole_shouldComplete_whenValidCommand() {
        SysRoleCommand command = new SysRoleCommand();
        command.setId(1L);
        command.setRoleName("Updated Role");
        command.setRoleCode("UPDATED_ROLE");
        command.setDataScope(1);
        command.setOwnerStatus(true);
        command.setRoleDesc("Updated Description");
        command.setRoleStatus(true);
        command.setDataPermission(new DataPermission());
        command.setDeptId(1L);

        SysRoleDomain mockDomain = new SysRoleDomain();
        when(sysRoleDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(sysRoleDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Void> result = sysRoleCommandService.updateRole(command);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void deleteRole_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteSysRoleByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysRoleCommandService.deleteRole(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void restore_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreSysRoleByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysRoleCommandService.restore(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeSysRoleByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysRoleCommandService.wipe(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
