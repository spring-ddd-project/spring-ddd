package com.springddd.application.service.role;

import com.springddd.domain.role.*;
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
class RestoreSysRoleByIdDomainServiceImplTest {

    @Mock
    private SysRoleDomainRepository sysRoleDomainRepository;

    private RestoreSysRoleByIdDomainServiceImpl restoreSysRoleByIdDomainService;

    @BeforeEach
    void setUp() {
        restoreSysRoleByIdDomainService = new RestoreSysRoleByIdDomainServiceImpl(sysRoleDomainRepository);
    }

    @Test
    void restoreByIds_shouldComplete_whenValidIds() {
        List<Long> ids = Arrays.asList(1L);
        SysRoleDomain mockDomain = new SysRoleDomain();
        when(sysRoleDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(sysRoleDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysRoleByIdDomainService.restoreByIds(ids))
                .verifyComplete();
    }
}
