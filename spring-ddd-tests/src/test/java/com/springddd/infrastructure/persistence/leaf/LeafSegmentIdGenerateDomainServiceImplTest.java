package com.springddd.infrastructure.persistence.leaf;

import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import com.springddd.infrastructure.persistence.r2dbc.LeafAllocRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LeafSegmentIdGenerateDomainServiceImplTest {

    @Mock
    private LeafAllocRepository leafAllocRepository;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    private LeafSegmentIdGenerateDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = spy(new LeafSegmentIdGenerateDomainServiceImpl(leafAllocRepository, r2dbcEntityTemplate));
    }

    @Test
    void nextId_shouldReturnId_whenBufferNotInitialized() {
        doReturn(Mono.empty()).when(service).updateMaxIdFromDb(any(), anyString(), anyBoolean());

        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().getValue().set(150);
        service.getBufferCache().put("test", buffer);

        StepVerifier.create(service.nextId("test"))
                .expectNext(150L)
                .verifyComplete();
    }

    @Test
    void nextId_shouldReturnId_whenBufferInitializedAndIdInRange() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().getValue().set(150);
        buffer.getThreadRunning().set(true);
        service.getBufferCache().put("test", buffer);

        StepVerifier.create(service.nextId("test"))
                .expectNext(150L)
                .verifyComplete();
    }

    @Test
    void nextId_shouldSwitchSegment_whenNextReady() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().getValue().set(200);
        buffer.setNextReady(true);
        buffer.getNext().setStep(100);
        buffer.getNext().setMax(300);
        buffer.getNext().getValue().set(250);
        buffer.getThreadRunning().set(true);
        service.getBufferCache().put("test", buffer);

        StepVerifier.create(service.nextId("test"))
                .expectNext(250L)
                .verifyComplete();

        assertFalse(buffer.isNextReady());
        assertEquals(1, buffer.getCurrentPos());
    }

    @Test
    void nextId_shouldThrowException_whenNextBufferNotReadyAndTimeout() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().getValue().set(200);
        buffer.setNextReady(false);
        buffer.getThreadRunning().set(true);
        service.getBufferCache().put("test", buffer);

        StepVerifier.create(service.nextId("test"))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void getBufferCache_shouldReturnMap() {
        Map<String, LeafSegmentBuffer> cache = service.getBufferCache();
        assertNotNull(cache);
    }

    @Test
    void findAllAlloc_shouldDelegateToRepository() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setId(1L);
        when(leafAllocRepository.findAll()).thenReturn(Flux.just(entity));

        StepVerifier.create(service.findAllAlloc())
                .assertNext(list -> {
                    assertEquals(1, list.size());
                    assertEquals(1L, list.get(0).getId());
                })
                .verifyComplete();
    }

    @Test
    void init_shouldLoadAllTags() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setBizTag("test_tag");
        when(leafAllocRepository.findAll()).thenReturn(Flux.just(entity));

        service.init();

        assertTrue(service.getBufferCache().containsKey("test_tag"));
    }

}
