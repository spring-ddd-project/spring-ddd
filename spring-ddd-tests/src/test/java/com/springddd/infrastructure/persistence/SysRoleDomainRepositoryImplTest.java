package com.springddd.infrastructure.persistence;

import com.springddd.domain.role.*;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
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
class SysRoleDomainRepositoryImplTest {

    @Mock
    private SysRoleRepository sysRoleRepository;

    @InjectMocks
    private SysRoleDomainRepositoryImpl repository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("管理员");
        entity.setRoleCode("admin");
        entity.setRoleDesc("系统管理员");
        entity.setRoleStatus(true);
        entity.setOwnerStatus(true);
        entity.setDeptId(1L);
        entity.setDeleteStatus(false);
        entity.setVersion(0);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());

        when(sysRoleRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.load(new RoleId(1L)))
                .assertNext(domain -> {
                    assertEquals(1L, domain.getRoleId().value());
                    assertEquals("管理员", domain.getRoleBasicInfo().roleName());
                    assertEquals("admin", domain.getRoleBasicInfo().roleCode());
                    assertTrue(domain.getRoleBasicInfo().roleOwner());
                    assertEquals("系统管理员", domain.getRoleExtendInfo().roleDesc());
                    assertTrue(domain.getRoleExtendInfo().roleStatus());
                    assertEquals(1L, domain.getDeptId());
                    assertFalse(domain.getDeleteStatus());
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(sysRoleRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.load(new RoleId(1L)))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(null);
        domain.setRoleBasicInfo(new RoleBasicInfo("管理员", "admin", true));
        domain.setRoleExtendInfo(new RoleExtendInfo("系统管理员", true));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        SysRoleEntity savedEntity = new SysRoleEntity();
        savedEntity.setId(1L);

        when(sysRoleRepository.save(any(SysRoleEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingExistingAggregate() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(new RoleId(1L));
        domain.setRoleBasicInfo(new RoleBasicInfo("管理员", "admin", true));
        domain.setRoleExtendInfo(new RoleExtendInfo("系统管理员", true));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        SysRoleEntity savedEntity = new SysRoleEntity();
        savedEntity.setId(1L);

        when(sysRoleRepository.save(any(SysRoleEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }
}
