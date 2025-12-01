package com.springddd.application.service.gen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springddd.application.service.gen.dto.ProjectTreeView;
import com.springddd.infrastructure.cache.keys.CacheKeys;
import com.springddd.infrastructure.cache.util.ReactiveRedisCacheHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GenDownloadDomainServiceImplTest {

    @Mock
    private ReactiveRedisCacheHelper cacheHelper;

    private ObjectMapper objectMapper;

    private GenDownloadDomainServiceImpl genDownloadDomainServiceImpl;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        genDownloadDomainServiceImpl = new GenDownloadDomainServiceImpl(cacheHelper, objectMapper);
    }

    @Test
    void download_shouldReturnZipFile_whenCacheHasData() {
        List<ProjectTreeView> treeViews = Arrays.asList(
                createProjectTreeView("file1.txt", "content1", null),
                createProjectTreeView("folder", null, Arrays.asList(
                        createProjectTreeView("file2.txt", "content2", null)
                ))
        );

        when(cacheHelper.getCache(anyString(), eq(List.class)))
                .thenReturn(Mono.just(treeViews));

        Mono<ResponseEntity<Resource>> result = genDownloadDomainServiceImpl.download();

        StepVerifier.create(result)
                .assertNext(response -> {
                    assert response.getStatusCode().value() == 200;
                    assert response.getHeaders().getContentDisposition().toString().contains("attachment");
                    assert response.getBody() != null;
                })
                .verifyComplete();
    }

    @Test
    void download_shouldReturnError_whenCacheIsEmpty() {
        when(cacheHelper.getCache(anyString(), eq(List.class)))
                .thenReturn(Mono.empty());

        Mono<ResponseEntity<Resource>> result = genDownloadDomainServiceImpl.download();

        StepVerifier.create(result).verifyComplete();
    }

    private ProjectTreeView createProjectTreeView(String label, String value, List<ProjectTreeView> children) {
        ProjectTreeView view = new ProjectTreeView();
        view.setLabel(label);
        view.setValue(value);
        view.setChildren(children);
        return view;
    }
}
