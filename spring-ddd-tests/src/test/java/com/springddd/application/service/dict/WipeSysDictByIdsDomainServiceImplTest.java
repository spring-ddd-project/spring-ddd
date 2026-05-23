package com.springddd.application.service.dict;

import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysDictItemRepository;
import com.springddd.infrastructure.persistence.r2dbc.SysDictRepository;
import org.junit.jupiter.api.DisplayName;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
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
    private WipeSysDictByIdsDomainServiceImpl service;

    @Test
    @DisplayName("deleteByIds 应删除字典及其字典项")
    void deleteByIds_shouldWipeDictsAndItems() {
        SysDictItemEntity item = new SysDictItemEntity();
        item.setId(10L);

        when(r2dbcEntityTemplate.select(any(Query.class), eq(SysDictItemEntity.class))).thenReturn(Flux.just(item));
        when(sysDictItemRepository.deleteAllById(anyList())).thenReturn(Mono.empty());
        when(sysDictRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();

        verify(sysDictItemRepository).deleteAllById(List.of(10L));
        verify(sysDictRepository).deleteAllById(List.of(1L));
    }

    @Test
    @DisplayName("deleteByIds 应处理空列表")
    void deleteByIds_shouldHandleEmptyList() {
        StepVerifier.create(service.deleteByIds(List.of()))
                .verifyComplete();

        verify(r2dbcEntityTemplate, never()).select(any(), any());
    }
}
