package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuCommand;
import com.springddd.domain.menu.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysMenuCommandServiceTest {

    @Mock
    private SysMenuDomainFactory sysMenuDomainFactory;

    @Mock
    private SysMenuDomainRepository sysMenuDomainRepository;

    @Mock
    private WipeSysMenuByIdsDomainService wipeSysMenuByIdsDomainService;

    @Mock
    private List<SysMenuDomainStrategy> strategies;

    @Mock
    private DeleteSysMenuByIdDomainService deleteSysMenuByIdDomainService;

    @Mock
    private RestoreSysMenuByIdDomainService restoreSysMenuByIdDomainService;

    @InjectMocks
    private SysMenuCommandService sysMenuCommandService;

    private SysMenuCommand createCommand;
    private SysMenuDomain mockDomain;

    @BeforeEach
    void setUp() {
        createCommand = new SysMenuCommand();
        createCommand.setParentId(0L);
        createCommand.setName("Test Menu");
        createCommand.setPath("/test");
        createCommand.setComponent("TestComponent");
        createCommand.setRedirect("/index");
        createCommand.setPermission("test:view");
        createCommand.setApi("/api/test");
        createCommand.setOrder(1);
        createCommand.setTitle("Test Title");
        createCommand.setAffixTab(true);
        createCommand.setNoBasicLayout(false);
        createCommand.setIcon("test-icon");
        createCommand.setMenuType(1);
        createCommand.setVisible(true);
        createCommand.setEmbedded(false);
        createCommand.setMenuStatus(true);
        createCommand.setDeptId(100L);

        mockDomain = new SysMenuDomain();
        mockDomain.setMenuId(new MenuId(1L));
        mockDomain.setParentId(new MenuId(0L));
        mockDomain.setName("Test Menu");
        mockDomain.setCatalog(new Catalog("/index"));
        mockDomain.setMenuExtendInfo(new MenuExtendInfo(1, "Test Title", "test-icon", 1, true, true));
        mockDomain.setDeleteStatus(false);
    }

    @Test
    void create_shouldCreateMenuSuccessfully() {
        when(sysMenuDomainFactory.create(any(MenuId.class), anyString(), any(Catalog.class),
                any(Menu.class), any(Button.class), any(MenuExtendInfo.class), anyLong()))
                .thenReturn(mockDomain);
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(sysMenuCommandService.create(createCommand))
                .expectNext(1L)
                .verifyComplete();

        verify(sysMenuDomainFactory).create(any(MenuId.class), eq("Test Menu"), any(Catalog.class),
                any(Menu.class), any(Button.class), any(MenuExtendInfo.class), eq(100L));
        verify(sysMenuDomainRepository).save(any(SysMenuDomain.class));
    }

    @Test
    void create_shouldReturnIdOnSuccess() {
        when(sysMenuDomainFactory.create(any(MenuId.class), anyString(), any(Catalog.class),
                any(Menu.class), any(Button.class), any(MenuExtendInfo.class), anyLong()))
                .thenReturn(mockDomain);
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(100L));

        StepVerifier.create(sysMenuCommandService.create(createCommand))
                .expectNext(100L)
                .verifyComplete();
    }

    @Test
    void update_shouldUpdateMenuSuccessfully() {
        SysMenuCommand updateCommand = new SysMenuCommand();
        updateCommand.setId(1L);
        updateCommand.setParentId(0L);
        updateCommand.setName("Updated Menu");
        updateCommand.setPath("/updated");
        updateCommand.setComponent("UpdatedComponent");
        updateCommand.setRedirect("/home");
        updateCommand.setPermission("test:update");
        updateCommand.setApi("/api/update");
        updateCommand.setOrder(2);
        updateCommand.setTitle("Updated Title");
        updateCommand.setAffixTab(false);
        updateCommand.setNoBasicLayout(true);
        updateCommand.setIcon("updated-icon");
        updateCommand.setMenuType(2);
        updateCommand.setVisible(false);
        updateCommand.setEmbedded(true);
        updateCommand.setMenuStatus(false);
        updateCommand.setDeptId(100L);

        SysMenuDomain dummyDomain = new SysMenuDomain();
        dummyDomain.setName("Updated Menu");
        dummyDomain.setCatalog(new Catalog("/home"));
        dummyDomain.setMenu(new Menu("/updated", "UpdatedComponent", false, true, true));
        dummyDomain.setButton(new Button("test:update", "/api/update"));
        dummyDomain.setMenuExtendInfo(new MenuExtendInfo(2, "Updated Title", "updated-icon", 2, false, false));

        when(sysMenuDomainRepository.load(any(MenuId.class))).thenReturn(Mono.just(mockDomain));
        when(sysMenuDomainFactory.create(any(MenuId.class), anyString(), any(Catalog.class),
                any(Menu.class), any(Button.class), any(MenuExtendInfo.class), anyLong()))
                .thenReturn(dummyDomain);
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(sysMenuCommandService.update(updateCommand))
                .verifyComplete();

        verify(sysMenuDomainRepository).load(any(MenuId.class));
        verify(sysMenuDomainRepository).save(any(SysMenuDomain.class));
    }

    @Test
    void update_shouldCompleteWhenDomainNotFound() {
        SysMenuCommand updateCommand = new SysMenuCommand();
        updateCommand.setId(999L);
        updateCommand.setParentId(0L);
        updateCommand.setName("Updated Menu");
        updateCommand.setMenuType(1);

        when(sysMenuDomainRepository.load(any(MenuId.class))).thenReturn(Mono.empty());

        StepVerifier.create(sysMenuCommandService.update(updateCommand))
                .verifyComplete();

        verify(sysMenuDomainRepository, never()).save(any());
    }

    @Test
    void delete_shouldCallDeleteDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        when(deleteSysMenuByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysMenuCommandService.delete(ids))
                .verifyComplete();

        verify(deleteSysMenuByIdDomainService).deleteByIds(ids);
    }

    @Test
    void wipe_shouldCallWipeDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeSysMenuByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysMenuCommandService.wipe(ids))
                .verifyComplete();

        verify(wipeSysMenuByIdsDomainService).deleteByIds(ids);
    }

    @Test
    void restore_shouldCallRestoreDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreSysMenuByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysMenuCommandService.restore(ids))
                .verifyComplete();

        verify(restoreSysMenuByIdDomainService).restoreByIds(ids);
    }
}
