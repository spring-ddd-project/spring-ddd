package com.springddd.application.service.menu;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.menu.SysMenuDomain;
import com.springddd.domain.menu.SysMenuDomainRepository;
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
class RestoreSysMenuByIdDomainServiceImplTest {

    @Mock
    private SysMenuDomainRepository sysMenuDomainRepository;

    @InjectMocks
    private RestoreSysMenuByIdDomainServiceImpl service;

    @Test
    @DisplayName("restoreByIds 应加载 domain 并调用 restore 和 save")
    void restoreByIds_shouldRestoreAndSave() {
        SysMenuDomain domain = mock(SysMenuDomain.class);
        when(sysMenuDomainRepository.load(new MenuId(1L))).thenReturn(Mono.just(domain));
        when(sysMenuDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.restoreByIds(List.of(1L)))
                .verifyComplete();

        verify(domain).restore();
        verify(sysMenuDomainRepository).save(domain);
    }
}
