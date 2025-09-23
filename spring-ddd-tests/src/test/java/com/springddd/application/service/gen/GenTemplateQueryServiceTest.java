package com.springddd.application.service.gen;

import com.springddd.application.service.gen.dto.GenTemplatePageQuery;
import com.springddd.application.service.gen.dto.GenTemplateView;
import com.springddd.application.service.gen.dto.GenTemplateViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.GenTemplateEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveSelectOperation;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenTemplateQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private GenTemplateViewMapStruct genTemplateViewMapStruct;

    @InjectMocks
    private GenTemplateQueryService genTemplateQueryService;

    private GenTemplateEntity mockTemplateEntity;
    private GenTemplateView mockTemplateView;
    private GenTemplatePageQuery pageQuery;
    private GenTemplatePageQuery recycleQuery;

    @BeforeEach
    void setUp() {
        mockTemplateEntity = new GenTemplateEntity();
        mockTemplateEntity.setId(1L);
        mockTemplateEntity.setTemplateName("entity_template");
        mockTemplateEntity.setTemplateContent("content here");
        mockTemplateEntity.setDeleteStatus(false);

        mockTemplateView = new GenTemplateView();
        mockTemplateView.setId(1L);
        mockTemplateView.setTemplateName("entity_template");
        mockTemplateView.setTemplateContent("content here");

        pageQuery = new GenTemplatePageQuery();
        pageQuery.setPageNum(1);
        pageQuery.setPageSize(10);
        pageQuery.setDeleteStatus(false);

        recycleQuery = new GenTemplatePageQuery();
        recycleQuery.setPageNum(1);
        recycleQuery.setPageSize(10);
        recycleQuery.setDeleteStatus(true);
    }

    @SuppressWarnings("unchecked")
    @Test
    void index_shouldReturnPageResponse_withTemplates() {
        GenTemplateEntity entity = new GenTemplateEntity();
        entity.setId(1L);
        entity.setTemplateName("test_template");
        entity.setTemplateContent("test content");
        entity.setDeleteStatus(false);

        GenTemplateView view = new GenTemplateView();
        view.setId(1L);
        view.setTemplateName("test_template");
        view.setTemplateContent("test content");

        ReactiveSelectOperation.ReactiveSelect<GenTemplateEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<GenTemplateEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(GenTemplateEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(entity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(GenTemplateEntity.class));
        when(genTemplateViewMapStruct.toViews(anyList())).thenReturn(List.of(view));

        StepVerifier.create(genTemplateQueryService.index(pageQuery))
                .expectNextMatches(response -> {
                    return response.getItems().size() == 1 &&
                           response.getTotal() == 1 &&
                           response.getPageNum() == 1 &&
                           response.getPageSize() == 10;
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void index_shouldReturnEmpty_whenNoTemplatesFound() {
        ReactiveSelectOperation.ReactiveSelect<GenTemplateEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<GenTemplateEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(GenTemplateEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.empty()).when(mockTerminatingSelect).all();
        doReturn(Mono.just(0L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(GenTemplateEntity.class));
        when(genTemplateViewMapStruct.toViews(anyList())).thenReturn(Collections.emptyList());

        StepVerifier.create(genTemplateQueryService.index(pageQuery))
                .expectNextMatches(response -> {
                    return response.getItems().isEmpty() && response.getTotal() == 0;
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void index_shouldFilterByTemplateName_whenProvided() {
        GenTemplatePageQuery queryWithName = new GenTemplatePageQuery();
        queryWithName.setPageNum(1);
        queryWithName.setPageSize(10);
        queryWithName.setTemplateName("entity");

        ReactiveSelectOperation.ReactiveSelect<GenTemplateEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<GenTemplateEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(GenTemplateEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockTemplateEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(GenTemplateEntity.class));
        when(genTemplateViewMapStruct.toViews(anyList())).thenReturn(List.of(mockTemplateView));

        StepVerifier.create(genTemplateQueryService.index(queryWithName))
                .expectNextMatches(response -> !response.getItems().isEmpty())
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void recycle_shouldReturnDeletedTemplates() {
        GenTemplateEntity deletedEntity = new GenTemplateEntity();
        deletedEntity.setId(2L);
        deletedEntity.setTemplateName("deleted_template");
        deletedEntity.setDeleteStatus(true);

        GenTemplateView deletedView = new GenTemplateView();
        deletedView.setId(2L);
        deletedView.setTemplateName("deleted_template");

        ReactiveSelectOperation.ReactiveSelect<GenTemplateEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<GenTemplateEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(GenTemplateEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(deletedEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(GenTemplateEntity.class));
        when(genTemplateViewMapStruct.toViews(anyList())).thenReturn(List.of(deletedView));

        StepVerifier.create(genTemplateQueryService.recycle(recycleQuery))
                .expectNextMatches(response -> response.getItems().size() == 1)
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void queryAllTemplate_shouldReturnAllActiveTemplates() {
        GenTemplateEntity entity1 = new GenTemplateEntity();
        entity1.setId(1L);
        entity1.setTemplateName("template1");
        entity1.setDeleteStatus(false);

        GenTemplateEntity entity2 = new GenTemplateEntity();
        entity2.setId(2L);
        entity2.setTemplateName("template2");
        entity2.setDeleteStatus(false);

        GenTemplateView view1 = new GenTemplateView();
        view1.setId(1L);
        view1.setTemplateName("template1");

        GenTemplateView view2 = new GenTemplateView();
        view2.setId(2L);
        view2.setTemplateName("template2");

        ReactiveSelectOperation.ReactiveSelect<GenTemplateEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<GenTemplateEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(GenTemplateEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(entity1, entity2)).when(mockTerminatingSelect).all();
        when(genTemplateViewMapStruct.toViews(anyList())).thenReturn(List.of(view1, view2));

        StepVerifier.create(genTemplateQueryService.queryAllTemplate())
                .expectNextMatches(views -> views.size() == 2)
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void queryAllTemplate_shouldReturnEmptyList_whenNoTemplatesExist() {
        ReactiveSelectOperation.ReactiveSelect<GenTemplateEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<GenTemplateEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(GenTemplateEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.empty()).when(mockTerminatingSelect).all();
        when(genTemplateViewMapStruct.toViews(anyList())).thenReturn(Collections.emptyList());

        StepVerifier.create(genTemplateQueryService.queryAllTemplate())
                .expectNextMatches(views -> views.isEmpty())
                .verifyComplete();
    }
}
