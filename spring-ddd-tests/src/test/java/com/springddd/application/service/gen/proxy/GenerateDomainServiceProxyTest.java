package com.springddd.application.service.gen.proxy;

import com.springddd.domain.gen.GenerateDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenerateDomainServiceProxyTest {

    @Mock
    private GenerateDomainService targetService;

    @InjectMocks
    private GenerateDomainServiceProxy proxy;

    @Test
    @DisplayName("generate 应委托给目标服务并记录耗时")
    void generate_shouldDelegateToTargetService() {
        when(targetService.generate("sys_user")).thenReturn(Mono.empty());

        StepVerifier.create(proxy.generate("sys_user"))
                .verifyComplete();

        verify(targetService).generate("sys_user");
    }
}
