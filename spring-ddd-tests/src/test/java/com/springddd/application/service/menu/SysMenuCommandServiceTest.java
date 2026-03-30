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

    @Mock
    private SysMenuDomainStrategy sysMenuDomainStrategy;

    private SysMenuCommandService sysMenuCommandService;

    @BeforeEach
    void setUp() {
        List<SysMenuDomainStrategy> strategies = Collections.singletonList(sysMenuDomainStrategy);
        sysMenuCommandService = new SysMenuCommandService(
                sysMenuDomainFactory,
                sysMenuDomainRepository,
                wipeSysMenuByIdsDomainService,
                strategies,
                deleteSysMenuByIdDomainService,
                restoreSysMenuByIdDomainService
        );
    }

    @Test
    void create_shouldReturnId_whenValidCommand() {
        SysMenuCommand command = new SysMenuCommand();
        command.setParentId(0L);
        command.setName("Test Menu");
        command.setRedirect("/redirect");
        command.setPath("/test");
        command.setComponent("test.vue");
        command.setAffixTab(false);
        command.setNoBasicLayout(false);
        command.setEmbedded(false);
        command.setPermission("test:view");
        command.setApi("/api/test");
        command.setOrder(1);
        command.setTitle("Test Menu");
        command.setIcon("icon");
        command.setMenuType(1);
        command.setVisible(true);
        command.setMenuStatus(true);
        command.setDeptId(1L);

        SysMenuDomain mockDomain = new SysMenuDomain();
        when(sysMenuDomainFactory.create(any(), any(), any(), any(), any(), any(), any())).thenReturn(mockDomain);
        when(sysMenuDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Long> result = sysMenuCommandService.create(command);

        StepVerifier.create(result)
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    void update_shouldComplete_whenValidCommand() {
        SysMenuCommand command = new SysMenuCommand();
        command.setId(1L);
        command.setParentId(0L);
        command.setName("Updated Menu");
        command.setRedirect("/redirect");
        command.setPath("/updated");
        command.setComponent("updated.vue");
        command.setAffixTab(false);
        command.setNoBasicLayout(false);
        command.setEmbedded(false);
        command.setPermission("test:update");
        command.setApi("/api/updated");
        command.setOrder(2);
        command.setTitle("Updated Menu");
        command.setIcon("icon-updated");
        command.setMenuType(1);
        command.setVisible(true);
        command.setMenuStatus(true);
        command.setDeptId(1L);

        SysMenuDomain mockDomain = new SysMenuDomain();
        when(sysMenuDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(sysMenuDomainFactory.create(any(), any(), any(), any(), any(), any(), any())).thenReturn(mockDomain);
        when(sysMenuDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Void> result = sysMenuCommandService.update(command);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void delete_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(deleteSysMenuByIdDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysMenuCommandService.delete(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(wipeSysMenuByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysMenuCommandService.wipe(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void restore_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(restoreSysMenuByIdDomainService.restoreByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysMenuCommandService.restore(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
