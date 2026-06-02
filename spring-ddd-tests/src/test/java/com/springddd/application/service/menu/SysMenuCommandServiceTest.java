package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuCommand;
import com.springddd.domain.menu.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysMenuCommandServiceTest {

    @Mock
    private SysMenuDomainFactory sysMenuDomainFactory;

    @Mock
    private SysMenuDomainRepository sysMenuDomainRepository;

    @Mock
    private WipeSysMenuByIdsDomainService wipeSysMenuByIdsDomainService;

    @Mock
    private DeleteSysMenuByIdDomainService deleteSysMenuByIdDomainService;

    @Mock
    private RestoreSysMenuByIdDomainService restoreSysMenuByIdDomainService;

    private SysMenuCommandService sysMenuCommandService;

    @BeforeEach
    void setUp() {
        sysMenuCommandService = new SysMenuCommandService(
                sysMenuDomainFactory,
                sysMenuDomainRepository,
                wipeSysMenuByIdsDomainService,
                Collections.emptyList(),
                deleteSysMenuByIdDomainService,
                restoreSysMenuByIdDomainService
        );
    }

    @Test
    void create_shouldReturnId_whenValidCommand() {
        SysMenuCommand command = new SysMenuCommand();
        command.setParentId(0L);
        command.setName("testMenu");
        command.setPath("/test");
        command.setComponent("TestComponent");
        command.setRedirect("/redirect");
        command.setApi("/api/test");
        command.setPermission("test:perm");
        command.setOrder(1);
        command.setTitle("Test Title");
        command.setAffixTab(false);
        command.setNoBasicLayout(false);
        command.setIcon("icon");
        command.setMenuType(1);
        command.setVisible(true);
        command.setEmbedded(false);
        command.setMenuStatus(true);
        command.setDeptId(1L);

        SysMenuDomain mockDomain = new SysMenuDomain();
        mockDomain.setName("testMenu");
        when(sysMenuDomainFactory.create(any(), any(), any(), any(), any(), any(), any())).thenReturn(mockDomain);
        when(sysMenuDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(sysMenuCommandService.create(command))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void update_shouldComplete_whenValidCommand() {
        SysMenuCommand command = new SysMenuCommand();
        command.setId(1L);
        command.setParentId(0L);
        command.setName("updatedMenu");
        command.setPath("/updated");
        command.setComponent("UpdatedComponent");
        command.setRedirect("/updated-redirect");
        command.setApi("/api/updated");
        command.setPermission("updated:perm");
        command.setOrder(2);
        command.setTitle("Updated Title");
        command.setAffixTab(true);
        command.setNoBasicLayout(true);
        command.setIcon("updated-icon");
        command.setMenuType(2);
        command.setVisible(false);
        command.setEmbedded(true);
        command.setMenuStatus(false);
        command.setDeptId(2L);

        SysMenuDomain existingDomain = new SysMenuDomain();
        existingDomain.setName("oldMenu");

        SysMenuDomain dummyDomain = new SysMenuDomain();
        dummyDomain.setName("updatedMenu");
        dummyDomain.setCatalog(new Catalog("/updated-redirect"));
        dummyDomain.setMenu(new Menu("/updated", "UpdatedComponent", true, true, true));
        dummyDomain.setButton(new Button("updated:perm", "/api/updated"));
        dummyDomain.setMenuExtendInfo(new MenuExtendInfo(2, "Updated Title", "updated-icon", 2, true, false));

        when(sysMenuDomainRepository.load(any())).thenReturn(Mono.just(existingDomain));
        when(sysMenuDomainFactory.create(any(), any(), any(), any(), any(), any(), any())).thenReturn(dummyDomain);
        when(sysMenuDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(sysMenuCommandService.update(command))
                .verifyComplete();
    }

    @Test
    void delete_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteSysMenuByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysMenuCommandService.delete(ids))
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeSysMenuByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysMenuCommandService.wipe(ids))
                .verifyComplete();
    }

    @Test
    void restore_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreSysMenuByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        StepVerifier.create(sysMenuCommandService.restore(ids))
                .verifyComplete();
    }
}
