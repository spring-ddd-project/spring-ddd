package com.springddd.domain.menu;

import com.springddd.application.service.menu.RestoreSysMenuByIdDomainServiceImpl;
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
class RestoreSysMenuByIdDomainServiceTest {

    @Mock
    private SysMenuDomainRepository sysMenuDomainRepository;

    @InjectMocks
    private RestoreSysMenuByIdDomainServiceImpl domainService;

    @Test
    void restoreByIds_shouldRestoreEntities() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new MenuId(1L));
        domain.delete();

        when(sysMenuDomainRepository.load(any(MenuId.class))).thenReturn(Mono.just(domain));
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(domainService.restoreByIds(Arrays.asList(1L, 2L)))
                .verifyComplete();

        verify(sysMenuDomainRepository, times(2)).load(any(MenuId.class));
        verify(sysMenuDomainRepository, times(2)).save(any(SysMenuDomain.class));
    }

    @Test
    void restoreByIds_shouldHandleEmptyList() {
        StepVerifier.create(domainService.restoreByIds(Arrays.asList()))
                .verifyComplete();

        verify(sysMenuDomainRepository, never()).load(any(MenuId.class));
        verify(sysMenuDomainRepository, never()).save(any(SysMenuDomain.class));
    }

    @Test
    void restoreByIds_shouldHandleNonExistingId() {
        when(sysMenuDomainRepository.load(any(MenuId.class))).thenReturn(Mono.empty());

        StepVerifier.create(domainService.restoreByIds(Arrays.asList(999L)))
                .verifyComplete();

        verify(sysMenuDomainRepository, times(1)).load(any(MenuId.class));
        verify(sysMenuDomainRepository, never()).save(any(SysMenuDomain.class));
    }
}
