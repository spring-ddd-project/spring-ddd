package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.*;
import com.springddd.infrastructure.persistence.entity.SysDictItemEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveSelectOperation;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SysDictItemQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysDictItemViewMapStruct sysDictItemViewMapStruct;

    @InjectMocks
    private SysDictItemQueryService sysDictItemQueryService;

    private SysDictItemEntity mockEntity;
    private SysDictItemView mockView;

    @BeforeEach
    void setUp() {
        mockEntity = new SysDictItemEntity();
        mockEntity.setId(1L);
        mockEntity.setDictId(1L);
        mockEntity.setItemLabel("Test Item");
        mockEntity.setItemValue(1);
        mockEntity.setSortOrder(1);
        mockEntity.setItemStatus(true);
        mockEntity.setDeleteStatus(false);

        mockView = new SysDictItemView();
        mockView.setId(1L);
        mockView.setDictId(1L);
        mockView.setItemLabel("Test Item");
        mockView.setItemValue(1);
        mockView.setSortOrder(1);
        mockView.setItemStatus(true);
        mockView.setDeleteStatus(false);
    }

    @SuppressWarnings("unchecked")
    private void setupMockSelectReturningEntity() {
        ReactiveSelectOperation.ReactiveSelect<SysDictItemEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictItemEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictItemEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Mono.just(mockEntity)).when(mockTerminatingSelect).one();
    }

    @SuppressWarnings("unchecked")
    private void setupMockSelectReturningEmpty() {
        ReactiveSelectOperation.ReactiveSelect<SysDictItemEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictItemEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictItemEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Mono.empty()).when(mockTerminatingSelect).one();
    }

    @SuppressWarnings("unchecked")
    @Test
    void index_shouldReturnPageResponse() {
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        List<SysDictItemEntity> entities = Arrays.asList(mockEntity);
        List<SysDictItemView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysDictItemEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictItemEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictItemEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysDictItemEntity.class));
        when(sysDictItemViewMapStruct.toViews(entities)).thenReturn(views);

        StepVerifier.create(sysDictItemQueryService.index(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.getItems().size());
                    assertEquals(1L, response.getTotal());
                    assertEquals("Test Item", response.getItems().get(0).getItemLabel());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void index_shouldFilterByDictId() {
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDictId(1L);
        List<SysDictItemEntity> entities = Arrays.asList(mockEntity);
        List<SysDictItemView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysDictItemEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictItemEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictItemEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysDictItemEntity.class));
        when(sysDictItemViewMapStruct.toViews(entities)).thenReturn(views);

        StepVerifier.create(sysDictItemQueryService.index(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.getItems().size());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void index_shouldReturnEmptyWhenNoData() {
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        ReactiveSelectOperation.ReactiveSelect<SysDictItemEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictItemEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictItemEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.empty()).when(mockTerminatingSelect).all();
        doReturn(Mono.just(0L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysDictItemEntity.class));
        when(sysDictItemViewMapStruct.toViews(Collections.emptyList())).thenReturn(Collections.emptyList());

        StepVerifier.create(sysDictItemQueryService.index(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(0, response.getItems().size());
                    assertEquals(0L, response.getTotal());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void recycle_shouldReturnDeletedItems() {
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        mockEntity.setDeleteStatus(true);
        mockView.setDeleteStatus(true);
        List<SysDictItemEntity> entities = Arrays.asList(mockEntity);
        List<SysDictItemView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysDictItemEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictItemEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictItemEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysDictItemEntity.class));
        when(sysDictItemViewMapStruct.toViews(entities)).thenReturn(views);

        StepVerifier.create(sysDictItemQueryService.recycle(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.getItems().size());
                    assertEquals(true, response.getItems().get(0).getDeleteStatus());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void recycle_shouldFilterByDictId() {
        SysDictItemPageQuery query = new SysDictItemPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDictId(1L);
        mockEntity.setDeleteStatus(true);
        mockView.setDeleteStatus(true);
        List<SysDictItemEntity> entities = Arrays.asList(mockEntity);
        List<SysDictItemView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysDictItemEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictItemEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictItemEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysDictItemEntity.class));
        when(sysDictItemViewMapStruct.toViews(entities)).thenReturn(views);

        StepVerifier.create(sysDictItemQueryService.recycle(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.getItems().size());
                })
                .verifyComplete();
    }

    @Test
    void queryItemLabelByItemValueAndDictId_shouldReturnItemView() {
        Long dictId = 1L;
        Integer itemValue = 1;

        setupMockSelectReturningEntity();
        when(sysDictItemViewMapStruct.toView(mockEntity)).thenReturn(mockView);

        StepVerifier.create(sysDictItemQueryService.queryItemLabelByItemValueAndDictId(dictId, itemValue))
                .assertNext(item -> {
                    assertNotNull(item);
                    assertEquals("Test Item", item.getItemLabel());
                    assertEquals(1, item.getItemValue());
                })
                .verifyComplete();
    }

    @Test
    void queryItemLabelByItemValueAndDictId_shouldReturnEmptyWhenNotFound() {
        Long dictId = 999L;
        Integer itemValue = 999;

        setupMockSelectReturningEmpty();

        StepVerifier.create(sysDictItemQueryService.queryItemLabelByItemValueAndDictId(dictId, itemValue))
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void queryItemLabelByDictId_shouldReturnItemViews() {
        Long dictId = 1L;
        List<SysDictItemEntity> entities = Arrays.asList(mockEntity);
        List<SysDictItemView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysDictItemEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictItemEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictItemEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        when(sysDictItemViewMapStruct.toViews(entities)).thenReturn(views);

        StepVerifier.create(sysDictItemQueryService.queryItemLabelByDictId(dictId))
                .assertNext(items -> {
                    assertNotNull(items);
                    assertEquals(1, items.size());
                    assertEquals("Test Item", items.get(0).getItemLabel());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void queryItemLabelByDictId_shouldReturnEmptyWhenNoItems() {
        Long dictId = 999L;

        ReactiveSelectOperation.ReactiveSelect<SysDictItemEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictItemEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictItemEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.empty()).when(mockTerminatingSelect).all();
        when(sysDictItemViewMapStruct.toViews(Collections.emptyList())).thenReturn(Collections.emptyList());

        StepVerifier.create(sysDictItemQueryService.queryItemLabelByDictId(dictId))
                .expectNext(Collections.emptyList())
                .verifyComplete();
    }
}
