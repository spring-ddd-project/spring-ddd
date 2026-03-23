package com.springddd.infrastructure.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.domain.role.*;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SysRoleDomainRepositoryImpl Tests")
class SysRoleDomainRepositoryImplTest {

    @Mock
    private SysRoleRepository sysRoleRepository;

    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private SysRoleDomainRepositoryImpl sysRoleDomainRepository;

    private SysRoleEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new SysRoleEntity();
        testEntity.setId(1L);
        testEntity.setRoleName("Admin Role");
        testEntity.setRoleCode("admin");
        testEntity.setRoleDesc("Administrator role");
        testEntity.setDataScope(1);
        testEntity.setDataPermission("{\"rowScope\":{\"self\":true},\"columnRules\":[]}");
        testEntity.setRoleStatus(true);
        testEntity.setOwnerStatus(true);
        testEntity.setDeptId(100L);
        testEntity.setDeleteStatus(false);
        testEntity.setCreateBy("admin");
        testEntity.setCreateTime(LocalDateTime.now());
        testEntity.setUpdateBy("admin");
        testEntity.setUpdateTime(LocalDateTime.now());
        testEntity.setVersion(1);
    }

    @Test
    @DisplayName("load() should return domain when entity exists with dataPermission")
    void load_WhenEntityExistsWithDataPermission_ReturnsDomain() {
        when(sysRoleRepository.findById(1L)).thenReturn(Mono.just(testEntity));

        StepVerifier.create(sysRoleDomainRepository.load(new RoleId(1L)))
                .assertNext(domain -> {
                    assertThat(domain.getRoleId().value()).isEqualTo(1L);
                    assertThat(domain.getRoleBasicInfo().roleName()).isEqualTo("Admin Role");
                    assertThat(domain.getRoleBasicInfo().roleCode()).isEqualTo("admin");
                    assertThat(domain.getRoleBasicInfo().roleDataScope()).isEqualTo(1);
                    assertThat(domain.getRoleBasicInfo().roleOwner()).isTrue();
                    assertThat(domain.getRoleExtendInfo().roleDesc()).isEqualTo("Administrator role");
                    assertThat(domain.getRoleExtendInfo().roleStatus()).isTrue();
                    assertThat(domain.getDeptId()).isEqualTo(100L);
                    assertThat(domain.getDataPermission()).isNotNull();
                    assertThat(domain.getDeleteStatus()).isFalse();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("load() should return domain when entity exists without dataPermission")
    void load_WhenEntityExistsWithoutDataPermission_ReturnsDomain() {
        testEntity.setDataPermission(null);
        when(sysRoleRepository.findById(1L)).thenReturn(Mono.just(testEntity));

        StepVerifier.create(sysRoleDomainRepository.load(new RoleId(1L)))
                .assertNext(domain -> {
                    assertThat(domain.getRoleId().value()).isEqualTo(1L);
                    assertThat(domain.getDataPermission()).isNull();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("load() should return empty when entity not found")
    void load_WhenEntityNotFound_ReturnsEmpty() {
        when(sysRoleRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(sysRoleDomainRepository.load(new RoleId(999L)))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() should persist domain and return id")
    void save_WhenValidDomain_ReturnsId() {
        when(sysRoleRepository.save(any(SysRoleEntity.class))).thenReturn(Mono.just(testEntity));

        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(new RoleId(1L));
        RoleBasicInfo basicInfo = new RoleBasicInfo("Admin Role", "admin", 1, true);
        domain.setRoleBasicInfo(basicInfo);
        RoleExtendInfo extendInfo = new RoleExtendInfo("Administrator role", true);
        domain.setRoleExtendInfo(extendInfo);
        domain.setDeptId(100L);
        RowScope rowScope = new RowScope();
        rowScope.setSelf(true);
        DataPermission dataPermission = new DataPermission(rowScope, Collections.emptyList());
        domain.setDataPermission(dataPermission);
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        StepVerifier.create(sysRoleDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null dataPermission should persist successfully")
    void save_WithNullDataPermission_PersistsSuccessfully() {
        SysRoleEntity savedEntity = new SysRoleEntity();
        savedEntity.setId(1L);
        when(sysRoleRepository.save(any(SysRoleEntity.class))).thenReturn(Mono.just(savedEntity));

        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(new RoleId(1L));
        RoleBasicInfo basicInfo = new RoleBasicInfo("Admin Role", "admin", 1, true);
        domain.setRoleBasicInfo(basicInfo);
        RoleExtendInfo extendInfo = new RoleExtendInfo("Administrator role", true);
        domain.setRoleExtendInfo(extendInfo);
        domain.setDeptId(100L);
        domain.setDataPermission(null);
        domain.setDeleteStatus(false);

        StepVerifier.create(sysRoleDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null id should create new entity")
    void save_WithNullId_CreatesNewEntity() {
        SysRoleEntity newEntity = new SysRoleEntity();
        newEntity.setId(2L);
        when(sysRoleRepository.save(any(SysRoleEntity.class))).thenReturn(Mono.just(newEntity));

        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(null);
        RoleBasicInfo basicInfo = new RoleBasicInfo("New Role", "new_role", 1, true);
        domain.setRoleBasicInfo(basicInfo);
        RoleExtendInfo extendInfo = new RoleExtendInfo("New role description", true);
        domain.setRoleExtendInfo(extendInfo);

        StepVerifier.create(sysRoleDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(2L))
                .verifyComplete();
    }
}
