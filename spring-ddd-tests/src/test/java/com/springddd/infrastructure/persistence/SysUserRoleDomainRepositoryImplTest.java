package com.springddd.infrastructure.persistence;

import com.springddd.domain.role.RoleId;
import com.springddd.domain.user.SysUserRoleDomain;
import com.springddd.domain.user.UserId;
import com.springddd.domain.user.UserRoleId;
import com.springddd.infrastructure.persistence.entity.SysUserRoleEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRoleRepository;
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
@DisplayName("SysUserRoleDomainRepositoryImpl Tests")
class SysUserRoleDomainRepositoryImplTest {

    @Mock
    private SysUserRoleRepository sysUserRoleRepository;

    @InjectMocks
    private SysUserRoleDomainRepositoryImpl sysUserRoleDomainRepository;

    private SysUserRoleEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new SysUserRoleEntity();
        testEntity.setId(1L);
        testEntity.setUserId(100L);
        testEntity.setRoleId(200L);
        testEntity.setDeptId(1L);
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
        when(sysUserRoleRepository.findById(1L)).thenReturn(Mono.just(testEntity));

        StepVerifier.create(sysUserRoleDomainRepository.load(new UserRoleId(1L)))
                .assertNext(domain -> {
                    assertThat(domain.getUserRoleId().value()).isEqualTo(1L);
                    assertThat(domain.getUserId().value()).isEqualTo(100L);
                    assertThat(domain.getRoleId().value()).isEqualTo(200L);
                    assertThat(domain.getDeptId()).isEqualTo(1L);
                    assertThat(domain.getDeleteStatus()).isFalse();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("load() should return empty when entity not found")
    void load_WhenEntityNotFound_ReturnsEmpty() {
        when(sysUserRoleRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(sysUserRoleDomainRepository.load(new UserRoleId(999L)))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() should persist domain and return id")
    void save_WhenValidDomain_ReturnsId() {
        when(sysUserRoleRepository.save(any(SysUserRoleEntity.class))).thenReturn(Mono.just(testEntity));

        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setUserRoleId(new UserRoleId(1L));
        domain.setUserId(new UserId(100L));
        domain.setRoleId(new RoleId(200L));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        StepVerifier.create(sysUserRoleDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null id should create new entity")
    void save_WithNullId_CreatesNewEntity() {
        SysUserRoleEntity newEntity = new SysUserRoleEntity();
        newEntity.setId(2L);
        when(sysUserRoleRepository.save(any(SysUserRoleEntity.class))).thenReturn(Mono.just(newEntity));

        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setUserRoleId(null);
        domain.setUserId(new UserId(100L));
        domain.setRoleId(new RoleId(200L));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);

        StepVerifier.create(sysUserRoleDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(2L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() should persist domain with all audit fields")
    void save_WithAllAuditFields_PersistsSuccessfully() {
        SysUserRoleEntity savedEntity = new SysUserRoleEntity();
        savedEntity.setId(1L);
        when(sysUserRoleRepository.save(any(SysUserRoleEntity.class))).thenReturn(Mono.just(savedEntity));

        LocalDateTime now = LocalDateTime.now();

        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setUserRoleId(new UserRoleId(1L));
        domain.setUserId(new UserId(100L));
        domain.setRoleId(new RoleId(200L));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(now);
        domain.setUpdateBy("admin");
        domain.setUpdateTime(now);
        domain.setVersion(1);

        StepVerifier.create(sysUserRoleDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with existing id should update entity")
    void save_WithExistingId_UpdatesEntity() {
        SysUserRoleEntity updatedEntity = new SysUserRoleEntity();
        updatedEntity.setId(1L);
        when(sysUserRoleRepository.save(any(SysUserRoleEntity.class))).thenReturn(Mono.just(updatedEntity));

        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setUserRoleId(new UserRoleId(1L));
        domain.setUserId(new UserId(100L));
        domain.setRoleId(new RoleId(300L));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);

        StepVerifier.create(sysUserRoleDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }
}
