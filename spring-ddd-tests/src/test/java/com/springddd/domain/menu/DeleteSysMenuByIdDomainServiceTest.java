package com.springddd.domain.menu;

import com.springddd.application.service.menu.DeleteSysMenuByIdDomainServiceImpl;
import com.springddd.application.service.menu.SysMenuQueryService;
import com.springddd.application.service.menu.dto.SysMenuView;
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

    @BeforeEach
    void setUp() {
        mockDomain = new SysMenuDomain();
        mockDomain.setMenuId(new MenuId(1L));
        mockDomain.setName("Test Menu");
        mockDomain.setDeleteStatus(false);
    }

    @Test
    void deleteByIds_shouldDeleteSingleId() {
        List<Long> ids = Collections.singletonList(1L);
        List<SysMenuView> menuViews = Collections.singletonList(createMenuView(1L, null));

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(menuViews));
        when(sysMenuDomainRepository.load(new MenuId(1L))).thenReturn(Mono.just(mockDomain));
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysMenuByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysMenuDomainRepository).load(new MenuId(1L));
        verify(sysMenuDomainRepository).save(any(SysMenuDomain.class));
    }

    @Test
    void deleteByIds_shouldDeleteMultipleIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        List<SysMenuView> menuViews = Arrays.asList(
                createMenuView(1L, null),
                createMenuView(2L, 1L),
                createMenuView(3L, 1L)
        );

        SysMenuDomain domain1 = new SysMenuDomain();
        domain1.setMenuId(new MenuId(1L));
        domain1.setDeleteStatus(false);

        SysMenuDomain domain2 = new SysMenuDomain();
        domain2.setMenuId(new MenuId(2L));
        domain2.setDeleteStatus(false);

        SysMenuDomain domain3 = new SysMenuDomain();
        domain3.setMenuId(new MenuId(3L));
        domain3.setDeleteStatus(false);

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(menuViews));
        when(sysMenuDomainRepository.load(new MenuId(1L))).thenReturn(Mono.just(domain1));
        when(sysMenuDomainRepository.load(new MenuId(2L))).thenReturn(Mono.just(domain2));
        when(sysMenuDomainRepository.load(new MenuId(3L))).thenReturn(Mono.just(domain3));
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysMenuByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysMenuDomainRepository).load(new MenuId(1L));
        verify(sysMenuDomainRepository).load(new MenuId(2L));
        verify(sysMenuDomainRepository).load(new MenuId(3L));
    }

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        StepVerifier.create(deleteSysMenuByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysMenuQueryService, never()).queryAllMenu();
        verify(sysMenuDomainRepository, never()).load(any());
        verify(sysMenuDomainRepository, never()).save(any());
    }

    @Test
    void deleteByIds_shouldSetDeleteStatusToTrue() {
        List<Long> ids = Collections.singletonList(1L);
        List<SysMenuView> menuViews = Collections.singletonList(createMenuView(1L, null));

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(menuViews));
        when(sysMenuDomainRepository.load(new MenuId(1L))).thenReturn(Mono.just(mockDomain));
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysMenuByIdDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysMenuDomainRepository).save(argThat(domain -> domain.getDeleteStatus() == true));
    }

    private SysMenuView createMenuView(Long id, Long parentId) {
        SysMenuView view = new SysMenuView();
        view.setId(id);
        view.setParentId(parentId);
        view.setDeleteStatus(false);
        view.setMenuType(1);
        return view;
    }
}
