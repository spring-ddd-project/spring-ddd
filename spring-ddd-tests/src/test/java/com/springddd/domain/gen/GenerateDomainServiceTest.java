package com.springddd.domain.gen;

import com.springddd.application.service.gen.GenerateDomainServiceImpl;
import com.springddd.application.service.gen.GenTableInfoQueryService;
import com.springddd.application.service.gen.GenTemplateQueryService;
import com.springddd.application.service.gen.dto.GenTemplateView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import freemarker.template.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateDomainServiceTest {

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
        generateDomainService = spy(new GenerateDomainServiceImpl(
                genTableInfoQueryService,
                templateQueryService,
                configuration,
                cacheHelper
        ));
        SecurityUtils.setUserId(1L);
    }

    @AfterEach
    void tearDown() {
        SecurityUtils.setUserId(null);
    }

    @Test
    void generate_shouldCompleteSuccessfully() {
        String tableName = "test_table";
        Map<String, Object> context = new HashMap<>();
        context.put("projectName", "test-project");
        context.put("moduleName", "test");
        context.put("packageName", "com.test");
        context.put("className", "TestClass");
        context.put("requestName", "test");
        context.put("aggregateViews", List.of());

        GenTemplateView templateView = new GenTemplateView();
        templateView.setId(1L);
        templateView.setTemplateName("entity");
        templateView.setTemplateContent("<#-- entity template -->");

        when(genTableInfoQueryService.buildData(tableName)).thenReturn(Mono.just(context));
        when(templateQueryService.queryAllTemplate()).thenReturn(Mono.just(Arrays.asList(templateView)));
        when(cacheHelper.setCache(anyString(), anyList(), any())).thenReturn(Mono.empty());
        doReturn(Mono.just("rendered content")).when(generateDomainService).renderTemplate(anyString(), anyString(), anyMap());

        StepVerifier.create(generateDomainService.generate(tableName))
                .verifyComplete();

        verify(genTableInfoQueryService).buildData(tableName);
        verify(templateQueryService).queryAllTemplate();
        verify(cacheHelper).setCache(eq(CacheKeys.GEN_FILES.buildKey(1L)), anyList(), eq(CacheKeys.GEN_FILES.ttl()));
    }

    @Test
    void generate_shouldHandleEmptyTemplates() {
        String tableName = "empty_table";
        Map<String, Object> context = new HashMap<>();
        context.put("projectName", "test-project");
        context.put("moduleName", "test");
        context.put("packageName", "com.test");
        context.put("className", "TestClass");
        context.put("requestName", "test");
        context.put("aggregateViews", List.of());

        when(genTableInfoQueryService.buildData(tableName)).thenReturn(Mono.just(context));
        when(templateQueryService.queryAllTemplate()).thenReturn(Mono.just(List.of()));
        when(cacheHelper.setCache(anyString(), anyList(), any())).thenReturn(Mono.empty());

        StepVerifier.create(generateDomainService.generate(tableName))
                .verifyComplete();

        verify(genTableInfoQueryService).buildData(tableName);
        verify(templateQueryService).queryAllTemplate();
    }
}
