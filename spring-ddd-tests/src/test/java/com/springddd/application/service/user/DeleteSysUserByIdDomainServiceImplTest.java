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
class DeleteSysUserByIdDomainServiceImplTest {

    @Mock
    private SysUserDomainRepository sysUserDomainRepository;

    @InjectMocks
    private DeleteSysUserByIdDomainServiceImpl service;

    @Test
    @DisplayName("deleteByIds 应加载 domain 并调用 delete 和 save")
    void deleteByIds_shouldDeleteAndSave() {
        SysUserDomain domain = mock(SysUserDomain.class);
        when(sysUserDomainRepository.load(new UserId(1L))).thenReturn(Mono.just(domain));
        when(sysUserDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();

        verify(domain).delete();
        verify(sysUserDomainRepository).save(domain);
    }
}
