package com.springddd.application.service.user;

import com.springddd.infrastructure.persistence.r2dbc.SysUserRoleRepository;
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
class WipeSysUserRoleByIdsDomainServiceImplTest {

    @Mock
    private SysUserRoleRepository sysUserRoleRepository;

    private WipeSysUserRoleByIdsDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new WipeSysUserRoleByIdsDomainServiceImpl(sysUserRoleRepository);
    }

    @Test
    void deleteByIds_shouldComplete_whenValidIds() {
        when(sysUserRoleRepository.deleteAllById(any(List.class))).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();
    }
}
