package com.springddd.web;

import com.springddd.domain.util.ApiResponse;
import com.springddd.infrastructure.persistence.leaf.LeafSegmentBuffer;
import com.springddd.infrastructure.persistence.leaf.LeafSegmentIdGenerateDomainServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeafAllocControllerTest {

    @Mock
    private LeafSegmentIdGenerateDomainServiceImpl leafSegmentIdGenerateDomainServiceImpl;

    private LeafAllocController controller;

    @BeforeEach
    void setUp() {
        controller = new LeafAllocController(leafSegmentIdGenerateDomainServiceImpl);
    }

    @Test
    void getSegmentId_shouldReturnIdString() {
        when(leafSegmentIdGenerateDomainServiceImpl.nextId("test")).thenReturn(Mono.just(12345L));

        StepVerifier.create(controller.getSegmentId("test"))
                .expectNext("12345")
                .verifyComplete();
    }

    @Test
    void getCacheStatus_shouldReturnApiResponse() {
        LeafSegmentBuffer buffer = new LeafSegmentBuffer("test");
        buffer.getCurrent().setStep(100);
        buffer.getCurrent().setMax(200);
        buffer.getCurrent().setStart(100);
        buffer.getDisruptorLock().init(100, 200);
        buffer.setNextReady(true);
        buffer.getNext().setStep(100);
        buffer.getNext().setMax(300);
        buffer.getNext().setStart(200);

        java.util.Map<String, LeafSegmentBuffer> cache = new java.util.HashMap<>();
        cache.put("test", buffer);
        when(leafSegmentIdGenerateDomainServiceImpl.getBufferCache()).thenReturn(cache);

        StepVerifier.create(controller.getCacheStatus())
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertNotNull(response.getData());
                    @SuppressWarnings("unchecked")
                    java.util.Map<String, Object> data = (java.util.Map<String, Object>) response.getData();
                    @SuppressWarnings("unchecked")
                    java.util.List<java.util.Map<String, Object>> buffers = (java.util.List<java.util.Map<String, Object>>) data.get("buffers");
                    assertEquals(1, buffers.size());
                    assertEquals("test", buffers.get(0).get("bizTag"));
                    assertEquals(0, buffers.get(0).get("currentPos"));
                    assertEquals(true, buffers.get(0).get("initOk"));
                    assertEquals(true, buffers.get(0).get("nextReady"));
                })
                .verifyComplete();
    }

    @Test
    void getDbStatus_shouldReturnApiResponse() {
        when(leafSegmentIdGenerateDomainServiceImpl.findAllAlloc()).thenReturn(Mono.just(List.of()));

        StepVerifier.create(controller.getDbStatus())
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertNotNull(response.getData());
                })
                .verifyComplete();
    }
}
