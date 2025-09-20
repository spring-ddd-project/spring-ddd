package com.springddd.domain.dict;

import com.springddd.application.service.dict.WipeSysDictItemByIdsDomainServiceImpl;
import com.springddd.infrastructure.persistence.r2dbc.SysDictItemRepository;
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
class WipeSysDictItemByIdsDomainServiceTest {

    @Mock
    private SysDictItemRepository sysDictItemRepository;

    @InjectMocks
    private WipeSysDictItemByIdsDomainServiceImpl wipeSysDictItemByIdsDomainService;

    @Test
    void deleteByIds_shouldDeleteAllByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        when(sysDictItemRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysDictItemByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDictItemRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        when(sysDictItemRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysDictItemByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDictItemRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldHandleSingleId() {
        List<Long> ids = Collections.singletonList(1L);

        when(sysDictItemRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysDictItemByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDictItemRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldPropagateError() {
        List<Long> ids = Arrays.asList(1L, 2L);

        when(sysDictItemRepository.deleteAllById(ids)).thenReturn(Mono.error(new RuntimeException("Database error")));

        StepVerifier.create(wipeSysDictItemByIdsDomainService.deleteByIds(ids))
                .expectError(RuntimeException.class)
                .verify();

        verify(sysDictItemRepository).deleteAllById(ids);
    }
}
