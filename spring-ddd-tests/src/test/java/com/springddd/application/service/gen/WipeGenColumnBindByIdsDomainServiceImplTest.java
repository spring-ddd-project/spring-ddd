package com.springddd.application.service.gen;

import com.springddd.application.service.gen.WipeGenColumnBindByIdsDomainServiceImpl;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnBindRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeGenColumnBindByIdsDomainServiceImplTest {

    @Mock
    private GenColumnBindRepository genColumnBindRepository;

    @InjectMocks
    private WipeGenColumnBindByIdsDomainServiceImpl wipeGenColumnBindByIdsDomainService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void wipeByIds_shouldDeleteColumnBinds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(genColumnBindRepository.deleteAllById(ids)).thenReturn(Mono.empty().then());

        StepVerifier.create(wipeGenColumnBindByIdsDomainService.wipeByIds(ids))
                .verifyComplete();

        verify(genColumnBindRepository).deleteAllById(ids);
    }

    @Test
    void wipeByIds_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        when(genColumnBindRepository.deleteAllById(ids)).thenReturn(Mono.empty().then());

        StepVerifier.create(wipeGenColumnBindByIdsDomainService.wipeByIds(ids))
                .verifyComplete();

        verify(genColumnBindRepository).deleteAllById(ids);
    }
}
