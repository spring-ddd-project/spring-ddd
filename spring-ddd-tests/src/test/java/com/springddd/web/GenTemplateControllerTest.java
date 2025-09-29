package com.springddd.web;

import com.springddd.application.service.gen.GenTemplateCommandService;
import com.springddd.application.service.gen.GenTemplateQueryService;
import com.springddd.application.service.gen.dto.GenTemplateCommand;
import com.springddd.application.service.gen.dto.GenTemplatePageQuery;
import com.springddd.domain.util.ApiResponse;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenTemplateControllerTest {

    @Mock
    private GenTemplateQueryService genTemplateQueryService;

    @Mock
    private GenTemplateCommandService genTemplateCommandService;

    private GenTemplateController genTemplateController;

    @BeforeEach
    void setUp() {
        genTemplateController = new GenTemplateController(genTemplateQueryService, genTemplateCommandService);
    }

    @Test
    void shouldCreateTemplateSuccessfully() {
        GenTemplateCommand command = new GenTemplateCommand();
        command.setTemplateName("test-template");
        command.setTemplateContent("template content");

        when(genTemplateCommandService.create(any(GenTemplateCommand.class))).thenReturn(Mono.just(1L));

        Mono<ApiResponse> result = genTemplateController.create(command);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(200, response.getCode());
                })
                .verifyComplete();

        verify(genTemplateCommandService).create(any(GenTemplateCommand.class));
    }

    @Test
    void shouldUpdateTemplateSuccessfully() {
        GenTemplateCommand command = new GenTemplateCommand();
        command.setId(1L);
        command.setTemplateName("updated-template");
        command.setTemplateContent("updated content");

        when(genTemplateCommandService.update(any(GenTemplateCommand.class))).thenReturn(Mono.empty());

        Mono<ApiResponse> result = genTemplateController.update(command);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(200, response.getCode());
                })
                .verifyComplete();

        verify(genTemplateCommandService).update(any(GenTemplateCommand.class));
    }

    @Test
    void shouldDeleteTemplatesSuccessfully() {
        List<Long> ids = Arrays.asList(1L, 2L);

        when(genTemplateCommandService.delete(ids)).thenReturn(Mono.empty());

        Mono<ApiResponse> result = genTemplateController.delete(ids);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(200, response.getCode());
                })
                .verifyComplete();

        verify(genTemplateCommandService).delete(ids);
    }

    @Test
    void shouldRestoreTemplatesSuccessfully() {
        List<Long> ids = Arrays.asList(1L, 2L);

        when(genTemplateCommandService.restore(ids)).thenReturn(Mono.empty());

        Mono<ApiResponse> result = genTemplateController.restore(ids);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(200, response.getCode());
                })
                .verifyComplete();

        verify(genTemplateCommandService).restore(ids);
    }

    @Test
    void shouldWipeTemplatesSuccessfully() {
        List<Long> ids = Arrays.asList(1L, 2L);

        when(genTemplateCommandService.wipe(ids)).thenReturn(Mono.empty());

        Mono<ApiResponse> result = genTemplateController.wipe(ids);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(200, response.getCode());
                })
                .verifyComplete();

        verify(genTemplateCommandService).wipe(ids);
    }
}
