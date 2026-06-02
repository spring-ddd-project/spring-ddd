package com.springddd.infrastructure.persistence;

import com.springddd.domain.role.RoleId;
import com.springddd.domain.user.*;
import com.springddd.infrastructure.persistence.entity.SysUserRoleEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRoleRepository;
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
class SysUserRoleDomainRepositoryImplTest {

    @Mock
    private SysUserRoleRepository sysUserRoleRepository;

    @InjectMocks
    private SysUserRoleDomainRepositoryImpl repository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        SysUserRoleEntity entity = new SysUserRoleEntity();
        entity.setId(1L);
        entity.setUserId(1L);
        entity.setRoleId(1L);
        entity.setDeptId(1L);
        entity.setDeleteStatus(false);
        entity.setVersion(0);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());

        when(sysUserRoleRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.load(new UserRoleId(1L)))
                .assertNext(domain -> {
                    assertEquals(1L, domain.getUserRoleId().value());
                    assertEquals(1L, domain.getUserId().value());
                    assertEquals(1L, domain.getRoleId().value());
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(sysUserRoleRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.load(new UserRoleId(1L)))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setUserRoleId(null);
        domain.setUserId(new UserId(1L));
        domain.setRoleId(new RoleId(1L));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        SysUserRoleEntity savedEntity = new SysUserRoleEntity();
        savedEntity.setId(1L);

        when(sysUserRoleRepository.save(any(SysUserRoleEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingExistingAggregate() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setUserRoleId(new UserRoleId(1L));
        domain.setUserId(new UserId(1L));
        domain.setRoleId(new RoleId(1L));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        SysUserRoleEntity savedEntity = new SysUserRoleEntity();
        savedEntity.setId(1L);

        when(sysUserRoleRepository.save(any(SysUserRoleEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }
}
