package com.springddd.application.service.leaf;

import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import com.springddd.infrastructure.persistence.r2dbc.LeafAllocRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeafSegmentTransactionalServiceTest {

    @Mock
    private LeafAllocRepository leafAllocRepository;

    @InjectMocks
    private LeafSegmentTransactionalService leafSegmentTransactionalService;

    @Test
    @DisplayName("updateMaxIdAndGet 应更新并返回实体")
    void updateMaxIdAndGet_shouldUpdateAndReturnEntity() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setBizTag("test");
        entity.setMaxId(200L);

        when(leafAllocRepository.updateMaxIdByCustomStep("test", 100)).thenReturn(Mono.just(1));
        when(leafAllocRepository.findByBizTag("test")).thenReturn(Mono.just(entity));

        StepVerifier.create(leafSegmentTransactionalService.updateMaxIdAndGet("test", 100))
                .assertNext(result -> {
                    assertThat(result.getBizTag()).isEqualTo("test");
                    assertThat(result.getMaxId()).isEqualTo(200L);
                })
                .verifyComplete();

        verify(leafAllocRepository).updateMaxIdByCustomStep("test", 100);
        verify(leafAllocRepository).findByBizTag("test");
    }
}
