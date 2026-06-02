package com.springddd.application.service.role;

import com.springddd.infrastructure.persistence.r2dbc.SysRoleMenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WipeSysRoleMenuByIdsDomainServiceImplTest {

    @Mock
    private SysRoleMenuRepository sysRoleMenuRepository;

    private WipeSysRoleMenuByIdsDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new WipeSysRoleMenuByIdsDomainServiceImpl(sysRoleMenuRepository);
    }

    @Test
    void deleteByIds_shouldComplete_whenValidIds() {
        when(sysRoleMenuRepository.deleteAllById(any(List.class))).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();
    }
}
