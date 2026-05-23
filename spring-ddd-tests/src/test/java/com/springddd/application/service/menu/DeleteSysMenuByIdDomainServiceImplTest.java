package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.domain.menu.MenuId;
import com.springddd.domain.menu.SysMenuDomain;
import com.springddd.domain.menu.SysMenuDomainRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteSysMenuByIdDomainServiceImplTest {

    @Mock
    private SysMenuDomainRepository sysMenuDomainRepository;

    @Mock
    private SysMenuQueryService sysMenuQueryService;

    @InjectMocks
    private DeleteSysMenuByIdDomainServiceImpl service;

    @Test
    @DisplayName("deleteByIds 应删除菜单及其子菜单")
    void deleteByIds_shouldDeleteAndChildren() {
        SysMenuView parent = new SysMenuView();
        parent.setId(1L);
        parent.setParentId(null);

        SysMenuView child = new SysMenuView();
        child.setId(2L);
        child.setParentId(1L);

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(List.of(parent, child)));

        SysMenuDomain domain1 = mock(SysMenuDomain.class);
        SysMenuDomain domain2 = mock(SysMenuDomain.class);
        when(sysMenuDomainRepository.load(new MenuId(1L))).thenReturn(Mono.just(domain1));
        when(sysMenuDomainRepository.load(new MenuId(2L))).thenReturn(Mono.just(domain2));
        when(sysMenuDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();

        verify(domain1).delete();
        verify(domain2).delete();
        verify(sysMenuDomainRepository, times(2)).save(any());
    }

    @Test
    @DisplayName("deleteByIds 应处理空列表")
    void deleteByIds_shouldHandleEmptyList() {
        StepVerifier.create(service.deleteByIds(List.of()))
                .verifyComplete();

        verify(sysMenuQueryService, never()).queryAllMenu();
    }
}
