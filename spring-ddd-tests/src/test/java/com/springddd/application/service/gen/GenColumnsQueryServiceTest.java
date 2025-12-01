package com.springddd.application.service.gen;

import com.springddd.application.service.dict.SysDictQueryService;
import com.springddd.application.service.gen.dto.GenColumnsView;
import com.springddd.application.service.gen.dto.GenColumnsViewMapStruct;
import com.springddd.application.service.gen.dto.GenProjectInfoViewMapStruct;
import com.springddd.domain.gen.GenColumnBindView;
import com.springddd.domain.util.PageResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenColumnsQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private GenColumnsViewMapStruct genColumnsViewMapStruct;

    @Mock
    private GenProjectInfoViewMapStruct genProjectInfoViewMapStruct;

    @Mock
    private DatabaseClient databaseClient;

    @Mock
    private GenColumnBindQueryService genColumnBindQueryService;

    @Mock
    private SysDictQueryService sysDictQueryService;

    private GenColumnsQueryService genColumnsQueryService;

    @BeforeEach
    void setUp() {
        genColumnsQueryService = new GenColumnsQueryService(
                r2dbcEntityTemplate,
                genColumnsViewMapStruct,
                genProjectInfoViewMapStruct,
                databaseClient,
                genColumnBindQueryService,
                sysDictQueryService
        );
    }

    @Test
    void queryColumnsByGenInfoId_shouldReturnEmpty_whenDatabaseNameIsEmpty() {
        Mono<PageResponse<GenColumnsView>> result = genColumnsQueryService.queryColumnsByGenInfoId(1L, "");
        StepVerifier.create(result).verifyComplete();
    }

    @Test
    void queryColumnsByGenInfoId_shouldReturnEmpty_whenDatabaseNameIsNull() {
        Mono<PageResponse<GenColumnsView>> result = genColumnsQueryService.queryColumnsByGenInfoId(1L, null);
        StepVerifier.create(result).verifyComplete();
    }
}
