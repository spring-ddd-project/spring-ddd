package com.springddd.infrastructure.persistence;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.role.*;
import com.springddd.infrastructure.persistence.entity.SysRoleMenuEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleMenuRepository;
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
class SysRoleMenuDomainRepositoryImplTest {

    @Mock
    private SysRoleMenuRepository sysRoleMenuRepository;

    @InjectMocks
    private SysRoleMenuDomainRepositoryImpl repository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        SysRoleMenuEntity entity = new SysRoleMenuEntity();
        entity.setId(1L);
        entity.setRoleId(1L);
        entity.setMenuId(1L);
        entity.setDeptId(1L);
        entity.setDeleteStatus(false);
        entity.setVersion(0);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());

        when(sysRoleMenuRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.load(new RoleMenuId(1L)))
                .assertNext(domain -> {
                    assertEquals(1L, domain.getRoleMenuId().value());
                    assertEquals(1L, domain.getRoleId().value());
                    assertEquals(1L, domain.getMenuId().value());
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(sysRoleMenuRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.load(new RoleMenuId(1L)))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.setRoleMenuId(null);
        domain.setRoleId(new RoleId(1L));
        domain.setMenuId(new MenuId(1L));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        SysRoleMenuEntity savedEntity = new SysRoleMenuEntity();
        savedEntity.setId(1L);

        when(sysRoleMenuRepository.save(any(SysRoleMenuEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingExistingAggregate() {
        SysRoleMenuDomain domain = new SysRoleMenuDomain();
        domain.setRoleMenuId(new RoleMenuId(1L));
        domain.setRoleId(new RoleId(1L));
        domain.setMenuId(new MenuId(1L));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        SysRoleMenuEntity savedEntity = new SysRoleMenuEntity();
        savedEntity.setId(1L);

        when(sysRoleMenuRepository.save(any(SysRoleMenuEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }
}
