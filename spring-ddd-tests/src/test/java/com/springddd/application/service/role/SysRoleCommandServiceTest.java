package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleCommand;
import com.springddd.domain.role.*;
import com.springddd.infrastructure.persistence.factory.RepositoryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysRoleCommandServiceTest {

    @Mock
    private RepositoryFactory repositoryFactory;

    @Mock
    private SysRoleDomainFactory sysRoleDomainFactory;

    @Mock
    private WipeSysRoleByIdsDomainService wipeSysRoleByIdsDomainService;

    @Mock
    private DeleteSysRoleByIdDomainService deleteSysRoleByIdDomainService;

    @Mock
    private RestoreSysRoleByIdDomainService restoreSysRoleByIdDomainService;

    @Mock
    private SysRoleDomainRepository sysRoleDomainRepository;

    @InjectMocks
    private SysRoleCommandService service;

    @Test
    @DisplayName("createRole 应创建角色并返回 ID")
    void createRole_shouldCreateRoleAndReturnId() {
        SysRoleCommand command = new SysRoleCommand();
        command.setRoleName("Admin");
        command.setRoleCode("admin");
        command.setDataScope(1);
        command.setRoleStatus(true);
        command.setOwnerStatus(true);

        SysRoleDomain domain = new SysRoleDomain();
        when(sysRoleDomainFactory.newInstance(any(RoleId.class), any(RoleBasicInfo.class), any(RoleExtendInfo.class), any(), any())).thenReturn(domain);
        when(repositoryFactory.getSysRoleDomainRepository()).thenReturn(sysRoleDomainRepository);
        when(sysRoleDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.createRole(command))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();

        verify(sysRoleDomainFactory).newInstance(any(RoleId.class), any(RoleBasicInfo.class), any(RoleExtendInfo.class), any(), any());
        verify(sysRoleDomainRepository).save(domain);
    }

    @Test
    @DisplayName("updateRole 应更新角色")
    void updateRole_shouldUpdateRole() {
        SysRoleCommand command = new SysRoleCommand();
        command.setId(1L);
        command.setRoleName("Updated");
        command.setRoleCode("updated");
        command.setDataScope(1);
        command.setRoleStatus(true);
        command.setOwnerStatus(true);

        SysRoleDomain domain = new SysRoleDomain();
        when(repositoryFactory.getSysRoleDomainRepository()).thenReturn(sysRoleDomainRepository);
        when(sysRoleDomainRepository.load(new RoleId(1L))).thenReturn(Mono.just(domain));
        when(sysRoleDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.updateRole(command))
                .verifyComplete();

        verify(sysRoleDomainRepository).load(new RoleId(1L));
        verify(sysRoleDomainRepository).save(domain);
    }

    @Test
    @DisplayName("deleteRole 应调用 deleteByIds 领域服务")
    void deleteRole_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(deleteSysRoleByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteRole(ids))
                .verifyComplete();

        verify(deleteSysRoleByIdDomainService).deleteByIds(ids);
    }

    @Test
    @DisplayName("restore 应调用 restore 领域服务")
    void restore_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(restoreSysRoleByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.restore(ids))
                .verifyComplete();

        verify(restoreSysRoleByIdDomainService).restoreByIds(ids);
    }

    @Test
    @DisplayName("wipe 应调用 wipe 领域服务")
    void wipe_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(wipeSysRoleByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.wipe(ids))
                .verifyComplete();

        verify(wipeSysRoleByIdsDomainService).deleteByIds(ids);
    }
}
