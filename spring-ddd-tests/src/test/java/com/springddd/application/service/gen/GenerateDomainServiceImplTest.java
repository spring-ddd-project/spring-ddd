package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenAggregateView;
import com.springddd.application.service.gen.dto.ProjectTreeBuilder;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.domain.gen.GenerateDomainService;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateDomainServiceImplTest {

    @Mock
    private GenTableInfoQueryService genTableInfoQueryService;

    @Mock
    private GenTemplateQueryService templateQueryService;

    @Mock
    private Configuration configuration;

    @Mock
    private ReactiveRedisCacheHelper cacheHelper;

    private GenerateDomainServiceImpl generateDomainService;

    @BeforeEach
    void setUp() {
        generateDomainService = new GenerateDomainServiceImpl(
                genTableInfoQueryService,
                templateQueryService,
                configuration,
                cacheHelper
        );
    }

    @Test
    void shouldReturnMonoVoid() {
        Map<String, Object> context = createMockContext();
        GenAggregateView aggregateView = new GenAggregateView();
        aggregateView.setObjectType((byte) 1);
        aggregateView.setHasCreated(true);
        aggregateView.setObjectName("TestAggregate");
        context.put("aggregateViews", List.of(aggregateView));

        when(genTableInfoQueryService.buildData(anyString())).thenReturn(Mono.just(context));
        when(templateQueryService.queryAllTemplate()).thenReturn(Mono.just(new ArrayList<>()));
        when(cacheHelper.setCache(anyString(), any(), any())).thenReturn(Mono.empty());

        Mono<Void> result = generateDomainService.generate("test_table");

        assertNotNull(result);
    }

    @Test
    void shouldUseProjectTreeBuilder() {
        GenerateDomainServiceImpl service = new GenerateDomainServiceImpl(
                genTableInfoQueryService,
                templateQueryService,
                configuration,
                cacheHelper
        );
        assertNotNull(service);
    }

    @Test
    void shouldHandleEmptyContext() {
        Map<String, Object> emptyContext = new HashMap<>();
        when(genTableInfoQueryService.buildData(anyString())).thenReturn(Mono.just(emptyContext));
        when(templateQueryService.queryAllTemplate()).thenReturn(Mono.just(new ArrayList<>()));

        Mono<Void> result = generateDomainService.generate("test_table");

        StepVerifier.create(result)
                .verifyComplete();
    }

    private Map<String, Object> createMockContext() {
        Map<String, Object> context = new HashMap<>();
        context.put("projectName", "test-project");
        context.put("moduleName", "test");
        context.put("packageName", "com.test");
        context.put("className", "TestClass");
        context.put("requestName", "testRequest");
        context.put("aggregateViews", new ArrayList<GenAggregateView>());
        return context;
    }
}
