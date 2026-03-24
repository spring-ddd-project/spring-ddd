package com.springddd.domain.gen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.gen.GenDownloadDomainServiceImpl;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class GenDownloadDomainServiceTest {

    @Mock
    private ReactiveRedisCacheHelper cacheHelper;

    @Mock
    private ObjectMapper objectMapper;

    private GenDownloadDomainServiceImpl genDownloadDomainService;

    @BeforeEach
    void setUp() {
        genDownloadDomainService = new GenDownloadDomainServiceImpl(cacheHelper, objectMapper);
        SecurityUtils.setUserId(1L);
    }

    @AfterEach
    void tearDown() {
        SecurityUtils.setUserId(null);
    }

    @Test
    void download_shouldReturnZipFile() {
        List<ProjectTreeView> treeViewList = Arrays.asList(
                createProjectTreeViewWithValue("Test.java", "package com.test;")
        );

        when(cacheHelper.getCache(eq(CacheKeys.GEN_FILES.buildKey(1L)), eq(List.class)))
                .thenReturn(Mono.just(treeViewList));
        when(objectMapper.convertValue(any(), any(com.fasterxml.jackson.core.type.TypeReference.class)))
                .thenReturn(treeViewList);

        StepVerifier.create(genDownloadDomainService.download())
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(200, response.getStatusCode().value());
                    assertNotNull(response.getBody());
                    assertTrue(response.getHeaders().getContentDisposition().toString().contains("attachment"));
                    assertTrue(response.getHeaders().getContentDisposition().toString().contains("ddd_files.zip"));
                })
                .verifyComplete();
    }

    @Test
    void download_shouldHandleEmptyCache() {
        when(cacheHelper.getCache(eq(CacheKeys.GEN_FILES.buildKey(1L)), eq(List.class)))
                .thenReturn(Mono.empty());

        StepVerifier.create(genDownloadDomainService.download())
                .verifyComplete();
    }

    @Test
    void download_shouldCreateZipWithNestedStructure() {
        ProjectTreeView root = new ProjectTreeView();
        root.setLabel("test-project");

        ProjectTreeView child = new ProjectTreeView();
        child.setLabel("src");
        child.setValue(null);
        root.setChildren(List.of(child));

        List<ProjectTreeView> treeViewList = Arrays.asList(root);

        when(cacheHelper.getCache(eq(CacheKeys.GEN_FILES.buildKey(1L)), eq(List.class)))
                .thenReturn(Mono.just(treeViewList));
        when(objectMapper.convertValue(any(), any(com.fasterxml.jackson.core.type.TypeReference.class)))
                .thenReturn(treeViewList);

        StepVerifier.create(genDownloadDomainService.download())
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(200, response.getStatusCode().value());
                })
                .verifyComplete();
    }

    private ProjectTreeView createProjectTreeViewWithValue(String label, String value) {
        ProjectTreeView node = new ProjectTreeView();
        node.setLabel(label);
        node.setValue(value);
        return node;
    }
}
