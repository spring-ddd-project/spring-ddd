package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.domain.menu.MenuId;
import com.springddd.domain.menu.SysMenuDomain;
import com.springddd.domain.menu.SysMenuDomainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteSysMenuByIdDomainServiceTest {

    @Mock
    private SysMenuDomainRepository sysMenuDomainRepository;

    @Mock
    private SysMenuQueryService sysMenuQueryService;

    @InjectMocks
    private DeleteSysMenuByIdDomainServiceImpl deleteSysMenuByIdDomainService;

    private SysMenuDomain mockDomain;
    private SysMenuView mockView;

    @BeforeEach
    void setUp() {
        mockDomain = new SysMenuDomain();
        mockDomain.setMenuId(new MenuId(1L));
        mockDomain.setName("Test Menu");
        mockDomain.setDeleteStatus(false);

        mockView = new SysMenuView();
        mockView.setId(1L);
        mockView.setParentId(0L);
        mockView.setName("Test Menu");
        mockView.setDeleteStatus(false);
    }

    @Test
    void deleteByIds_shouldDeleteMenuAndChildren() {
        List<Long> ids = Arrays.asList(1L);
        List<SysMenuView> allMenus = Arrays.asList(mockView);

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(allMenus));
        when(sysMenuDomainRepository.load(any(MenuId.class))).thenReturn(Mono.just(mockDomain));
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysMenuByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysMenuQueryService).queryAllMenu();
        verify(sysMenuDomainRepository).load(any(MenuId.class));
        verify(sysMenuDomainRepository).save(any(SysMenuDomain.class));
    }

    @Test
    void deleteByIds_shouldHandleEmptyIdsList() {
        List<Long> ids = Collections.emptyList();

        StepVerifier.create(deleteSysMenuByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysMenuQueryService, never()).queryAllMenu();
        verify(sysMenuDomainRepository, never()).load(any());
    }

    @Test
    void deleteByIds_shouldHandleNoMenuFound() {
        List<Long> ids = Arrays.asList(999L);
        List<SysMenuView> allMenus = Collections.emptyList();

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(allMenus));

        StepVerifier.create(deleteSysMenuByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysMenuDomainRepository, never()).load(any());
    }

    @Test
    void deleteByIds_shouldDeleteMultipleMenus() {
        SysMenuView childView = new SysMenuView();
        childView.setId(2L);
        childView.setParentId(1L);
        childView.setName("Child Menu");
        childView.setDeleteStatus(false);

        List<Long> ids = Arrays.asList(1L);
        List<SysMenuView> allMenus = Arrays.asList(mockView, childView);

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(allMenus));
        when(sysMenuDomainRepository.load(any(MenuId.class))).thenReturn(Mono.just(mockDomain));
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysMenuByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysMenuQueryService).queryAllMenu();
    }
}
