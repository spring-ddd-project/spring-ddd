package com.springddd.application.service.gen;

import com.springddd.application.service.gen.WipeGenColumnsByIdsDomainServiceImpl;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnsRepository;
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
class WipeGenColumnsByIdsDomainServiceImplTest {

    @Mock
    private GenColumnsRepository genColumnsRepository;

    @InjectMocks
    private WipeGenColumnsByIdsDomainServiceImpl wipeGenColumnsByIdsDomainService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void wipeByIds_shouldDeleteColumns() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(genColumnsRepository.deleteAllById(ids)).thenReturn(Mono.empty().then());

        StepVerifier.create(wipeGenColumnsByIdsDomainService.wipeByIds(ids))
                .verifyComplete();

        verify(genColumnsRepository).deleteAllById(ids);
    }

    @Test
    void wipeByIds_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        when(genColumnsRepository.deleteAllById(ids)).thenReturn(Mono.empty().then());

        StepVerifier.create(wipeGenColumnsByIdsDomainService.wipeByIds(ids))
                .verifyComplete();

        verify(genColumnsRepository).deleteAllById(ids);
    }
}
