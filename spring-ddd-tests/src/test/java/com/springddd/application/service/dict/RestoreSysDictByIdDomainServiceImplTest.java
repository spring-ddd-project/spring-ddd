package com.springddd.application.service.dict;

import com.springddd.domain.dict.*;
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
class RestoreSysDictByIdDomainServiceImplTest {

    @Mock
    private SysDictDomainRepository sysDictDomainRepository;

    private RestoreSysDictByIdDomainServiceImpl restoreSysDictByIdDomainService;

    @BeforeEach
    void setUp() {
        restoreSysDictByIdDomainService = new RestoreSysDictByIdDomainServiceImpl(sysDictDomainRepository);
    }

    @Test
    void restoreByIds_shouldComplete_whenValidIds() {
        List<Long> ids = Arrays.asList(1L);
        SysDictDomain mockDomain = new SysDictDomain();
        mockDomain.setDeleteStatus(true);
        when(sysDictDomainRepository.load(any())).thenReturn(Mono.just(mockDomain));
        when(sysDictDomainRepository.save(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(restoreSysDictByIdDomainService.restoreByIds(ids))
                .verifyComplete();
    }
}
