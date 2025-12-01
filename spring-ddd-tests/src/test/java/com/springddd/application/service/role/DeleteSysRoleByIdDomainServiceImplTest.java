package com.springddd.application.service.role;

import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.role.RoleId;
import com.springddd.domain.role.SysRoleDomain;
import com.springddd.domain.role.SysRoleDomainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteSysRoleByIdDomainServiceImplTest {

    @Mock
    private SysRoleDomainRepository sysRoleDomainRepository;

    private DeleteSysRoleByIdDomainServiceImpl deleteSysRoleByIdDomainServiceImpl;

    @BeforeEach
    void setUp() {
        deleteSysRoleByIdDomainServiceImpl = new DeleteSysRoleByIdDomainServiceImpl(sysRoleDomainRepository);
    }

    @Test
    void deleteByIds_shouldComplete_whenValidIds() {
        SysRoleDomain mockDomain = new SysRoleDomain();
        when(sysRoleDomainRepository.load(any(RoleId.class))).thenReturn(Mono.just(mockDomain));
        when(sysRoleDomainRepository.save(any())).thenReturn(Mono.just(1L));

        Mono<Void> result = deleteSysRoleByIdDomainServiceImpl.deleteByIds(Arrays.asList(1L, 2L));

        StepVerifier.create(result)
                .verifyComplete();
    }
}
