package com.springddd.infrastructure.persistence;

import com.springddd.domain.menu.*;
import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private SysMenuDomainRepositoryImpl sysMenuDomainRepository;

    @BeforeEach
    void setUp() {
        sysMenuDomainRepository = new SysMenuDomainRepositoryImpl(sysMenuRepository);
    }

    @Test
    void shouldLoadMenuById() {
        Long menuId = 1L;
        Long parentId = 0L;
        
        SysMenuEntity entity = createMenuEntity(menuId, parentId);
        when(sysMenuRepository.findById(menuId)).thenReturn(Mono.just(entity));

        Mono<SysMenuDomain> result = sysMenuDomainRepository.load(new MenuId(menuId));

        StepVerifier.create(result)
                .assertNext(domain -> {
                    assertNotNull(domain);
                    assertEquals(menuId, domain.getMenuId().value());
                    assertEquals(parentId, domain.getParentId().value());
                })
                .verifyComplete();

        verify(sysMenuRepository).findById(menuId);
    }

    @Test
    void shouldReturnEmptyWhenMenuNotFound() {
        Long menuId = 999L;
        when(sysMenuRepository.findById(menuId)).thenReturn(Mono.empty());

        Mono<SysMenuDomain> result = sysMenuDomainRepository.load(new MenuId(menuId));

        StepVerifier.create(result)
                .verifyComplete();

        verify(sysMenuRepository).findById(menuId);
    }

    @Test
    void shouldSaveMenuSuccessfully() {
        SysMenuDomain domain = createSysMenuDomain();
        SysMenuEntity savedEntity = createMenuEntity(1L, 0L);
        
        when(sysMenuRepository.save(any(SysMenuEntity.class))).thenReturn(Mono.just(savedEntity));

        Mono<Long> result = sysMenuDomainRepository.save(domain);

        StepVerifier.create(result)
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();

        verify(sysMenuRepository).save(any(SysMenuEntity.class));
    }

    @Test
    void shouldSaveMenuWithNullOptionalFields() {
        SysMenuDomain domain = createSysMenuDomain();
        domain.setMenuId(null);
        domain.setParentId(null);
        
        SysMenuEntity savedEntity = new SysMenuEntity();
        savedEntity.setId(1L);
        when(sysMenuRepository.save(any(SysMenuEntity.class))).thenReturn(Mono.just(savedEntity));

        Mono<Long> result = sysMenuDomainRepository.save(domain);

        StepVerifier.create(result)
                .assertNext(id -> assertEquals(1L, id))
                .verifyComplete();

        verify(sysMenuRepository).save(any(SysMenuEntity.class));
    }

    private SysMenuEntity createMenuEntity(Long id, Long parentId) {
        SysMenuEntity entity = new SysMenuEntity();
        entity.setId(id);
        entity.setParentId(parentId);
        entity.setName("Test Menu");
        entity.setPath("/test");
        entity.setComponent("test/component");
        entity.setRedirect("/redirect");
        entity.setAffixTab(false);
        entity.setNoBasicLayout(false);
        entity.setEmbedded(false);
        entity.setPermission("test:permission");
        entity.setApi("/api/test");
        entity.setSortOrder(1);
        entity.setTitle("Test Title");
        entity.setIcon("test-icon");
        entity.setMenuType("M");
        entity.setVisible(true);
        entity.setMenuStatus(true);
        entity.setDeptId(1L);
        entity.setDeleteStatus(false);
        entity.setVersion(1L);
        entity.setCreateBy("admin");
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateBy("admin");
        entity.setUpdateTime(LocalDateTime.now());
        return entity;
    }

    private SysMenuDomain createSysMenuDomain() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new MenuId(1L));
        domain.setParentId(new MenuId(0L));
        domain.setCatalog(new Catalog("/redirect"));
        domain.setName("Test Menu");
        domain.setMenu(new Menu("/test", "test/component", false, false, false));
        domain.setButton(new Button("test:permission", "/api/test"));
        domain.setMenuExtendInfo(new MenuExtendInfo(1, "Test Title", "test-icon", "M", true, true));
        domain.setDeptId(1L);
        domain.setDeleteStatus(false);
        domain.setVersion(1L);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        return domain;
    }
}
