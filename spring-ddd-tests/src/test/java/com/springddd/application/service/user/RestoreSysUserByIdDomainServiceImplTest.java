package com.springddd.application.service.user;

import com.springddd.domain.user.SysUserDomain;
import com.springddd.domain.user.SysUserDomainRepository;
import com.springddd.domain.user.UserId;
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
class RestoreSysUserByIdDomainServiceImplTest {

    @Mock
    private SysUserDomainRepository sysUserDomainRepository;

    @InjectMocks
    private RestoreSysUserByIdDomainServiceImpl service;

    @Test
    @DisplayName("restoreByIds 应加载 domain 并调用 restore 和 save")
    void restoreByIds_shouldRestoreAndSave() {
        SysUserDomain domain = mock(SysUserDomain.class);
        when(sysUserDomainRepository.load(new UserId(1L))).thenReturn(Mono.just(domain));
        when(sysUserDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.restoreByIds(List.of(1L)))
                .verifyComplete();

        verify(domain).restore();
        verify(sysUserDomainRepository).save(domain);
    }
}
