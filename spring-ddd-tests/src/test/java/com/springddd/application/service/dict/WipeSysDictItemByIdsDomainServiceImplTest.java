package com.springddd.application.service.dict;

import com.springddd.infrastructure.persistence.r2dbc.SysDictItemRepository;
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
class WipeSysDictItemByIdsDomainServiceImplTest {

    @Mock
    private SysDictItemRepository sysDictItemRepository;

    private WipeSysDictItemByIdsDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new WipeSysDictItemByIdsDomainServiceImpl(sysDictItemRepository);
    }

    @Test
    void deleteByIds_shouldComplete_whenValidIds() {
        when(sysDictItemRepository.deleteAllById(any(List.class))).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();
    }
}
