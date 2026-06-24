package com.springddd.web;

import com.springddd.application.service.gen.GenTableInfoCommandService;
import com.springddd.application.service.gen.GenTableInfoQueryService;
import com.springddd.application.service.gen.dto.GenTableInfoPageQuery;
import com.springddd.application.service.gen.dto.GenTableInfoPageResponse;
import com.springddd.domain.util.ApiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenTableControllerTest {

    @Mock
    private GenTableInfoQueryService genTableInfoQueryService;

    @Mock
    private GenTableInfoCommandService genTableInfoCommandService;

    @InjectMocks
    private GenTableController genTableController;

    @Test
    void tableIndex_shouldReturnDatabaseNameInPageResponse() {
        GenTableInfoPageResponse pageResponse = new GenTableInfoPageResponse(
                Collections.emptyList(),
                0L,
                1,
                10,
                "test_ddd"
        );
        when(genTableInfoQueryService.index(any(GenTableInfoPageQuery.class))).thenReturn(Mono.just(pageResponse));

        Mono<ApiResponse> result = genTableController.tableIndex(Mono.just(new GenTableInfoPageQuery()));

        StepVerifier.create(result)
                .assertNext(apiResponse -> {
                    assertEquals(0, apiResponse.getCode());
                    GenTableInfoPageResponse data = (GenTableInfoPageResponse) apiResponse.getData();
                    assertEquals("test_ddd", data.getDatabaseName());
                })
                .verifyComplete();
    }
}
