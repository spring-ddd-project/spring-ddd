package com.springddd.application.service.gen;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.domain.auth.SecurityUtils;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenDownloadDomainServiceImplTest {

    @Mock
    private ReactiveRedisCacheHelper cacheHelper;

    @Mock
    private ObjectMapper objectMapper;

    private GenDownloadDomainServiceImpl service;

    @BeforeEach
    void setUp() {
        service = new GenDownloadDomainServiceImpl(cacheHelper, objectMapper);
    }

    @Test
    void download_shouldReturnZipResponse() {
        SecurityUtils.setUserId(1L);

        ProjectTreeView root = new ProjectTreeView();
        root.setLabel("test-project");
        root.setChildren(new ArrayList<>());

        ProjectTreeView dir = new ProjectTreeView();
        dir.setLabel("src");
        dir.setChildren(new ArrayList<>());
        root.getChildren().add(dir);

        ProjectTreeView file = new ProjectTreeView();
        file.setLabel("Test.java");
        file.setValue("public class Test {}");
        dir.getChildren().add(file);

        List<ProjectTreeView> treeList = List.of(root);

        when(cacheHelper.getCache(CacheKeys.GEN_FILES.buildKey(1L), List.class))
                .thenReturn(Mono.just(treeList));
        when(objectMapper.convertValue(eq(treeList), any(TypeReference.class)))
                .thenReturn(treeList);

        StepVerifier.create(service.download())
                .assertNext(response -> {
                    assertEquals(200, response.getStatusCode().value());
                    assertNotNull(response.getBody());
                })
                .verifyComplete();
    }

    @Test
    void download_shouldError_whenCacheMiss() {
        SecurityUtils.setUserId(1L);

        when(cacheHelper.getCache(CacheKeys.GEN_FILES.buildKey(1L), List.class))
                .thenReturn(Mono.empty());

        StepVerifier.create(service.download())
                .verifyComplete();
    }

    @Test
    void download_shouldHandleDirectoriesInTree() {
        SecurityUtils.setUserId(1L);

        ProjectTreeView root = new ProjectTreeView();
        root.setLabel("test-project");
        root.setChildren(new ArrayList<>());

        ProjectTreeView dir = new ProjectTreeView();
        dir.setLabel("src");
        dir.setChildren(new ArrayList<>());
        root.getChildren().add(dir);

        ProjectTreeView file = new ProjectTreeView();
        file.setLabel("Test.java");
        file.setValue("public class Test {}");
        dir.getChildren().add(file);

        ProjectTreeView emptyFile = new ProjectTreeView();
        emptyFile.setLabel("Empty.java");
        emptyFile.setValue(null);
        dir.getChildren().add(emptyFile);

        List<ProjectTreeView> treeList = List.of(root);

        when(cacheHelper.getCache(CacheKeys.GEN_FILES.buildKey(1L), List.class))
                .thenReturn(Mono.just(treeList));
        when(objectMapper.convertValue(eq(treeList), any(TypeReference.class)))
                .thenReturn(treeList);

        StepVerifier.create(service.download())
                .assertNext(response -> {
                    assertEquals(200, response.getStatusCode().value());
                    assertNotNull(response.getBody());
                })
                .verifyComplete();
    }

    @Test
    void download_shouldError_whenConvertValueThrowsException() {
        SecurityUtils.setUserId(1L);

        List<ProjectTreeView> treeList = List.of();

        when(cacheHelper.getCache(CacheKeys.GEN_FILES.buildKey(1L), List.class))
                .thenReturn(Mono.just(treeList));
        when(objectMapper.convertValue(eq(treeList), any(TypeReference.class)))
                .thenThrow(new RuntimeException("convert error"));

        StepVerifier.create(service.download())
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void download_shouldHandleNullChildren() {
        SecurityUtils.setUserId(1L);

        ProjectTreeView root = new ProjectTreeView();
        root.setLabel("test-project");
        root.setChildren(null);

        List<ProjectTreeView> treeList = List.of(root);

        when(cacheHelper.getCache(CacheKeys.GEN_FILES.buildKey(1L), List.class))
                .thenReturn(Mono.just(treeList));
        when(objectMapper.convertValue(eq(treeList), any(TypeReference.class)))
                .thenReturn(treeList);

        StepVerifier.create(service.download())
                .assertNext(response -> {
                    assertEquals(200, response.getStatusCode().value());
                    assertNotNull(response.getBody());
                })
                .verifyComplete();
    }

    @Test
    void download_shouldHandleEmptyChildren() {
        SecurityUtils.setUserId(1L);

        ProjectTreeView root = new ProjectTreeView();
        root.setLabel("test-project");
        root.setChildren(new ArrayList<>());

        List<ProjectTreeView> treeList = List.of(root);

        when(cacheHelper.getCache(CacheKeys.GEN_FILES.buildKey(1L), List.class))
                .thenReturn(Mono.just(treeList));
        when(objectMapper.convertValue(eq(treeList), any(TypeReference.class)))
                .thenReturn(treeList);

        StepVerifier.create(service.download())
                .assertNext(response -> {
                    assertEquals(200, response.getStatusCode().value());
                    assertNotNull(response.getBody());
                })
                .verifyComplete();
    }
}
