package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.domain.user.*;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WipeSysUserByIdsDomainServiceImplTest {

    @Mock
    private SysUserRepository sysUserRepository;

    @Mock
    private SysUserRoleQueryService sysUserRoleQueryService;

    @Mock
    private SysUserRoleCommandService sysUserRoleCommandService;

    private WipeSysUserByIdsDomainServiceImpl wipeSysUserByIdsDomainService;

    @BeforeEach
    void setUp() {
        wipeSysUserByIdsDomainService = new WipeSysUserByIdsDomainServiceImpl(
                sysUserRepository,
                sysUserRoleQueryService,
                sysUserRoleCommandService
        );
    }

    @Test
    void deleteByIds_shouldComplete_whenValidIds() {
        List<Long> ids = Arrays.asList(1L);
        when(sysUserRoleQueryService.queryLinkUserAndRole(any())).thenReturn(Mono.just(Collections.emptyList()));
        when(sysUserRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysUserByIdsDomainService.deleteByIds(ids))
                .verifyComplete();
    }

    @Test
    void deleteByIds_shouldWipeUserRoles_whenRolesExist() {
        List<Long> ids = Arrays.asList(1L);
        SysUserRoleView view = new SysUserRoleView();
        view.setId(1L);
        view.setUserId(1L);
        view.setRoleId(2L);

        when(sysUserRoleQueryService.queryLinkUserAndRole(1L)).thenReturn(Mono.just(Collections.singletonList(view)));
        when(sysUserRoleCommandService.wipe(any())).thenReturn(Mono.empty());
        when(sysUserRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysUserByIdsDomainService.deleteByIds(ids))
                .verifyComplete();
    }
}
