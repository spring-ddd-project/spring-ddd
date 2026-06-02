package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserCommand;
import com.springddd.domain.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysUserCommandServiceTest {

    @Mock
    private SysUserDomainRepository sysUserDomainRepository;

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

    private SysUserCommandService sysUserCommandService;

    @BeforeEach
    void setUp() {
        sysUserCommandService = new SysUserCommandService(
                sysUserDomainRepository,
                sysUserDomainFactory,
                wipeSysUserByIdsDomainService,
                passwordEncoder,
                deleteSysUserByIdDomainService,
                restoreSysUserByIdDomainService
        );
    }

    @Test
    void createUser_shouldReturnId_whenValidCommand() {
        SysUserCommand command = new SysUserCommand();
        command.setUsername("testuser");
        command.setPassword("password");
        command.setPhone("13800138000");
        command.setEmail("test@example.com");
        command.setLockStatus(false);
        command.setAvatar("avatar.png");
        command.setSex(true);
        command.setDeptId(1L);

        SysUserDomain mockDomain = new SysUserDomain();
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(sysUserDomainFactory.newInstance(any(), any(), any())).thenReturn(mockDomain);
        when(sysUserDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(sysUserCommandService.createUser(command))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void updateUser_shouldComplete_whenValidCommand() {
        SysUserCommand command = new SysUserCommand();
        command.setId(1L);
        command.setUsername("updateduser");
        command.setPhone("13800138001");
        command.setEmail("updated@example.com");
        command.setLockStatus(false);
        command.setAvatar("newavatar.png");
        command.setSex(false);
        command.setDeptId(2L);

        SysUserDomain mockDomain = new SysUserDomain();
        Account account = new Account();
        account.setPassword(new Password("encodedPassword"));
        mockDomain.setAccount(account);

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(sysUserDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(sysUserDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(sysUserCommandService.updateUser(command))
                .verifyComplete();
    }

    @Test
    void delete_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteSysUserByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysUserCommandService.delete(ids))
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeSysUserByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysUserCommandService.wipe(ids))
                .verifyComplete();
    }

    @Test
    void restore_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreSysUserByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysUserCommandService.restore(ids))
                .verifyComplete();
    }
}
