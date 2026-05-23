package com.springddd.application.service.role;

import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.SysRoleDomain;
import com.springddd.domain.role.SysRoleDomainRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestoreSysRoleByIdDomainServiceImplTest {

    @Mock
    private SysRoleDomainRepository sysRoleDomainRepository;

    @InjectMocks
    private RestoreSysRoleByIdDomainServiceImpl service;

    @Test
    @DisplayName("restoreByIds 应加载 domain 并调用 restore 和 save")
    void restoreByIds_shouldRestoreAndSave() {
        SysRoleDomain domain = mock(SysRoleDomain.class);
        when(sysRoleDomainRepository.load(new RoleId(1L))).thenReturn(Mono.just(domain));
        when(sysRoleDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.restoreByIds(List.of(1L)))
                .verifyComplete();

        verify(domain).restore();
        verify(sysRoleDomainRepository).save(domain);
    }
}
