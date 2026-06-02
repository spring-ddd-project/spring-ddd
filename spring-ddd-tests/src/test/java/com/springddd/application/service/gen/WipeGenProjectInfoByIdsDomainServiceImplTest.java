package com.springddd.application.service.gen;

import com.springddd.infrastructure.persistence.r2dbc.GenProjectInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WipeGenProjectInfoByIdsDomainServiceImplTest {

    @Mock
    private GenProjectInfoRepository genProjectInfoRepository;

    private WipeGenProjectInfoByIdsDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new WipeGenProjectInfoByIdsDomainServiceImpl(genProjectInfoRepository);
    }

    @Test
    void wipeByIds_shouldComplete_whenValidIds() {
        when(genProjectInfoRepository.deleteAllById(any(List.class))).thenReturn(Mono.empty());

        StepVerifier.create(service.wipeByIds(List.of(1L)))
                .verifyComplete();
    }
}
