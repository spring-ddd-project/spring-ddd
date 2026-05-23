package com.springddd.infrastructure.persistence;

import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.SysRoleDomain;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
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
class SysRoleDomainRepositoryImplTest {

    @Mock
    private SysRoleRepository sysRoleRepository;

    @Mock
    private EntityFactory entityFactory;

    @InjectMocks
    private SysRoleDomainRepositoryImpl repository;

    @Test
    @DisplayName("load 应通过 findById 和 entityFactory 返回 domain")
    void load_shouldReturnDomain() {
        RoleId roleId = new RoleId(1L);
        SysRoleEntity entity = new SysRoleEntity();
        SysRoleDomain domain = new SysRoleDomain();

        given(sysRoleRepository.findById(1L)).willReturn(Mono.just(entity));
        given(entityFactory.createSysRoleDomain(entity)).willReturn(domain);

        StepVerifier.create(repository.load(roleId))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("load 当记录不存在时应返回空 Mono")
    void load_whenNotFound_shouldReturnEmpty() {
        RoleId roleId = new RoleId(1L);

        given(sysRoleRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.load(roleId))
                .verifyComplete();
    }

    @Test
    @DisplayName("save 应通过 entityFactory 转换并返回 id")
    void save_shouldReturnId() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(new RoleId(1L));
        SysRoleEntity entity = new SysRoleEntity();
        SysRoleEntity savedEntity = new SysRoleEntity();
        savedEntity.setId(1L);

        given(entityFactory.createSysRoleEntity(domain)).willReturn(entity);
        given(sysRoleRepository.save(entity)).willReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete 应调用 deleteById 并返回 Mono<Void>")
    void delete_shouldCallDeleteById() {
        SysRoleDomain domain = new SysRoleDomain();
        domain.setRoleId(new RoleId(1L));

        given(sysRoleRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.delete(domain))
                .verifyComplete();

        verify(sysRoleRepository).deleteById(1L);
    }
}
