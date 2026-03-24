package com.springddd.domain.menu;

import com.springddd.application.service.menu.RestoreSysMenuByIdDomainServiceImpl;
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
    void restoreByIds_shouldRestoreSingleId() {
        List<Long> ids = Collections.singletonList(1L);

        when(sysMenuDomainRepository.load(new MenuId(1L))).thenReturn(Mono.just(mockDomain));
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysMenuByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysMenuDomainRepository).load(new MenuId(1L));
        verify(sysMenuDomainRepository).save(any(SysMenuDomain.class));
    }

    @Test
    void restoreByIds_shouldRestoreMultipleIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        SysMenuDomain domain1 = new SysMenuDomain();
        domain1.setMenuId(new MenuId(1L));
        domain1.setDeleteStatus(true);

        SysMenuDomain domain2 = new SysMenuDomain();
        domain2.setMenuId(new MenuId(2L));
        domain2.setDeleteStatus(true);

        SysMenuDomain domain3 = new SysMenuDomain();
        domain3.setMenuId(new MenuId(3L));
        domain3.setDeleteStatus(true);

        when(sysMenuDomainRepository.load(new MenuId(1L))).thenReturn(Mono.just(domain1));
        when(sysMenuDomainRepository.load(new MenuId(2L))).thenReturn(Mono.just(domain2));
        when(sysMenuDomainRepository.load(new MenuId(3L))).thenReturn(Mono.just(domain3));
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysMenuByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysMenuDomainRepository).load(new MenuId(1L));
        verify(sysMenuDomainRepository).load(new MenuId(2L));
        verify(sysMenuDomainRepository).load(new MenuId(3L));
    }

    @Test
    void restoreByIds_shouldSetDeleteStatusToFalse() {
        List<Long> ids = Collections.singletonList(1L);

        when(sysMenuDomainRepository.load(new MenuId(1L))).thenReturn(Mono.just(mockDomain));
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysMenuByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysMenuDomainRepository).save(argThat(domain -> domain.getDeleteStatus() == false));
    }

    @Test
    void restoreByIds_shouldContinueWhenDomainNotFound() {
        List<Long> ids = Arrays.asList(1L, 999L);

        SysMenuDomain domain1 = new SysMenuDomain();
        domain1.setMenuId(new MenuId(1L));
        domain1.setDeleteStatus(true);

        when(sysMenuDomainRepository.load(new MenuId(1L))).thenReturn(Mono.just(domain1));
        when(sysMenuDomainRepository.load(new MenuId(999L))).thenReturn(Mono.empty());
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysMenuByIdDomainService.restoreByIds(ids))
                .verifyComplete();

        verify(sysMenuDomainRepository).load(new MenuId(1L));
        verify(sysMenuDomainRepository).load(new MenuId(999L));
    }
}
