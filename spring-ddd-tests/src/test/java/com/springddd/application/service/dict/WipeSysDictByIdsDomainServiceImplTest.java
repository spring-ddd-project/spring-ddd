package com.springddd.application.service.dict;

import com.springddd.application.service.dict.WipeSysDictByIdsDomainServiceImpl;
import com.springddd.application.service.dict.dto.SysDictItemQuery;
import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysDictItemRepository;
import com.springddd.infrastructure.persistence.r2dbc.SysDictRepository;
import org.junit.jupiter.api.BeforeEach;
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
class WipeSysDictByIdsDomainServiceImplTest {

    @Mock
    private SysDictRepository sysDictRepository;

    @Mock
    private SysDictItemRepository sysDictItemRepository;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @InjectMocks
    private WipeSysDictByIdsDomainServiceImpl wipeSysDictByIdsDomainService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void deleteByIds_shouldDeleteDictAndRelatedItems() {
        List<Long> ids = Arrays.asList(1L, 2L);

        SysDictItemEntity itemEntity = new SysDictItemEntity();
        itemEntity.setId(100L);
        itemEntity.setDictId(1L);

        when(r2dbcEntityTemplate.select(any(Query.class), eq(SysDictItemEntity.class)))
                .thenReturn(Flux.just(itemEntity));
        when(sysDictItemRepository.deleteAllById(anyList())).thenReturn(Mono.empty().then());
        when(sysDictRepository.deleteAllById(ids)).thenReturn(Mono.empty().then());

        StepVerifier.create(wipeSysDictByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(r2dbcEntityTemplate).select(any(Query.class), eq(SysDictItemEntity.class));
        verify(sysDictItemRepository).deleteAllById(anyList());
        verify(sysDictRepository).deleteAllById(ids);
    }

    @Test
    void deleteByIds_shouldHandleEmptyList() {
        List<Long> ids = Collections.emptyList();

        StepVerifier.create(wipeSysDictByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(r2dbcEntityTemplate, never()).select(any(Query.class), eq(SysDictItemEntity.class));
        verify(sysDictItemRepository, never()).deleteAllById(anyList());
        verify(sysDictRepository, never()).deleteAllById(anyList());
    }

    @Test
    void deleteByIds_shouldHandleNoItemsFound() {
        List<Long> ids = Arrays.asList(1L);

        when(r2dbcEntityTemplate.select(any(Query.class), eq(SysDictItemEntity.class)))
                .thenReturn(Flux.empty());
        when(sysDictItemRepository.deleteAllById(anyList())).thenReturn(Mono.empty().then());
        when(sysDictRepository.deleteAllById(ids)).thenReturn(Mono.empty().then());

        StepVerifier.create(wipeSysDictByIdsDomainService.deleteByIds(ids))
                .verifyComplete();

        verify(r2dbcEntityTemplate).select(any(Query.class), eq(SysDictItemEntity.class));
        verify(sysDictItemRepository).deleteAllById(Collections.emptyList());
        verify(sysDictRepository).deleteAllById(ids);
    }
}
