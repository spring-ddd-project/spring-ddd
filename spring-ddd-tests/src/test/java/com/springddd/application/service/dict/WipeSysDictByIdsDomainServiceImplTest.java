package com.springddd.application.service.dict;

import com.springddd.domain.dict.*;
import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysDictItemRepository;
import com.springddd.infrastructure.persistence.r2dbc.SysDictRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveSelectOperation;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class WipeSysDictByIdsDomainServiceImplTest {

    @Mock
    private SysDictRepository sysDictRepository;

    @Mock
    private SysDictItemRepository sysDictItemRepository;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysDictItemEntity> reactiveSelect;

    private WipeSysDictByIdsDomainServiceImpl wipeSysDictByIdsDomainService;

    @BeforeEach
    void setUp() {
        wipeSysDictByIdsDomainService = new WipeSysDictByIdsDomainServiceImpl(
                sysDictRepository,
                sysDictItemRepository,
                r2dbcEntityTemplate
        );
    }

    @Test
    void deleteByIds_shouldComplete_whenValidIds() {
        List<Long> ids = Arrays.asList(1L);
        when(r2dbcEntityTemplate.select(any(Query.class), any())).thenReturn(Flux.empty());
        when(sysDictItemRepository.deleteAllById(any())).thenReturn(Mono.empty());
        when(sysDictRepository.deleteAllById(ids)).thenReturn(Mono.empty());

        StepVerifier.create(wipeSysDictByIdsDomainService.deleteByIds(ids))
                .verifyComplete();
    }

    @Test
    void deleteByIds_shouldReturnEmpty_whenIdsEmpty() {
        StepVerifier.create(wipeSysDictByIdsDomainService.deleteByIds(Collections.emptyList()))
                .verifyComplete();
    }
}
