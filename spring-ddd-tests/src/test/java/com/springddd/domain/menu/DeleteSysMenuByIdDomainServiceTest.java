package com.springddd.domain.menu;

import com.springddd.application.service.menu.DeleteSysMenuByIdDomainServiceImpl;
import com.springddd.application.service.menu.SysMenuQueryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteSysMenuByIdDomainServiceTest {

    @Mock
    private SysMenuDomainRepository sysMenuDomainRepository;

    @Mock
    private SysMenuQueryService sysMenuQueryService;

    @InjectMocks
    private DeleteSysMenuByIdDomainServiceImpl domainService;

    @Test
    void deleteByIds_shouldDeleteEntities() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new MenuId(1L));

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(Arrays.asList()));
        when(sysMenuDomainRepository.load(any(MenuId.class))).thenReturn(Mono.just(domain));
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(domainService.deleteByIds(Arrays.asList(1L, 2L)))
                .verifyComplete();

        verify(sysMenuQueryService, times(1)).queryAllMenu();
    }

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        StepVerifier.create(domainService.deleteByIds(Arrays.asList()))
                .verifyComplete();

        verify(sysMenuQueryService, never()).queryAllMenu();
    }

    @Test
    void deleteByIds_shouldHandleNonExistingId() {
        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(Arrays.asList()));
        when(sysMenuDomainRepository.load(any(MenuId.class))).thenReturn(Mono.empty());

        StepVerifier.create(domainService.deleteByIds(Arrays.asList(999L)))
                .verifyComplete();

        verify(sysMenuQueryService, times(1)).queryAllMenu();
    }
}