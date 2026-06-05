package com.springddd.application.service.dept;

import com.springddd.domain.dept.*;
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
class RestoreSysDeptByIdDomainServiceImplTest {

    @Mock
    private SysDeptDomainRepository sysDeptDomainRepository;

    private RestoreSysDeptByIdDomainServiceImpl restoreSysDeptByIdDomainService;

    @BeforeEach
    void setUp() {
        restoreSysDeptByIdDomainService = new RestoreSysDeptByIdDomainServiceImpl(sysDeptDomainRepository);
    }

    @Test
    void restoreByIds_shouldComplete_whenValidIds() {
        List<Long> ids = Arrays.asList(1L);
        SysDeptDomain mockDomain = new SysDeptDomain();
        mockDomain.setDeleteStatus(true);
        when(sysDeptDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(sysDeptDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysDeptByIdDomainService.restoreByIds(ids))
                .verifyComplete();
    }
}
