package com.springddd.infrastructure.persistence;

import static org.assertj.core.api.Assertions.*;

import com.springddd.domain.role.RoleId;
import com.springddd.domain.user.SysUserRoleDomain;
import com.springddd.domain.user.UserId;
import com.springddd.domain.user.UserRoleId;
import com.springddd.infrastructure.persistence.entity.SysUserRoleEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRoleRepository;
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
class SysUserRoleDomainRepositoryImplTest {

    @Mock
    private SysUserRoleRepository sysUserRoleRepository;

    @InjectMocks
    private SysUserRoleDomainRepositoryImpl repository;

    @Test
    @DisplayName("load 应通过 findById 返回手动转换的 domain")
    void load_shouldReturnDomain() {
        UserRoleId userRoleId = new UserRoleId(1L);
        SysUserRoleEntity entity = new SysUserRoleEntity();
        entity.setId(1L);
        entity.setUserId(2L);
        entity.setRoleId(3L);
        entity.setDeptId(4L);
        entity.setDeleteStatus(false);
        entity.setVersion(1);
        entity.setCreateBy("admin");

        given(sysUserRoleRepository.findById(1L)).willReturn(Mono.just(entity));

        StepVerifier.create(repository.load(userRoleId))
                .assertNext(domain -> {
                    org.assertj.core.api.Assertions.assertThat(domain.getUserRoleId().value()).isEqualTo(1L);
                    org.assertj.core.api.Assertions.assertThat(domain.getUserId().value()).isEqualTo(2L);
                    org.assertj.core.api.Assertions.assertThat(domain.getRoleId().value()).isEqualTo(3L);
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
        UserRoleId userRoleId = new UserRoleId(1L);

        given(sysUserRoleRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.load(userRoleId))
                .verifyComplete();
    }

    @Test
    @DisplayName("save 应手动转换 entity 并返回 id")
    void save_shouldReturnId() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setUserRoleId(new UserRoleId(1L));
        domain.setUserId(new UserId(2L));
        domain.setRoleId(new RoleId(3L));
        domain.setDeptId(4L);
        domain.setDeleteStatus(false);
        domain.setVersion(1);
        domain.setCreateBy("admin");

        SysUserRoleEntity savedEntity = new SysUserRoleEntity();
        savedEntity.setId(1L);

        given(sysUserRoleRepository.save(org.mockito.ArgumentMatchers.any(SysUserRoleEntity.class)))
                .willReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete 应调用 deleteById 并返回 Mono<Void>")
    void delete_shouldCallDeleteById() {
        SysUserRoleDomain domain = new SysUserRoleDomain();
        domain.setUserRoleId(new UserRoleId(1L));

        given(sysUserRoleRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.delete(domain))
                .verifyComplete();

        verify(sysUserRoleRepository).deleteById(1L);
    }
}
