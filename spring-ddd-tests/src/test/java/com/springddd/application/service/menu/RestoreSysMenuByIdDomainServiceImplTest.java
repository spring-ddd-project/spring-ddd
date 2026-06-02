package com.springddd.application.service.menu;

import com.springddd.domain.menu.*;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestoreSysMenuByIdDomainServiceImplTest {

    @Mock
    private SysMenuDomainRepository sysMenuDomainRepository;

    private RestoreSysMenuByIdDomainServiceImpl restoreSysMenuByIdDomainService;

    @BeforeEach
    void setUp() {
        restoreSysMenuByIdDomainService = new RestoreSysMenuByIdDomainServiceImpl(sysMenuDomainRepository);
    }

    @Test
    void restoreByIds_shouldComplete_whenValidIds() {
        List<Long> ids = Arrays.asList(1L);
        SysMenuDomain mockDomain = new SysMenuDomain();
        when(sysMenuDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(sysMenuDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysMenuByIdDomainService.restoreByIds(ids))
                .verifyComplete();
    }
}
