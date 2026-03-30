package com.springddd.application.service.role;

import com.springddd.domain.role.LinkRoleAndMenusDomainService;
import com.springddd.domain.role.WipeSysRoleMenuByIdsDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysRoleMenuCommandServiceTest {

    @Mock
    private LinkRoleAndMenusDomainService linkRoleAndMenusDomainService;

    @Mock
    private WipeSysRoleMenuByIdsDomainService wipeSysRoleMenuByIdsDomainService;

    private SysRoleMenuCommandService sysRoleMenuCommandService;

    @BeforeEach
    void setUp() {
        sysRoleMenuCommandService = new SysRoleMenuCommandService(
                linkRoleAndMenusDomainService,
                wipeSysRoleMenuByIdsDomainService
        );
    }

    @Test
    void create_shouldDelegateToDomainService() {
        Long roleId = 1L;
        List<Long> menuIds = Arrays.asList(1L, 2L, 3L);

        when(linkRoleAndMenusDomainService.link(roleId, menuIds)).thenReturn(Mono.empty());

        Mono<Void> result = sysRoleMenuCommandService.create(roleId, menuIds);

        StepVerifier.create(result)
                .verifyComplete();
    }

    @Test
    void wipe_shouldDelegateToDomainService() {
        List<Long> ids = Arrays.asList(1L, 2L);

        when(wipeSysRoleMenuByIdsDomainService.deleteByIds(ids)).thenReturn(Mono.empty());

        Mono<Void> result = sysRoleMenuCommandService.wipe(ids);

        StepVerifier.create(result)
                .verifyComplete();
    }
}
