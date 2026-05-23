package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserCommand;
import com.springddd.domain.user.*;
import com.springddd.infrastructure.persistence.factory.RepositoryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysUserCommandServiceTest {

    @Mock
    private RepositoryFactory repositoryFactory;

    @Mock
    private SysUserDomainFactory sysUserDomainFactory;

    @Mock
    private WipeSysUserByIdsDomainService wipeSysUserByIdsDomainService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private DeleteSysUserByIdDomainService deleteSysUserByIdDomainService;

    @Mock
    private RestoreSysUserByIdDomainService restoreSysUserByIdDomainService;

    @Mock
    private SysUserDomainRepository sysUserDomainRepository;

    @InjectMocks
    private SysUserCommandService service;

    @Test
    @DisplayName("createUser 应创建用户并返回 ID")
    void createUser_shouldCreateUserAndReturnId() {
        SysUserCommand command = new SysUserCommand();
        command.setUsername("test");
        command.setPassword("123456");
        command.setEmail("test@example.com");
        command.setPhone("13800138000");
        command.setLockStatus(false);
        command.setDeptId(1L);

        SysUserDomain domain = new SysUserDomain();
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(sysUserDomainFactory.newInstance(any(Account.class), any(ExtendInfo.class), anyLong())).thenReturn(domain);
        when(repositoryFactory.getSysUserDomainRepository()).thenReturn(sysUserDomainRepository);
        when(sysUserDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.createUser(command))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();

        verify(sysUserDomainFactory).newInstance(any(Account.class), any(ExtendInfo.class), eq(1L));
        verify(sysUserDomainRepository).save(domain);
    }

    @Test
    @DisplayName("updateUser 应更新用户")
    void updateUser_shouldUpdateUser() {
        SysUserCommand command = new SysUserCommand();
        command.setId(1L);
        command.setUsername("updated");
        command.setDeptId(1L);

        SysUserDomain domain = new SysUserDomain();
        Account account = new Account(new Username("old"), new Password("oldpass"), null, null, false);
        domain.setAccount(account);

        when(repositoryFactory.getSysUserDomainRepository()).thenReturn(sysUserDomainRepository);
        when(sysUserDomainRepository.load(new UserId(1L))).thenReturn(Mono.just(domain));
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(sysUserDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.updateUser(command))
                .verifyComplete();

        verify(sysUserDomainRepository).load(new UserId(1L));
        verify(sysUserDomainRepository).save(domain);
    }

    @Test
    @DisplayName("wipe 应调用 wipe 领域服务")
    void wipe_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(wipeSysUserByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.wipe(ids))
                .verifyComplete();

        verify(wipeSysUserByIdsDomainService).deleteByIds(ids);
    }

    @Test
    @DisplayName("delete 应调用 delete 领域服务")
    void delete_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(deleteSysUserByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.delete(ids))
                .verifyComplete();

        verify(deleteSysUserByIdDomainService).deleteByIds(ids);
    }

    @Test
    @DisplayName("restore 应调用 restore 领域服务")
    void restore_shouldCallDomainService() {
        List<Long> ids = List.of(1L, 2L);
        when(restoreSysUserByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(service.restore(ids))
                .verifyComplete();

        verify(restoreSysUserByIdDomainService).restoreByIds(ids);
    }
}
