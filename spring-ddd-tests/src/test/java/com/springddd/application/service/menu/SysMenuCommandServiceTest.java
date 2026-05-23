package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuCommand;
import com.springddd.domain.menu.*;
import com.springddd.infrastructure.persistence.factory.RepositoryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SysMenuCommandServiceTest {

    @Mock
    private SysMenuDomainFactory sysMenuDomainFactory;

    @Mock
    private RepositoryFactory repositoryFactory;

    @Mock
    private WipeSysMenuByIdsDomainService wipeSysMenuByIdsDomainService;

    @Mock
    private DeleteSysMenuByIdDomainService deleteSysMenuByIdDomainService;

    @Mock
    private RestoreSysMenuByIdDomainService restoreSysMenuByIdDomainService;

    @Mock
    private SysMenuDomainRepository sysMenuDomainRepository;

    @InjectMocks
    private SysMenuCommandService service;

    @BeforeEach
    void setUp() {
        when(repositoryFactory.getSysMenuDomainRepository()).thenReturn(sysMenuDomainRepository);
    }

    @Test
    @DisplayName("create 应调用 factory 和 repository 保存")
    void create_shouldCallFactoryAndSave() {
        SysMenuCommand command = new SysMenuCommand();
        command.setParentId(0L);
        command.setName("Test Menu");
        command.setPath("/test");
        command.setComponent("TestComponent");
        command.setPermission("sys:test:index");
        command.setApi("/api/test");
        command.setOrder(1);
        command.setTitle("Test");
        command.setIcon("icon");
        command.setMenuType(1);
        command.setVisible(true);
        command.setMenuStatus(true);
        command.setDeptId(1L);

        SysMenuDomain domain = mock(SysMenuDomain.class);
        when(sysMenuDomainFactory.create(any(MenuId.class), any(), any(Catalog.class), any(Menu.class), any(Button.class), any(MenuExtendInfo.class), any())).thenReturn(domain);
        when(sysMenuDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.create(command))
                .expectNext(1L)
                .verifyComplete();

        verify(domain).create();
        verify(sysMenuDomainRepository).save(domain);
    }

    @Test
    @DisplayName("update 应加载 domain 并更新保存")
    void update_shouldLoadAndUpdate() {
        SysMenuCommand command = new SysMenuCommand();
        command.setId(1L);
        command.setParentId(0L);
        command.setName("Updated Menu");
        command.setPath("/updated");
        command.setComponent("UpdatedComponent");
        command.setPermission("sys:updated:index");
        command.setApi("/api/updated");
        command.setOrder(2);
        command.setTitle("Updated");
        command.setIcon("icon2");
        command.setMenuType(1);
        command.setVisible(true);
        command.setMenuStatus(true);
        command.setDeptId(1L);

        SysMenuDomain domain = mock(SysMenuDomain.class);
        SysMenuDomain dummy = mock(SysMenuDomain.class);
        when(sysMenuDomainRepository.load(new MenuId(1L))).thenReturn(Mono.just(domain));
        when(sysMenuDomainFactory.create(any(MenuId.class), any(), any(Catalog.class), any(Menu.class), any(Button.class), any(MenuExtendInfo.class), any())).thenReturn(dummy);
        when(dummy.getName()).thenReturn("Updated Menu");
        when(dummy.getCatalog()).thenReturn(mock(Catalog.class));
        when(dummy.getMenu()).thenReturn(mock(Menu.class));
        when(dummy.getButton()).thenReturn(mock(Button.class));
        when(dummy.getMenuExtendInfo()).thenReturn(mock(MenuExtendInfo.class));
        when(sysMenuDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.update(command))
                .verifyComplete();

        verify(domain).update(any(MenuId.class), any(), any(), any(), any(), any(), any());
        verify(sysMenuDomainRepository).save(domain);
    }

    @Test
    @DisplayName("delete 应调用 delete domain service")
    void delete_shouldCallDeleteDomainService() {
        when(deleteSysMenuByIdDomainService.deleteByIds(List.of(1L, 2L))).thenReturn(Mono.empty());

        StepVerifier.create(service.delete(List.of(1L, 2L)))
                .verifyComplete();

        verify(deleteSysMenuByIdDomainService).deleteByIds(List.of(1L, 2L));
    }

    @Test
    @DisplayName("wipe 应调用 wipe domain service")
    void wipe_shouldCallWipeDomainService() {
        when(wipeSysMenuByIdsDomainService.deleteByIds(List.of(1L, 2L))).thenReturn(Mono.empty());

        StepVerifier.create(service.wipe(List.of(1L, 2L)))
                .verifyComplete();

        verify(wipeSysMenuByIdsDomainService).deleteByIds(List.of(1L, 2L));
    }

    @Test
    @DisplayName("restore 应调用 restore domain service")
    void restore_shouldCallRestoreDomainService() {
        when(restoreSysMenuByIdDomainService.restoreByIds(List.of(1L, 2L))).thenReturn(Mono.empty());

        StepVerifier.create(service.restore(List.of(1L, 2L)))
                .verifyComplete();

        verify(restoreSysMenuByIdDomainService).restoreByIds(List.of(1L, 2L));
    }
}
