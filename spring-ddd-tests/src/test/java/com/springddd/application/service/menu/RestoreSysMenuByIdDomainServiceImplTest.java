package com.springddd.application.service.menu;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.menu.RestoreSysMenuByIdDomainService;
import com.springddd.domain.menu.SysMenuDomain;
import com.springddd.domain.menu.SysMenuDomainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestoreSysMenuByIdDomainServiceImplTest {

    @Mock
    private SysMenuDomainRepository sysMenuDomainRepository;

    private RestoreSysMenuByIdDomainService restoreSysMenuByIdDomainService;

    @BeforeEach
    void setUp() {
        restoreSysMenuByIdDomainService = new RestoreSysMenuByIdDomainServiceImpl(
                sysMenuDomainRepository
        );
    }

    @Test
    void restoreByIds_shouldRestoreSingleMenu() {
        Long menuId = 1L;
        List<Long> ids = Arrays.asList(menuId);

        SysMenuDomain domain = new SysMenuDomain();
        domain.setDeleteStatus(true);

        when(sysMenuDomainRepository.load(new MenuId(menuId))).thenReturn(Mono.just(domain));
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        Mono<Void> result = restoreSysMenuByIdDomainService.restoreByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();

        verify(sysMenuDomainRepository).load(new MenuId(menuId));
        verify(sysMenuDomainRepository).save(any(SysMenuDomain.class));
    }

    @Test
    void restoreByIds_shouldRestoreMultipleMenus() {
        Long menuId1 = 1L;
        Long menuId2 = 2L;
        List<Long> ids = Arrays.asList(menuId1, menuId2);

        SysMenuDomain domain1 = new SysMenuDomain();
        domain1.setDeleteStatus(true);
        SysMenuDomain domain2 = new SysMenuDomain();
        domain2.setDeleteStatus(true);

        when(sysMenuDomainRepository.load(new MenuId(menuId1))).thenReturn(Mono.just(domain1));
        when(sysMenuDomainRepository.load(new MenuId(menuId2))).thenReturn(Mono.just(domain2));
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        Mono<Void> result = restoreSysMenuByIdDomainService.restoreByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();

        verify(sysMenuDomainRepository, times(2)).load(any(MenuId.class));
        verify(sysMenuDomainRepository, times(2)).save(any(SysMenuDomain.class));
    }

    @Test
    void restoreByIds_shouldHandleEmptyList() {
        List<Long> ids = Arrays.asList();

        Mono<Void> result = restoreSysMenuByIdDomainService.restoreByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();

        verifyNoInteractions(sysMenuDomainRepository);
    }

    @Test
    void restoreByIds_shouldContinue_whenOneMenuNotFound() {
        Long menuId1 = 1L;
        Long menuId2 = 2L;
        List<Long> ids = Arrays.asList(menuId1, menuId2);

        SysMenuDomain domain1 = new SysMenuDomain();
        domain1.setDeleteStatus(true);

        when(sysMenuDomainRepository.load(new MenuId(menuId1))).thenReturn(Mono.just(domain1));
        when(sysMenuDomainRepository.load(new MenuId(menuId2))).thenReturn(Mono.empty());
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        Mono<Void> result = restoreSysMenuByIdDomainService.restoreByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();

        verify(sysMenuDomainRepository).load(new MenuId(menuId1));
        verify(sysMenuDomainRepository).save(any(SysMenuDomain.class));
    }
}
