package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuQuery;
import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.menu.DeleteSysMenuByIdDomainService;
import com.springddd.domain.menu.MenuId;
import com.springddd.domain.menu.SysMenuDomain;
import com.springddd.domain.menu.SysMenuDomainRepository;
import com.springddd.domain.util.ReactiveTreeUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteSysMenuByIdDomainServiceImplTest {

    @Mock
    private SysMenuDomainRepository sysMenuDomainRepository;

    @Mock
    private SysMenuQueryService sysMenuQueryService;

    private DeleteSysMenuByIdDomainServiceImpl deleteSysMenuByIdDomainService;

    @BeforeEach
    void setUp() {
        deleteSysMenuByIdDomainService = new DeleteSysMenuByIdDomainServiceImpl(
                sysMenuDomainRepository,
                sysMenuQueryService
        );
    }

    @Test
    void shouldDeleteByIdsSuccessfully() {
        Long menuId = 1L;
        SysMenuView menuView = new SysMenuView();
        menuView.setId(menuId);
        menuView.setParentId(0L);

        SysMenuDomain domain = new SysMenuDomain();

        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(Arrays.asList(menuView)));
        when(sysMenuDomainRepository.load(any(MenuId.class))).thenReturn(Mono.just(domain));
        when(sysMenuDomainRepository.save(any(SysMenuDomain.class))).thenReturn(Mono.just(1L));
        when(SecurityUtils.concurrency()).thenReturn(reactor.util.concurrent.Queues.<SysMenuDomain>unbounded(16).toContextWrite());

        List<Long> ids = Arrays.asList(menuId);

        Mono<Void> result = deleteSysMenuByIdDomainService.deleteByIds(ids);

        StepVerifier.create(result)
                .verifyComplete();

        verify(sysMenuQueryService).queryAllMenu();
    }

    @Test
    void shouldReturnEmptyWhenIdsIsNull() {
        Mono<Void> result = deleteSysMenuByIdDomainService.deleteByIds(null);

        StepVerifier.create(result)
                .verifyComplete();

        verifyNoInteractions(sysMenuQueryService);
    }

    @Test
    void shouldReturnEmptyWhenIdsIsEmpty() {
        Mono<Void> result = deleteSysMenuByIdDomainService.deleteByIds(Arrays.asList());

        StepVerifier.create(result)
                .verifyComplete();

        verifyNoInteractions(sysMenuQueryService);
    }
}
