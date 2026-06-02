package com.springddd.application.service.menu;

import com.springddd.application.service.menu.dto.SysMenuView;
import com.springddd.domain.menu.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
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
    void deleteByIds_shouldComplete_whenValidIds() {
        List<Long> ids = Arrays.asList(1L);
        SysMenuView view = new SysMenuView();
        view.setId(1L);
        view.setParentId(0L);

        SysMenuDomain mockDomain = new SysMenuDomain();
        when(sysMenuQueryService.queryAllMenu()).thenReturn(Mono.just(Collections.singletonList(view)));
        when(sysMenuDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(sysMenuDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysMenuByIdDomainService.deleteByIds(ids))
                .verifyComplete();
    }

    @Test
    void deleteByIds_shouldReturnEmpty_whenIdsEmpty() {
        StepVerifier.create(deleteSysMenuByIdDomainService.deleteByIds(Collections.emptyList()))
                .verifyComplete();
    }
}
