package com.springddd.application.service.dict;

import com.springddd.infrastructure.persistence.r2dbc.SysDictItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WipeSysDictItemByIdsDomainServiceImplTest {

    @Mock
    private SysDictItemRepository sysDictItemRepository;

    @InjectMocks
    private WipeSysDictItemByIdsDomainServiceImpl service;

    @Test
    @DisplayName("deleteByIds 应物理删除字典项")
    void deleteByIds_shouldWipeDictItems() {
        when(sysDictItemRepository.deleteAllById(anyList())).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteByIds(List.of(1L)))
                .verifyComplete();

        verify(sysDictItemRepository).deleteAllById(List.of(1L));
    }
}
