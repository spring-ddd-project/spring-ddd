package com.springddd.application.service.gen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.gen.dto.GenColumnsView;
import com.springddd.application.service.gen.dto.GenProjectInfoView;
import com.springddd.application.service.gen.dto.GenTableInfoPageQuery;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenTableInfoQueryServiceTest {

    @Mock
    private GenProjectInfoQueryService projectInfoQueryService;

    @Mock
    private GenColumnsQueryService columnsQueryService;

    @Mock
    private GenAggregateQueryService aggregateQueryService;

    @Mock
    private ReactiveRedisCacheHelper cacheHelper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GenTableInfoQueryService genTableInfoQueryService;

    private GenTableInfoPageQuery pageQuery;
    private MockedStatic<SecurityUtils> securityUtilsMockedStatic;

    @BeforeEach
    void setUp() {
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
    void preview_shouldReturnCachedTreeView() throws Exception {
        List<ProjectTreeView> treeViews = Arrays.asList(new ProjectTreeView());

        when(cacheHelper.getCache(eq(CacheKeys.GEN_FILES.buildKey(1L)), eq(List.class)))
                .thenReturn(Mono.just(treeViews));
        when(objectMapper.convertValue(any(), any(com.fasterxml.jackson.core.type.TypeReference.class)))
                .thenReturn(treeViews);

        StepVerifier.create(genTableInfoQueryService.preview())
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.size());
                })
                .verifyComplete();
    }
}
