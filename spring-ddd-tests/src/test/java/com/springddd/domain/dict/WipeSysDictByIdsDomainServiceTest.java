package com.springddd.domain.dict;

import com.springddd.application.service.dict.WipeSysDictByIdsDomainServiceImpl;
import com.springddd.application.service.dict.dto.SysDictItemQuery;
import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysDictItemRepository;
import com.springddd.infrastructure.persistence.r2dbc.SysDictRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysDictByIdsDomainServiceTest {

    @Mock
    private SysDictRepository sysDictRepository;

    @Mock
    private SysDictItemRepository sysDictItemRepository;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @InjectMocks
    private WipeSysDictByIdsDomainServiceImpl wipeSysDictByIdsDomainService;

    @Test
    void deleteByIds_shouldDeleteAllByIds() {
        List<Long> ids = Arrays.asList(1L, 2L, 3L);

        SysDictItemEntity item1 = new SysDictItemEntity();
        item1.setId(100L);
        item1.setDictId(1L);

        SysDictItemEntity item2 = new SysDictItemEntity();
        item2.setId(200L);
        item2.setDictId(2L);

        when(r2dbcEntityTemplate.select(
                any(Query.class),
                eq(SysDictItemEntity.class)
        )).thenReturn(Flux.just(item1, item2));

        when(sysDictItemRepository.deleteAllById(any())).thenReturn(Mono.empty());
        when(sysDictRepository.deleteAllById(any())).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysDictByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(r2dbcEntityTemplate).select(any(Query.class), eq(SysDictItemEntity.class));
        verify(sysDictItemRepository).deleteAllById(any());
        verify(sysDictRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        StepVerifier.create(wipeSysDictByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(r2dbcEntityTemplate, never()).select(any(Query.class), eq(SysDictItemEntity.class));
        verify(sysDictItemRepository, never()).deleteAllById(any());
        verify(sysDictRepository, never()).deleteAllById(any());
    }

    @Test
    void deleteByIds_shouldDeleteMultipleItemsAndDicts() {
        List<Long> ids = Arrays.asList(1L, 2L);

        SysDictItemEntity item1 = new SysDictItemEntity();
        item1.setId(100L);
        item1.setDictId(1L);

        SysDictItemEntity item2 = new SysDictItemEntity();
        item2.setId(200L);
        item2.setDictId(2L);

        SysDictItemEntity item3 = new SysDictItemEntity();
        item3.setId(300L);
        item3.setDictId(1L);

        when(r2dbcEntityTemplate.select(
                any(Query.class),
                eq(SysDictItemEntity.class)
        )).thenReturn(Flux.just(item1, item2, item3));

        when(sysDictItemRepository.deleteAllById(any())).thenReturn(Mono.empty());
        when(sysDictRepository.deleteAllById(any())).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysDictByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(sysDictItemRepository).deleteAllById(any());
        verify(sysDictRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldDeleteDictEvenWhenNoItemsFound() {
        List<Long> ids = Arrays.asList(1L, 2L);

        when(r2dbcEntityTemplate.select(
                any(Query.class),
                eq(SysDictItemEntity.class)
        )).thenReturn(Flux.empty());

        when(sysDictItemRepository.deleteAllById(any())).thenReturn(Mono.empty());
        when(sysDictRepository.deleteAllById(any())).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysDictByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(r2dbcEntityTemplate).select(any(Query.class), eq(SysDictItemEntity.class));
        verify(sysDictRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldCallCorrectQueryWithDictIds() {
        List<Long> ids = Arrays.asList(1L, 2L);

        when(r2dbcEntityTemplate.select(
                any(Query.class),
                eq(SysDictItemEntity.class)
        )).thenReturn(Flux.empty());

        when(sysDictItemRepository.deleteAllById(any())).thenReturn(Mono.empty());
        when(sysDictRepository.deleteAllById(any())).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysDictByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        // Verify that the select query was called
        verify(r2dbcEntityTemplate).select(any(Query.class), eq(SysDictItemEntity.class));
    }
}
