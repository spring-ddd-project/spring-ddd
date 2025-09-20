package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserCommand;
import com.springddd.domain.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @InjectMocks
    private SysUserCommandService sysUserCommandService;

    private SysUserCommand createCommand;
    private SysUserDomain mockDomain;

    @BeforeEach
    void setUp() {
        createCommand = new SysUserCommand();
        createCommand.setUsername("testuser");
        createCommand.setPassword("password123");
        createCommand.setPhone("13800138000");
        createCommand.setEmail("test@example.com");
        createCommand.setLockStatus(false);
        createCommand.setAvatar("avatar.png");
        createCommand.setSex(true);
        createCommand.setDeptId(1L);

        mockDomain = new SysUserDomain();
        mockDomain.setUserId(new UserId(1L));
        Account account = new Account();
        account.setUsername(new Username("testuser"));
        account.setPassword(new Password("encoded_password"));
        account.setPhone("13800138000");
        account.setEmail("test@example.com");
        account.setLockStatus(false);
        mockDomain.setAccount(account);

        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("avatar.png");
        extendInfo.setSex(true);
        mockDomain.setExtendInfo(extendInfo);
        mockDomain.setDeptId(1L);
        mockDomain.setDeleteStatus(false);
    }

    @Test
    void createUser_shouldCreateAndSaveUser() {
        when(passwordEncoder.encode(any())).thenReturn("encoded_password");
        when(sysUserDomainFactory.newInstance(any(Account.class), any(ExtendInfo.class), anyLong()))
                .thenReturn(mockDomain);
        when(sysUserDomainRepository.save(any(SysUserDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(sysUserCommandService.createUser(createCommand))
                .expectNext(1L)
                .verifyComplete();

        verify(passwordEncoder).encode("password123");
        verify(sysUserDomainFactory).newInstance(any(Account.class), any(ExtendInfo.class), eq(1L));
        verify(sysUserDomainRepository).save(any(SysUserDomain.class));
    }

    @Test
    void createUser_shouldEncodePassword() {
        when(passwordEncoder.encode("password123")).thenReturn("hashed_password");
        when(sysUserDomainFactory.newInstance(any(Account.class), any(ExtendInfo.class), anyLong()))
                .thenReturn(mockDomain);
        when(sysUserDomainRepository.save(any(SysUserDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(sysUserCommandService.createUser(createCommand))
                .expectNext(1L)
                .verifyComplete();

        verify(passwordEncoder).encode("password123");
    }

    @Test
    void updateUser_shouldUpdateExistingUser() {
        SysUserCommand updateCommand = new SysUserCommand();
        updateCommand.setId(1L);
        updateCommand.setUsername("updateduser");
        updateCommand.setPhone("13900139000");
        updateCommand.setEmail("updated@example.com");
        updateCommand.setLockStatus(true);
        updateCommand.setAvatar("new_avatar.png");
        updateCommand.setSex(false);
        updateCommand.setDeptId(2L);

        Account existingAccount = new Account();
        existingAccount.setUsername(new Username("olduser"));
        existingAccount.setPassword(new Password("old_encoded_password"));
        existingAccount.setLockStatus(false);
        mockDomain.setAccount(existingAccount);

        when(sysUserDomainRepository.load(new UserId(1L))).thenReturn(Mono.just(mockDomain));
        when(passwordEncoder.encode(any())).thenReturn("encoded");
        when(sysUserDomainRepository.save(any(SysUserDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(sysUserCommandService.updateUser(updateCommand))
                .verifyComplete();

        verify(sysUserDomainRepository).load(new UserId(1L));
        verify(sysUserDomainRepository).save(any(SysUserDomain.class));
    }

    @Test
    void updateUser_shouldCompleteWhenUserNotFound() {
        SysUserCommand updateCommand = new SysUserCommand();
        updateCommand.setId(999L);
        updateCommand.setUsername("updateduser");

        when(sysUserDomainRepository.load(new UserId(999L))).thenReturn(Mono.empty());

        StepVerifier.create(sysUserCommandService.updateUser(updateCommand))
                .verifyComplete();

        verify(sysUserDomainRepository, never()).save(any());
    }

    @Test
    void wipe_shouldCallWipeDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(wipeSysUserByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysUserCommandService.wipe(ids))
                .verifyComplete();

        verify(wipeSysUserByIdsDomainService).deleteByIds(ids);
    }

    @Test
    void delete_shouldCallDeleteDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteSysUserByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysUserCommandService.delete(ids))
                .verifyComplete();

        verify(deleteSysUserByIdDomainService).deleteByIds(ids);
    }

    @Test
    void restore_shouldCallRestoreDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreSysUserByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysUserCommandService.restore(ids))
                .verifyComplete();

        verify(restoreSysUserByIdDomainService).restoreByIds(ids);
    }
}
