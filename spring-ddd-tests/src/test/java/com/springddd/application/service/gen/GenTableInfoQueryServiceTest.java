package com.springddd.application.service.gen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.gen.dto.*;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenTableInfoQueryServiceTest {

    @Mock
    private DatabaseClient databaseClient;

    @Mock
    private GenProjectInfoQueryService projectInfoQueryService;

    @Mock
    private GenColumnsQueryService columnsQueryService;

    @Mock
    private GenAggregateQueryService aggregateQueryService;

    @Mock
    private ReactiveRedisCacheHelper cacheHelper;

    private GenTableInfoQueryService genTableInfoQueryService;

    private ObjectMapper objectMapper;
    private GenTableInfoPageQuery pageQuery;
    private MockedStatic<SecurityUtils> securityUtilsMockedStatic;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        genTableInfoQueryService = new GenTableInfoQueryService(
                databaseClient,
                projectInfoQueryService,
                columnsQueryService,
                aggregateQueryService,
                cacheHelper,
                objectMapper
        );

        pageQuery = new GenTableInfoPageQuery();
        pageQuery.setDatabaseName("test_db");
        pageQuery.setPageNum(1);
        pageQuery.setPageSize(10);

        securityUtilsMockedStatic = mockStatic(SecurityUtils.class);
        securityUtilsMockedStatic.when(SecurityUtils::getUserId).thenReturn(1L);
    }

    @AfterEach
    void tearDown() {
        securityUtilsMockedStatic.close();
    }

    @Test
    void index_withNullDatabaseName_shouldReturnEmpty() {
        pageQuery.setDatabaseName(null);

        StepVerifier.create(genTableInfoQueryService.index(pageQuery))
                .verifyComplete();
    }

    @Test
    void index_withEmptyDatabaseName_shouldReturnEmpty() {
        pageQuery.setDatabaseName("");

        StepVerifier.create(genTableInfoQueryService.index(pageQuery))
                .verifyComplete();
    }

    @Test
    void preview_shouldReturnCachedTreeView() throws Exception {
        List<ProjectTreeView> treeViews = new ArrayList<>();
        treeViews.add(new ProjectTreeView());

        when(cacheHelper.getCache(eq(CacheKeys.GEN_FILES.buildKey(1L)), eq(List.class)))
                .thenReturn(Mono.just((List) treeViews));

        StepVerifier.create(genTableInfoQueryService.preview())
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.size());
                })
                .verifyComplete();
    }

    @Test
    void preview_withEmptyCache_shouldReturnEmpty() {
        when(cacheHelper.getCache(eq(CacheKeys.GEN_FILES.buildKey(1L)), eq(List.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(genTableInfoQueryService.preview())
                .verifyComplete();
    }
}
