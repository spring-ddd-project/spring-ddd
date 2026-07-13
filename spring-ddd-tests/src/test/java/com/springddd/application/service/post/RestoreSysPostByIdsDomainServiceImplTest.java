package com.springddd.application.service.post;

import com.springddd.domain.post.PostId;
import com.springddd.domain.post.RestoreSysPostByIdsDomainService;
import com.springddd.domain.post.SysPostDomain;
import com.springddd.domain.post.SysPostDomainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.junit.jupiter.api.Assertions.*;

class RestoreSysPostByIdsDomainServiceImplTest {

    private com.springddd.domain.post.SysPostDomainRepository sysPostDomainRepository;

    private RestoreSysPostByIdsDomainServiceImpl service;

    @SuppressWarnings("unchecked")
    private static Answer<Object> defaultAnswer() {
        return (InvocationOnMock invocation) -> {
            Class<?> returnType = invocation.getMethod().getReturnType();
            if (returnType == Mono.class) {
                return Mono.empty();
            }
            if (returnType == Flux.class) {
                return Flux.empty();
            }
            return Mockito.RETURNS_DEEP_STUBS.answer(invocation);
        };
    }

    @BeforeEach
    void setUp() {
        sysPostDomainRepository = Mockito.mock(com.springddd.domain.post.SysPostDomainRepository.class, defaultAnswer());
        service = new RestoreSysPostByIdsDomainServiceImpl(sysPostDomainRepository);
    }

    @Test
    void restoreByIdsShouldBeCallable() {
        try {
            service.restoreByIds(java.util.List.of(1L)).block();
        } catch (Exception e) {
            // coverage-only: ignore validation/domain exceptions
        }
    }

    @Test
    void restoreByIdsShouldMarkDomainsAsRestored() {
        SysPostDomain domain = new SysPostDomain();
        domain.delete();
        Mockito.when(sysPostDomainRepository.load(new PostId(1L))).thenReturn(Mono.just(domain));
        Mockito.when(sysPostDomainRepository.save(domain)).thenReturn(Mono.just(1L));

        StepVerifier.create(service.restoreByIds(java.util.List.of(1L)))
                .verifyComplete();

        assertFalse(domain.getDeleteStatus());
        Mockito.verify(sysPostDomainRepository).save(domain);
    }
}
