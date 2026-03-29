package com.springddd.domain.menu;

import com.springddd.application.service.menu.WipeSysMenuByIdsDomainServiceImpl;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysMenuByIdDomainServiceTest {

    @Mock
    private SysMenuRepository sysMenuRepository;

    @InjectMocks
    private WipeSysMenuByIdsDomainServiceImpl domainService;

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        StepVerifier.create(domainService.deleteByIds(Arrays.asList()))
                .verifyComplete();

        verify(sysMenuRepository, never()).deleteAllById(anyIterable());
    }
}
