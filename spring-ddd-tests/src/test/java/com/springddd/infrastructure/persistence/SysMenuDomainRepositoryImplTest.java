package com.springddd.infrastructure.persistence;

import com.springddd.domain.menu.*;
import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
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
@DisplayName("SysMenuDomainRepositoryImpl Tests")
class SysMenuDomainRepositoryImplTest {

    @Mock
    private SysMenuRepository sysMenuRepository;

    @InjectMocks
    private SysMenuDomainRepositoryImpl sysMenuDomainRepository;

    private SysMenuEntity testEntity;

    @BeforeEach
    void setUp() {
        testEntity = new SysMenuEntity();
        testEntity.setId(1L);
        testEntity.setParentId(0L);
        testEntity.setName("Test Menu");
        testEntity.setPath("/test");
        testEntity.setComponent("test/index");
        testEntity.setRedirect("/test");
        testEntity.setPermission("test:view");
        testEntity.setApi("/api/test");
        testEntity.setSortOrder(1);
        testEntity.setTitle("Test Menu Title");
        testEntity.setIcon("test-icon");
        testEntity.setMenuType(1);
        testEntity.setAffixTab(true);
        testEntity.setNoBasicLayout(false);
        testEntity.setEmbedded(false);
        testEntity.setVisible(true);
        testEntity.setMenuStatus(true);
        testEntity.setDeptId(100L);
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
        when(sysMenuRepository.findById(1L)).thenReturn(Mono.just(testEntity));

        StepVerifier.create(sysMenuDomainRepository.load(new MenuId(1L)))
                .assertNext(domain -> {
                    assertThat(domain.getMenuId().value()).isEqualTo(1L);
                    assertThat(domain.getParentId().value()).isEqualTo(0L);
                    assertThat(domain.getCatalog().menuRedirect()).isEqualTo("/test");
                    assertThat(domain.getName()).isEqualTo("Test Menu");
                    assertThat(domain.getMenu().menuPath()).isEqualTo("/test");
                    assertThat(domain.getMenu().component()).isEqualTo("test/index");
                    assertThat(domain.getMenu().affixTab()).isTrue();
                    assertThat(domain.getMenu().noBasicLayout()).isFalse();
                    assertThat(domain.getMenu().embedded()).isFalse();
                    assertThat(domain.getButton().permission()).isEqualTo("test:view");
                    assertThat(domain.getButton().api()).isEqualTo("/api/test");
                    assertThat(domain.getMenuExtendInfo().order()).isEqualTo(1);
                    assertThat(domain.getMenuExtendInfo().title()).isEqualTo("Test Menu Title");
                    assertThat(domain.getMenuExtendInfo().icon()).isEqualTo("test-icon");
                    assertThat(domain.getMenuExtendInfo().menuType()).isEqualTo(1);
                    assertThat(domain.getMenuExtendInfo().visible()).isTrue();
                    assertThat(domain.getMenuExtendInfo().menuStatus()).isTrue();
                    assertThat(domain.getDeptId()).isEqualTo(100L);
                    assertThat(domain.getDeleteStatus()).isFalse();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("load() should return empty when entity not found")
    void load_WhenEntityNotFound_ReturnsEmpty() {
        when(sysMenuRepository.findById(999L)).thenReturn(Mono.empty());

        StepVerifier.create(sysMenuDomainRepository.load(new MenuId(999L)))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() should persist domain and return id")
    void save_WhenValidDomain_ReturnsId() {
        when(sysMenuRepository.save(any(SysMenuEntity.class))).thenReturn(Mono.just(testEntity));

        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new MenuId(1L));
        domain.setParentId(new MenuId(0L));
        domain.setCatalog(new Catalog("/test"));
        domain.setName("Test Menu");
        domain.setMenu(new Menu("/test", "test/index", true, false, false));
        domain.setButton(new Button("test:view", "/api/test"));
        domain.setMenuExtendInfo(new MenuExtendInfo(1, "Test Menu Title", "test-icon", 1, true, true));
        domain.setDeptId(100L);
        domain.setDeleteStatus(false);
        domain.setCreateBy("admin");
        domain.setCreateTime(LocalDateTime.now());
        domain.setUpdateBy("admin");
        domain.setUpdateTime(LocalDateTime.now());
        domain.setVersion(1);

        StepVerifier.create(sysMenuDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null catalog should persist successfully")
    void save_WithNullCatalog_PersistsSuccessfully() {
        SysMenuEntity savedEntity = new SysMenuEntity();
        savedEntity.setId(1L);
        when(sysMenuRepository.save(any(SysMenuEntity.class))).thenReturn(Mono.just(savedEntity));

        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new MenuId(1L));
        domain.setParentId(new MenuId(0L));
        domain.setCatalog(null);
        domain.setName("Test Menu");
        domain.setMenu(new Menu("/test", "test/index", true, false, false));
        domain.setButton(new Button("test:view", "/api/test"));
        domain.setMenuExtendInfo(new MenuExtendInfo(1, "Test Menu Title", "test-icon", 1, true, true));

        StepVerifier.create(sysMenuDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null menu should persist successfully")
    void save_WithNullMenu_PersistsSuccessfully() {
        SysMenuEntity savedEntity = new SysMenuEntity();
        savedEntity.setId(1L);
        when(sysMenuRepository.save(any(SysMenuEntity.class))).thenReturn(Mono.just(savedEntity));

        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new MenuId(1L));
        domain.setParentId(new MenuId(0L));
        domain.setCatalog(new Catalog("/test"));
        domain.setName("Test Menu");
        domain.setMenu(null);
        domain.setButton(new Button("test:view", "/api/test"));
        domain.setMenuExtendInfo(new MenuExtendInfo(1, "Test Menu Title", "test-icon", 1, true, true));

        StepVerifier.create(sysMenuDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null button should persist successfully")
    void save_WithNullButton_PersistsSuccessfully() {
        SysMenuEntity savedEntity = new SysMenuEntity();
        savedEntity.setId(1L);
        when(sysMenuRepository.save(any(SysMenuEntity.class))).thenReturn(Mono.just(savedEntity));

        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new MenuId(1L));
        domain.setParentId(new MenuId(0L));
        domain.setCatalog(new Catalog("/test"));
        domain.setName("Test Menu");
        domain.setMenu(new Menu("/test", "test/index", true, false, false));
        domain.setButton(null);
        domain.setMenuExtendInfo(new MenuExtendInfo(1, "Test Menu Title", "test-icon", 1, true, true));

        StepVerifier.create(sysMenuDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(1L))
                .verifyComplete();
    }

    @Test
    @DisplayName("save() with null ids should create new entity")
    void save_WithNullIds_CreatesNewEntity() {
        SysMenuEntity newEntity = new SysMenuEntity();
        newEntity.setId(2L);
        when(sysMenuRepository.save(any(SysMenuEntity.class))).thenReturn(Mono.just(newEntity));

        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(null);
        domain.setParentId(null);
        domain.setCatalog(new Catalog("/new"));
        domain.setName("New Menu");
        domain.setMenu(new Menu("/new", "new/index", false, false, false));
        domain.setButton(new Button("new:view", "/api/new"));
        domain.setMenuExtendInfo(new MenuExtendInfo(2, "New Menu", "new-icon", 1, true, true));

        StepVerifier.create(sysMenuDomainRepository.save(domain))
                .assertNext(id -> assertThat(id).isEqualTo(2L))
                .verifyComplete();
    }
}
