package com.springddd.application.service.user;

import com.springddd.domain.user.*;
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
class DeleteSysUserByIdDomainServiceImplTest {

    @Mock
    private SysUserDomainRepository sysUserDomainRepository;

    private DeleteSysUserByIdDomainServiceImpl deleteSysUserByIdDomainService;

    @BeforeEach
    void setUp() {
        deleteSysUserByIdDomainService = new DeleteSysUserByIdDomainServiceImpl(sysUserDomainRepository);
    }

    @Test
    void deleteByIds_shouldComplete_whenValidIds() {
        List<Long> ids = Arrays.asList(1L);
        SysUserDomain mockDomain = new SysUserDomain();
        when(sysUserDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(sysUserDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(deleteSysUserByIdDomainService.deleteByIds(ids))
                .verifyComplete();
    }
}
