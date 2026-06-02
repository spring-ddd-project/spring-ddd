package com.springddd.infrastructure.persistence;

import com.springddd.domain.menu.*;
import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
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
class SysMenuDomainRepositoryImplTest {

    @Mock
    private SysMenuRepository sysMenuRepository;

    @InjectMocks
    private SysMenuDomainRepositoryImpl repository;

    @Test
    void load_shouldReturnDomain_whenEntityExists() {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(1L);
        entity.setParentId(0L);
        entity.setName("系统管理");
        entity.setPath("/system");
        entity.setComponent("system/index");
        entity.setPermission("system:view");
        entity.setApi("/api/system");
        entity.setRedirect("/system/user");
        entity.setSortOrder(1);
        entity.setTitle("系统管理");
        entity.setIcon("icon-system");
        entity.setMenuType(1);
        entity.setVisible(true);
        entity.setAffixTab(false);
        entity.setNoBasicLayout(false);
        entity.setEmbedded(false);
        entity.setMenuStatus(true);
        entity.setDeptId(1L);
        entity.setDeleteStatus(false);
        entity.setVersion(0);
        entity.setCreateBy("system");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("system");
        entity.setUpdateTime(LocalDateTime.now());

        when(sysMenuRepository.findById(1L)).thenReturn(Mono.just(entity));

        StepVerifier.create(repository.load(new MenuId(1L)))
                .assertNext(domain -> {
                    assertEquals(1L, domain.getMenuId().value());
                    assertEquals("系统管理", domain.getName());
                    assertEquals("/system", domain.getMenu().menuPath());
                })
                .verifyComplete();
    }

    @Test
    void load_shouldReturnEmpty_whenEntityNotFound() {
        when(sysMenuRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(repository.load(new MenuId(1L)))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate_withAllFields() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(null);
        domain.setParentId(new MenuId(0L));
        domain.setName("系统管理");
        domain.setCatalog(new Catalog("/system/user"));
        domain.setMenu(new Menu("/system", "system/index", false, false, false));
        domain.setButton(new Button("system:view", "/api/system"));
        domain.setMenuExtendInfo(new MenuExtendInfo(1, "系统管理", "icon-system", 1, true, true));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        SysMenuEntity savedEntity = new SysMenuEntity();
        savedEntity.setId(1L);

        when(sysMenuRepository.save(any(SysMenuEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenSavingNewAggregate_withNullCatalogMenuButton() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(null);
        domain.setParentId(new MenuId(0L));
        domain.setName("系统管理");
        domain.setCatalog(null);
        domain.setMenu(null);
        domain.setButton(null);
        domain.setMenuExtendInfo(new MenuExtendInfo(1, "系统管理", "icon-system", 1, true, true));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(0);

        SysMenuEntity savedEntity = new SysMenuEntity();
        savedEntity.setId(1L);

        when(sysMenuRepository.save(any(SysMenuEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }

    @Test
    void save_shouldReturnId_whenUpdatingExistingAggregate() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new MenuId(1L));
        domain.setParentId(new MenuId(0L));
        domain.setName("系统管理");
        domain.setCatalog(new Catalog("/system/user"));
        domain.setMenu(new Menu("/system", "system/index", false, false, false));
        domain.setButton(new Button("system:view", "/api/system"));
        domain.setMenuExtendInfo(new MenuExtendInfo(1, "系统管理", "icon-system", 1, true, true));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(1);

        SysMenuEntity savedEntity = new SysMenuEntity();
        savedEntity.setId(1L);

        when(sysMenuRepository.save(any(SysMenuEntity.class))).thenReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();
    }
}
