package com.springddd.application.service.gen;

import com.springddd.application.service.gen.WipeGenProjectInfoByIdsDomainServiceImpl;
import com.springddd.infrastructure.persistence.r2dbc.GenProjectInfoRepository;
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
class WipeGenProjectInfoByIdsDomainServiceImplTest {

    @Mock
    private GenProjectInfoRepository genProjectInfoRepository;

    @InjectMocks
    private WipeGenProjectInfoByIdsDomainServiceImpl wipeGenProjectInfoByIdsDomainService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void wipeByIds_shouldDeleteProjectInfos() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(genProjectInfoRepository.deleteAllById(ids)).thenReturn(Mono.empty().then());

        StepVerifier.create(wipeGenProjectInfoByIdsDomainService.wipeByIds(ids))
                .verifyComplete();

        verify(genProjectInfoRepository).deleteAllById(ids);
    }

    @Test
    void wipeByIds_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        when(genProjectInfoRepository.deleteAllById(ids)).thenReturn(Mono.empty().then());

        StepVerifier.create(wipeGenProjectInfoByIdsDomainService.wipeByIds(ids))
                .verifyComplete();

        verify(genProjectInfoRepository).deleteAllById(ids);
    }
}
