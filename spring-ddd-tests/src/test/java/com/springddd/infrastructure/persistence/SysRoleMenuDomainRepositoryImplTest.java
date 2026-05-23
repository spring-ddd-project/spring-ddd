package com.springddd.infrastructure.persistence;

import static org.assertj.core.api.Assertions.*;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.RoleMenuId;
import com.springddd.domain.role.SysRoleMenuDomain;
import com.springddd.infrastructure.persistence.entity.SysRoleMenuEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleMenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SysRoleMenuDomainRepositoryImplTest {

    @Mock
    private SysRoleMenuRepository sysRoleMenuRepository;

    @InjectMocks
    private SysRoleMenuDomainRepositoryImpl repository;

    @Test
    @DisplayName("load 应通过 findById 返回手动转换的 domain")
    void load_shouldReturnDomain() {
        RoleMenuId roleMenuId = new RoleMenuId(1L);
        SysRoleMenuEntity entity = new SysRoleMenuEntity();
        entity.setId(1L);
        entity.setRoleId(2L);
        entity.setMenuId(3L);
        entity.setDeptId(4L);
        entity.setDeleteStatus(false);
        entity.setVersion(1);
        entity.setCreateBy("admin");

        given(sysRoleMenuRepository.findById(1L)).willReturn(Mono.just(entity));

        StepVerifier.create(repository.load(roleMenuId))
                .assertNext(domain -> {
                    org.assertj.core.api.Assertions.assertThat(domain.getRoleMenuId().value()).isEqualTo(1L);
                    org.assertj.core.api.Assertions.assertThat(domain.getRoleId().value()).isEqualTo(2L);
                    org.assertj.core.api.Assertions.assertThat(domain.getMenuId().value()).isEqualTo(3L);
                    org.assertj.core.api.Assertions.assertThat(domain.getDeptId()).isEqualTo(4L);
                    org.assertj.core.api.Assertions.assertThat(domain.getDeleteStatus()).isFalse();
                    org.assertj.core.api.Assertions.assertThat(domain.getVersion()).isEqualTo(1);
                    org.assertj.core.api.Assertions.assertThat(domain.getCreateBy()).isEqualTo("admin");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("load 当记录不存在时应返回空 Mono")
    void load_whenNotFound_shouldReturnEmpty() {
        RoleMenuId roleMenuId = new RoleMenuId(1L);

        given(sysRoleMenuRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.load(roleMenuId))
                .verifyComplete();
    }

    @Test
    @DisplayName("save 应手动转换 entity 并返回 id")
    void save_shouldReturnId() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.setRoleMenuId(new RoleMenuId(1L));
        domain.setRoleId(new RoleId(2L));
        domain.setMenuId(new MenuId(3L));
        domain.setDeptId(4L);
        domain.setDeleteStatus(false);
        domain.setVersion(1);
        domain.setCreateBy("admin");

        SysRoleMenuEntity savedEntity = new SysRoleMenuEntity();
        savedEntity.setId(1L);

        given(sysRoleMenuRepository.save(org.mockito.ArgumentMatchers.any(SysRoleMenuEntity.class)))
                .willReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("save 当 roleId 为 null 时应抛出 RoleIdNullException")
    void save_whenRoleIdIsNull_shouldThrowException() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.setRoleMenuId(new RoleMenuId(1L));
        domain.setRoleId(null);
        domain.setMenuId(new MenuId(3L));

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> repository.save(domain))
                .isInstanceOf(com.springddd.domain.role.exception.RoleIdNullException.class);
    }

    @Test
    @DisplayName("save 当 menuId 为 null 时应抛出 MenuIdNullException")
    void save_whenMenuIdIsNull_shouldThrowException() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.setRoleMenuId(new RoleMenuId(1L));
        domain.setRoleId(new RoleId(2L));
        domain.setMenuId(null);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> repository.save(domain))
                .isInstanceOf(com.springddd.domain.menu.exception.MenuIdNullException.class);
    }

    @Test
    @DisplayName("delete 应调用 deleteById 并返回 Mono<Void>")
    void delete_shouldCallDeleteById() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.setRoleMenuId(new RoleMenuId(1L));

        given(sysRoleMenuRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.delete(domain))
                .verifyComplete();

        verify(sysRoleMenuRepository).deleteById(1L);
    }
}
