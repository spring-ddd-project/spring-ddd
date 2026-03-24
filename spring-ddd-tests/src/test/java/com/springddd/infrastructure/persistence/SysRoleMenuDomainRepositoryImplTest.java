package com.springddd.infrastructure.persistence;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.RoleMenuId;
import com.springddd.domain.role.SysRoleMenuDomain;
import com.springddd.infrastructure.persistence.entity.SysRoleMenuEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleMenuRepository;
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
@DisplayName("SysRoleMenuDomainRepositoryImpl Tests")
class SysRoleMenuDomainRepositoryImplTest {

    @Mock
    private SysRoleMenuRepository sysRoleMenuRepository;

    @InjectMocks
    private SysRoleMenuDomainRepositoryImpl sysRoleMenuDomainRepository;

    private SysRoleMenuEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new SysRoleMenuEntity();
        testEntity.setId(1L);
        testEntity.setRoleId(100L);
        testEntity.setMenuId(200L);
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
        when(sysRoleMenuRepository.findById(1L)).thenReturn(Mono.just(testEntity));

        StepVerifier.create(sysRoleMenuDomainRepository.load(new RoleMenuId(1L)))
                .assertNext(domain -> {
                    assertThat(domain.getRoleMenuId().value()).isEqualTo(1L);
                    assertThat(domain.getRoleId().value()).isEqualTo(100L);
                    assertThat(domain.getMenuId().value()).isEqualTo(200L);
                    assertThat(domain.getDeptId()).isEqualTo(1L);
                    assertThat(domain.getDeleteStatus()).isFalse();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("load() should return empty when entity not found")
    void load_WhenEntityNotFound_ReturnsEmpty() {
        when(sysRoleMenuRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(sysRoleMenuDomainRepository.load(new RoleMenuId(999L)))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() should persist domain and return id")
    void save_WhenValidDomain_ReturnsId() {
        when(sysRoleMenuRepository.save(any(SysRoleMenuEntity.class))).thenReturn(Mono.just(testEntity));

        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.setRoleMenuId(new RoleMenuId(1L));
        domain.setRoleId(new RoleId(100L));
        domain.setMenuId(new MenuId(200L));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        StepVerifier.create(sysRoleMenuDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null id should create new entity")
    void save_WithNullId_CreatesNewEntity() {
        SysRoleMenuEntity newEntity = new SysRoleMenuEntity();
        newEntity.setId(2L);
        when(sysRoleMenuRepository.save(any(SysRoleMenuEntity.class))).thenReturn(Mono.just(newEntity));

        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.setRoleMenuId(null);
        domain.setRoleId(new RoleId(100L));
        domain.setMenuId(new MenuId(200L));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);

        StepVerifier.create(sysRoleMenuDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(2L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() should persist domain with all audit fields")
    void save_WithAllAuditFields_PersistsSuccessfully() {
        SysRoleMenuEntity savedEntity = new SysRoleMenuEntity();
        savedEntity.setId(1L);
        when(sysRoleMenuRepository.save(any(SysRoleMenuEntity.class))).thenReturn(Mono.just(savedEntity));

        LocalDateTime now = LocalDateTime.now();

        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.setRoleMenuId(new RoleMenuId(1L));
        domain.setRoleId(new RoleId(100L));
        domain.setMenuId(new MenuId(200L));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(now);
        domain.setUpdateBy("admin");
        domain.setUpdateTime(now);
        domain.setVersion(1);

        StepVerifier.create(sysRoleMenuDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }
}
