package com.springddd.webif.controller.leaf;

import com.springddd.application.service.leaf.LeafAllocCommandService;
import com.springddd.application.service.leaf.LeafAllocQueryService;
import com.springddd.application.service.leaf.dto.LeafAllocCommand;
import com.springddd.application.service.leaf.dto.LeafAllocPageQuery;
import com.springddd.application.service.leaf.dto.LeafAllocView;
import com.springddd.domain.util.ApiResponse;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.leaf.LeafSegmentBuffer;
import com.springddd.infrastructure.persistence.leaf.LeafSegmentIdGenerateDomainServiceImpl;
import com.springddd.web.leaf.LeafAllocController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeafAllocControllerTest {

    @Mock
    private LeafAllocCommandService leafAllocCommandService;
    @Mock
    private LeafAllocQueryService leafAllocQueryService;
    @Mock
    private LeafSegmentIdGenerateDomainServiceImpl leafSegmentIdGenerateDomainServiceImpl;

    private LeafAllocController controller;

    @BeforeEach
    void setUp() {
        controller = new LeafAllocController(leafAllocCommandService, leafAllocQueryService, leafSegmentIdGenerateDomainServiceImpl);
    }

    @Test
    void page_shouldReturnApiResponse() {
        LeafAllocPageQuery query = new LeafAllocPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        PageResponse<LeafAllocView> pageResponse = new PageResponse<>(List.of(), 0L, 1, 10);
        when(leafAllocQueryService.page(any())).thenReturn(Mono.just(pageResponse));

        StepVerifier.create(controller.page(Mono.just(query)))
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertNotNull(response.getData());
                })
                .verifyComplete();
    }

    @Test
    void recyclePage_shouldReturnApiResponse() {
        LeafAllocPageQuery query = new LeafAllocPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        PageResponse<LeafAllocView> pageResponse = new PageResponse<>(List.of(), 0L, 1, 10);
        when(leafAllocQueryService.recycle(any())).thenReturn(Mono.just(pageResponse));

        StepVerifier.create(controller.recyclePage(Mono.just(query)))
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertNotNull(response.getData());
                })
                .verifyComplete();
    }

    @Test
    void create_shouldReturnApiResponse() {
        LeafAllocCommand command = new LeafAllocCommand();
        when(leafAllocCommandService.create(any())).thenReturn(Mono.just(1L));

        StepVerifier.create(controller.create(command))
                .assertNext(response -> {
                    assertEquals(0, response.getCode());
                    assertEquals(1L, response.getData());
                })
                .verifyComplete();
    }

    @Test
    void update_shouldReturnApiResponse() {
        LeafAllocCommand command = new LeafAllocCommand();
        when(leafAllocCommandService.update(any())).thenReturn(Mono.empty());

        StepVerifier.create(controller.update(command))
                .assertNext(response -> assertEquals(0, response.getCode()))
                .verifyComplete();
    }

    @Test
    void delete_shouldReturnApiResponse() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(leafAllocCommandService.delete(any())).thenReturn(Mono.empty());

        StepVerifier.create(controller.delete(ids))
                .assertNext(response -> assertEquals(0, response.getCode()))
                .verifyComplete();
    }

    @Test
    void restore_shouldReturnApiResponse() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(leafAllocCommandService.restore(any())).thenReturn(Mono.empty());

        StepVerifier.create(controller.restore(ids))
                .assertNext(response -> assertEquals(0, response.getCode()))
                .verifyComplete();
    }

    @Test
    void wipe_shouldReturnApiResponse() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(leafAllocCommandService.wipe(any())).thenReturn(Mono.empty());

        StepVerifier.create(controller.wipe(ids))
                .assertNext(response -> assertEquals(0, response.getCode()))
                .verifyComplete();
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
        buffer.getCurrent().getValue().set(150);
        buffer.setNextReady(true);
        buffer.getNext().setStep(100);
        buffer.getNext().setMax(300);
        buffer.getNext().getValue().set(250);

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
