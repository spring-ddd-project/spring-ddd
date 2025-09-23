package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleCommand;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @InjectMocks
    private SysRoleCommandService sysRoleCommandService;

    private SysRoleCommand createCommand;
    private SysRoleDomain mockDomain;

    @BeforeEach
    void setUp() {
        createCommand = new SysRoleCommand();
        createCommand.setId(1L);
        createCommand.setRoleName("Admin");
        createCommand.setRoleCode("ADMIN");
        createCommand.setDataScope(1);
        createCommand.setOwnerStatus(true);
        createCommand.setRoleDesc("Administrator role");
        createCommand.setRoleStatus(true);
        createCommand.setDeptId(100L);

        mockDomain = new SysRoleDomain();
        mockDomain.setRoleId(new RoleId(1L));
        mockDomain.setRoleBasicInfo(new RoleBasicInfo("Admin", "ADMIN", 1, true));
        mockDomain.setRoleExtendInfo(new RoleExtendInfo("Administrator role", true));
        mockDomain.setDeleteStatus(false);
    }

    @Test
    void createRole_shouldCreateRoleAndReturnId() {
        when(sysRoleDomainFactory.newInstance(any(RoleId.class), any(RoleBasicInfo.class), any(RoleExtendInfo.class), any(), anyLong()))
                .thenReturn(mockDomain);
        when(sysRoleDomainRepository.save(any(SysRoleDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(sysRoleCommandService.createRole(createCommand))
                .expectNext(1L)
                .verifyComplete();

        verify(sysRoleDomainFactory).newInstance(any(RoleId.class), any(RoleBasicInfo.class), any(RoleExtendInfo.class), any(), anyLong());
        verify(sysRoleDomainRepository).save(any(SysRoleDomain.class));
    }

    @Test
    void createRole_shouldReturnIdOnSuccess() {
        when(sysRoleDomainFactory.newInstance(any(RoleId.class), any(RoleBasicInfo.class), any(RoleExtendInfo.class), any(), anyLong()))
                .thenReturn(mockDomain);
        when(sysRoleDomainRepository.save(any(SysRoleDomain.class))).thenReturn(Mono.just(100L));

        StepVerifier.create(sysRoleCommandService.createRole(createCommand))
                .expectNext(100L)
                .verifyComplete();
    }

    @Test
    void updateRole_shouldUpdateRoleWhenDomainExists() {
        SysRoleCommand updateCommand = new SysRoleCommand();
        updateCommand.setId(1L);
        updateCommand.setRoleName("Updated Admin");
        updateCommand.setRoleCode("ADMIN_UPDATED");
        updateCommand.setDataScope(2);
        updateCommand.setOwnerStatus(false);
        updateCommand.setRoleDesc("Updated description");
        updateCommand.setRoleStatus(false);
        updateCommand.setDeptId(100L);

        when(sysRoleDomainRepository.load(any(RoleId.class))).thenReturn(Mono.just(mockDomain));
        when(sysRoleDomainRepository.save(any(SysRoleDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(sysRoleCommandService.updateRole(updateCommand))
                .verifyComplete();

        verify(sysRoleDomainRepository).load(any(RoleId.class));
        verify(sysRoleDomainRepository).save(any(SysRoleDomain.class));
    }

    @Test
    void updateRole_shouldCompleteWhenDomainNotFound() {
        SysRoleCommand updateCommand = new SysRoleCommand();
        updateCommand.setId(999L);
        updateCommand.setRoleName("Updated Admin");

        when(sysRoleDomainRepository.load(any(RoleId.class))).thenReturn(Mono.empty());

        StepVerifier.create(sysRoleCommandService.updateRole(updateCommand))
                .verifyComplete();

        verify(sysRoleDomainRepository, never()).save(any());
    }

    @Test
    void deleteRole_shouldCallDeleteDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(deleteSysRoleByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysRoleCommandService.deleteRole(ids))
                .verifyComplete();

        verify(deleteSysRoleByIdDomainService).deleteByIds(ids);
    }

    @Test
    void wipe_shouldCallWipeDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeSysRoleByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysRoleCommandService.wipe(ids))
                .verifyComplete();

        verify(wipeSysRoleByIdsDomainService).deleteByIds(ids);
    }

    @Test
    void restore_shouldCallRestoreDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreSysRoleByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysRoleCommandService.restore(ids))
                .verifyComplete();

        verify(restoreSysRoleByIdDomainService).restoreByIds(ids);
    }

    @Test
    void createRole_shouldCreateWithAllFields() {
        SysRoleCommand fullCommand = new SysRoleCommand();
        fullCommand.setId(1L);
        fullCommand.setRoleName("Full Role");
        fullCommand.setRoleCode("FULL");
        fullCommand.setDataScope(1);
        fullCommand.setOwnerStatus(true);
        fullCommand.setRoleDesc("Full description");
        fullCommand.setRoleStatus(true);
        fullCommand.setDeptId(200L);
        fullCommand.setDataPermission(new DataPermission());

        when(sysRoleDomainFactory.newInstance(any(RoleId.class), any(RoleBasicInfo.class), any(RoleExtendInfo.class), any(DataPermission.class), anyLong()))
                .thenReturn(mockDomain);
        when(sysRoleDomainRepository.save(any(SysRoleDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(sysRoleCommandService.createRole(fullCommand))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void updateRole_shouldUpdateBasicInfo() {
        SysRoleCommand updateCommand = new SysRoleCommand();
        updateCommand.setId(1L);
        updateCommand.setRoleName("New Name");
        updateCommand.setRoleCode("NEW_CODE");
        updateCommand.setDataScope(1);
        updateCommand.setOwnerStatus(true);
        updateCommand.setRoleStatus(true);

        when(sysRoleDomainRepository.load(any(RoleId.class))).thenReturn(Mono.just(mockDomain));
        when(sysRoleDomainRepository.save(any(SysRoleDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(sysRoleCommandService.updateRole(updateCommand))
                .verifyComplete();

        verify(sysRoleDomainRepository).save(any(SysRoleDomain.class));
    }
}
