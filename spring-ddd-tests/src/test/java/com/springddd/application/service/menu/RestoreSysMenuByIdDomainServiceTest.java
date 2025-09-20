package com.springddd.application.service.menu;

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
class RestoreSysMenuByIdDomainServiceTest {

    @Mock
    private SysMenuDomainRepository sysMenuDomainRepository;

    @InjectMocks
    private RestoreSysMenuByIdDomainServiceImpl restoreSysMenuByIdDomainService;

    private SysMenuDomain mockDomain;

    @BeforeEach
    void setUp() {
        mockDomain = new SysMenuDomain();
        mockDomain.setMenuId(new MenuId(1L));
        mockDomain.setName("Test Menu");
        mockDomain.setDeleteStatus(true);
    }

    @Test
    void restoreByIds_shouldRestoreMenu() {
        List<Long> ids = Arrays.asList(1L);

        when(sysMenuDomainRepository.load(any(MenuId.class))).thenReturn(Mono.just(mockDomain));
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysMenuByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysMenuDomainRepository).load(any(MenuId.class));
        verify(sysMenuDomainRepository).save(any(SysMenuDomain.class));
    }

    @Test
    void restoreByIds_shouldHandleMultipleIds() {
        SysMenuDomain domain2 = new SysMenuDomain();
        domain2.setMenuId(new MenuId(2L));
        domain2.setName("Test Menu 2");
        domain2.setDeleteStatus(true);

        List<Long> ids = Arrays.asList(1L, 2L);

        when(sysMenuDomainRepository.load(new MenuId(1L))).thenReturn(Mono.just(mockDomain));
        when(sysMenuDomainRepository.load(new MenuId(2L))).thenReturn(Mono.just(domain2));
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysMenuByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysMenuDomainRepository, times(2)).load(any(MenuId.class));
        verify(sysMenuDomainRepository, times(2)).save(any(SysMenuDomain.class));
    }

    @Test
    void restoreByIds_shouldHandleEmptyIdsList() {
        List<Long> ids = Collections.emptyList();

        StepVerifier.create(restoreSysMenuByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysMenuDomainRepository, never()).load(any());
        verify(sysMenuDomainRepository, never()).save(any());
    }

    @Test
    void restoreByIds_shouldHandleMenuNotFound() {
        List<Long> ids = Arrays.asList(999L);

        when(sysMenuDomainRepository.load(any(MenuId.class))).thenReturn(Mono.empty());

        StepVerifier.create(restoreSysMenuByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysMenuDomainRepository).load(any(MenuId.class));
        verify(sysMenuDomainRepository, never()).save(any());
    }
}
