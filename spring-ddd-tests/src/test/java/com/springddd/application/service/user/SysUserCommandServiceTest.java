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
import static org.mockito.ArgumentMatchers.anyString;
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
        command.setPassword("password123");
        command.setPhone("1234567890");
        command.setEmail("test@example.com");
        command.setLockStatus(false);
        command.setAvatar("avatar.png");
        command.setSex(true);
        command.setDeptId(1L);

        SysUserDomain mockDomain = new SysUserDomain();
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(sysUserDomainFactory.newInstance(any(), any(), any())).thenReturn(mockDomain);
        when(sysUserDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Long> result = sysUserCommandService.createUser(command);

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void updateUser_shouldComplete_whenValidCommand() {
        SysUserCommand command = new SysUserCommand();
        command.setId(1L);
        command.setUsername("updateduser");
        command.setPassword("newpassword");
        command.setPhone("9876543210");
        command.setEmail("updated@example.com");
        command.setLockStatus(false);
        command.setAvatar("newavatar.png");
        command.setSex(false);
        command.setDeptId(1L);

        SysUserDomain mockDomain = new SysUserDomain();
        Account existingAccount = new Account();
        existingAccount.setPassword(new Password("existingEncodedPassword"));
        mockDomain.setAccount(existingAccount);

        when(sysUserDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(passwordEncoder.encode(anyString())).thenReturn("newEncodedPassword");
        when(sysUserDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Void> result = sysUserCommandService.updateUser(command);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeSysUserByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysUserCommandService.wipe(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void delete_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteSysUserByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysUserCommandService.delete(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void restore_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreSysUserByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysUserCommandService.restore(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
