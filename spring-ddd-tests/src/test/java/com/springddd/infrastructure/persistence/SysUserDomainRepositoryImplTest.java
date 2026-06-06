package com.springddd.infrastructure.persistence;

import com.springddd.domain.user.*;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysUserDomainRepositoryImplTest {

    @Mock
    private SysUserRepository sysUserRepository;

    @InjectMocks
    private SysUserDomainRepositoryImpl repository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        SysUserEntity entity = new SysUserEntity();
        entity.setId(1L);
        entity.setUsername("admin");
        entity.setPassword("password");
        entity.setPhone("1234567890");
        entity.setEmail("admin@example.com");
        entity.setLockStatus(false);
        entity.setAvatar("avatar.png");
        entity.setSex(true);
        entity.setDeptId(1L);
        entity.setDeleteStatus(false);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());
        entity.setVersion(0);

        when(sysUserRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.load(new UserId(1L)))
                .assertNext(domain -> {
                    assert domain.getUserId().value().equals(1L);
                    assert domain.getAccount().username().value().equals("admin");
                    assert domain.getAccount().password().value().equals("password");
                    assert domain.getDeptId().equals(1L);
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(sysUserRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.load(new UserId(1L)))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate() {
        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(null);
        Account account = new Account(
                new Username("admin"),
                new Password("password"),
                "admin@example.com",
                "1234567890",
                false
        );
        domain.setAccount(account);
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("avatar.png");
        extendInfo.setSex(true);
        domain.setExtendInfo(extendInfo);
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        SysUserEntity savedEntity = new SysUserEntity();
        savedEntity.setId(1L);

        when(sysUserRepository.save(any(SysUserEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingExistingAggregate() {
        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(new UserId(1L));
        Account account = new Account(
                new Username("admin"),
                new Password("password"),
                "admin@example.com",
                "1234567890",
                false
        );
        domain.setAccount(account);
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("avatar.png");
        extendInfo.setSex(true);
        domain.setExtendInfo(extendInfo);
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        SysUserEntity savedEntity = new SysUserEntity();
        savedEntity.setId(1L);

        when(sysUserRepository.save(any(SysUserEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldHandleNullUsername() {
        SysUserDomain domain = new SysUserDomain();
        Account account = new Account(
                null,
                new Password("password"),
                "admin@example.com",
                "1234567890",
                false
        );
        domain.setAccount(account);
        domain.setExtendInfo(new ExtendInfo());
        domain.setDeptId(1L);

        SysUserEntity savedEntity = new SysUserEntity();
        savedEntity.setId(1L);

        when(sysUserRepository.save(any(SysUserEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldHandleNullPassword() {
        SysUserDomain domain = new SysUserDomain();
        Account account = new Account(
                new Username("admin"),
                null,
                "admin@example.com",
                "1234567890",
                false
        );
        domain.setAccount(account);
        domain.setExtendInfo(new ExtendInfo());
        domain.setDeptId(1L);

        SysUserEntity savedEntity = new SysUserEntity();
        savedEntity.setId(1L);

        when(sysUserRepository.save(any(SysUserEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }
}
