package com.springddd.infrastructure.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysRoleDomainRepositoryImplTest {

    @Mock
    private SysRoleRepository sysRoleRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private SysRoleDomainRepositoryImpl repository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() throws JsonProcessingException {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("管理员");
        entity.setRoleCode("admin");
        entity.setRoleDesc("系统管理员");
        entity.setDataScope(1);
        entity.setDataPermission("{\"rowScope\":{\"deptIds\":[1],\"self\":false}}");
        entity.setRoleStatus(true);
        entity.setOwnerStatus(true);
        entity.setDeptId(1L);
        entity.setDeleteStatus(false);
        entity.setVersion(0);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());

        DataPermission dataPermission = new DataPermission();
        dataPermission.setRowScope(new RowScope(Collections.singletonList(1L), null, null, false));

        when(sysRoleRepository.findById(1L)).thenReturn(Mono.just(entity));
        when(objectMapper.readValue(eq("{\"rowScope\":{\"deptIds\":[1],\"self\":false}}"), eq(DataPermission.class)))
                .thenReturn(dataPermission);

        StepVerifier.create(repository.load(new RoleId(1L)))
                .assertNext(domain -> {
                    assertEquals(1L, domain.getRoleId().value());
                    assertEquals("管理员", domain.getRoleBasicInfo().roleName());
                    assertNotNull(domain.getDataPermission());
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnDomain_withoutDataPermission_whenEntityDataPermissionIsNull() {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("管理员");
        entity.setRoleCode("admin");
        entity.setRoleDesc("系统管理员");
        entity.setDataScope(1);
        entity.setDataPermission(null);
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
                    assertNull(domain.getDataPermission());
                })
                .verifyComplete();
    }

    @Test
    void load_shouldThrowRuntimeException_whenJsonProcessingFails() throws JsonProcessingException {
        SysRoleEntity entity = new SysRoleEntity();
        entity.setId(1L);
        entity.setRoleName("管理员");
        entity.setRoleCode("admin");
        entity.setRoleDesc("系统管理员");
        entity.setDataScope(1);
        entity.setDataPermission("invalid-json");
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
        when(objectMapper.readValue(eq("invalid-json"), eq(DataPermission.class)))
                .thenThrow(new JsonProcessingException("parse error") {});

        StepVerifier.create(repository.load(new RoleId(1L)))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(sysRoleRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.load(new RoleId(1L)))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate() throws JsonProcessingException {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(null);
        domain.setRoleBasicInfo(new RoleBasicInfo("管理员", "admin", 1, true));
        domain.setRoleExtendInfo(new RoleExtendInfo("系统管理员", true));
        domain.setDeptId(1L);
        DataPermission dataPermission = new DataPermission();
        dataPermission.setRowScope(new RowScope(Collections.singletonList(1L), null, null, false));
        domain.setDataPermission(dataPermission);
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        when(objectMapper.writeValueAsString(any(DataPermission.class)))
                .thenReturn("{\"rowScope\":{\"deptIds\":[1],\"self\":false}}");

        SysRoleEntity savedEntity = new SysRoleEntity();
        savedEntity.setId(1L);

        when(sysRoleRepository.save(any(SysRoleEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingExistingAggregate() throws JsonProcessingException {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(new RoleId(1L));
        domain.setRoleBasicInfo(new RoleBasicInfo("管理员", "admin", 1, true));
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

    @Test
    void save_shouldThrowRuntimeException_whenJsonProcessingFails() throws JsonProcessingException {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(null);
        domain.setRoleBasicInfo(new RoleBasicInfo("管理员", "admin", 1, true));
        domain.setRoleExtendInfo(new RoleExtendInfo("系统管理员", true));
        domain.setDeptId(1L);
        DataPermission dataPermission = new DataPermission();
        dataPermission.setRowScope(new RowScope(Collections.singletonList(1L), null, null, false));
        domain.setDataPermission(dataPermission);
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        when(objectMapper.writeValueAsString(any(DataPermission.class)))
                .thenThrow(new JsonProcessingException("write error") {});

        RuntimeException exception = assertThrows(RuntimeException.class, () -> repository.save(domain));
        assertEquals("Failed to write data permission config", exception.getMessage());
    }
}
