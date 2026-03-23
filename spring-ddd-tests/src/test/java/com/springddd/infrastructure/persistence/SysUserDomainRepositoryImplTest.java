package com.springddd.infrastructure.persistence;

import com.springddd.domain.user.*;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SysUserDomainRepositoryImpl Tests")
class SysUserDomainRepositoryImplTest {

    @Mock
    private SysUserRepository sysUserRepository;

    @InjectMocks
    private SysUserDomainRepositoryImpl sysUserDomainRepository;

    private SysUserEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new SysUserEntity();
        testEntity.setId(1L);
        testEntity.setUsername("testuser");
        testEntity.setPassword("encoded_password");
        testEntity.setPhone("13800138000");
        testEntity.setEmail("test@example.com");
        testEntity.setAvatar("avatar_url");
        testEntity.setSex(true);
        testEntity.setLockStatus(false);
        testEntity.setDeptId(100L);
        testEntity.setDeleteStatus(false);
        testEntity.setCreateBy("admin");
        testEntity.setCreateTime(LocalDateTime.now());
        testEntity.setUpdateBy("admin");
        testEntity.setUpdateTime(LocalDateTime.now());
        testEntity.setVersion(1);
    }

    @Test
    @DisplayName("load() should return domain when entity exists")
    void load_WhenEntityExists_ReturnsDomain() {
        when(sysUserRepository.findById(1L)).thenReturn(Mono.just(testEntity));

        StepVerifier.create(sysUserDomainRepository.load(new UserId(1L)))
                .assertNext(domain -> {
                    assertThat(domain.getUserId().value()).isEqualTo(1L);
                    assertThat(domain.getAccount().getUsername().value()).isEqualTo("testuser");
                    assertThat(domain.getAccount().getPassword().value()).isEqualTo("encoded_password");
                    assertThat(domain.getAccount().getPhone()).isEqualTo("13800138000");
                    assertThat(domain.getAccount().getEmail()).isEqualTo("test@example.com");
                    assertThat(domain.getAccount().getLockStatus()).isFalse();
                    assertThat(domain.getExtendInfo().getAvatar()).isEqualTo("avatar_url");
                    assertThat(domain.getExtendInfo().getSex()).isTrue();
                    assertThat(domain.getDeptId()).isEqualTo(100L);
                    assertThat(domain.getDeleteStatus()).isFalse();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("load() should return empty when entity not found")
    void load_WhenEntityNotFound_ReturnsEmpty() {
        when(sysUserRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(sysUserDomainRepository.load(new UserId(999L)))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() should persist domain and return id")
    void save_WhenValidDomain_ReturnsId() {
        when(sysUserRepository.save(any(SysUserEntity.class))).thenReturn(Mono.just(testEntity));

        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(new UserId(1L));
        Account account = new Account();
        account.setUsername(new Username("testuser"));
        account.setPassword(new Password("encoded_password"));
        account.setPhone("13800138000");
        account.setEmail("test@example.com");
        account.setLockStatus(false);
        domain.setAccount(account);
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("avatar_url");
        extendInfo.setSex(true);
        domain.setExtendInfo(extendInfo);
        domain.setDeptId(100L);
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        StepVerifier.create(sysUserDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null id should create new entity")
    void save_WithNullId_CreatesNewEntity() {
        SysUserEntity newEntity = new SysUserEntity();
        newEntity.setId(2L);
        when(sysUserRepository.save(any(SysUserEntity.class))).thenReturn(Mono.just(newEntity));

        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(null);
        Account account = new Account();
        account.setUsername(new Username("newuser"));
        account.setPassword(new Password("new_password"));
        account.setPhone("13900139000");
        account.setEmail("new@example.com");
        account.setLockStatus(false);
        domain.setAccount(account);
        ExtendInfo extendInfo = new ExtendInfo();
        extendInfo.setAvatar("new_avatar");
        extendInfo.setSex(false);
        domain.setExtendInfo(extendInfo);
        domain.setDeptId(100L);

        StepVerifier.create(sysUserDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(2L))
                .verifyComplete();
    }
}
