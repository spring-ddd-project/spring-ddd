package com.springddd.application.service.dict;

import com.springddd.application.service.dict.dto.*;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysDictEntity;
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
class SysDictQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysDictViewMapStruct sysDictViewMapStruct;

    @Mock
    private SysDictItemQueryService sysDictItemQueryService;

    @Mock
    private SysDictItemViewMapStruct sysDictItemViewMapStruct;

    @InjectMocks
    private SysDictQueryService sysDictQueryService;

    private SysDictEntity mockEntity;
    private SysDictView mockView;

    @BeforeEach
    void setUp() {
        mockEntity = new SysDictEntity();
        mockEntity.setId(1L);
        mockEntity.setDictName("Test Dict");
        mockEntity.setDictCode("test_dict");
        mockEntity.setSortOrder(1);
        mockEntity.setDictStatus(true);
        mockEntity.setDeleteStatus(false);

        mockView = new SysDictView();
        mockView.setId(1L);
        mockView.setDictName("Test Dict");
        mockView.setDictCode("test_dict");
        mockView.setSortOrder(1);
        mockView.setDictStatus(true);
        mockView.setDeleteStatus(false);
    }

    @SuppressWarnings("unchecked")
    private void setupMockSelectReturningEntity() {
        ReactiveSelectOperation.ReactiveSelect<SysDictEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Mono.just(mockEntity)).when(mockTerminatingSelect).one();
    }

    @SuppressWarnings("unchecked")
    private void setupMockSelectReturningEmpty() {
        ReactiveSelectOperation.ReactiveSelect<SysDictEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Mono.empty()).when(mockTerminatingSelect).one();
    }

    @SuppressWarnings("unchecked")
    @Test
    void index_shouldReturnPageResponse() {
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        List<SysDictEntity> entities = Arrays.asList(mockEntity);
        List<SysDictView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysDictEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysDictEntity.class));
        when(sysDictViewMapStruct.toViews(entities)).thenReturn(views);

        StepVerifier.create(sysDictQueryService.index(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.getItems().size());
                    assertEquals(1L, response.getTotal());
                    assertEquals("Test Dict", response.getItems().get(0).getDictName());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void index_shouldFilterByDictName() {
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDictName("Test");
        List<SysDictEntity> entities = Arrays.asList(mockEntity);
        List<SysDictView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysDictEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysDictEntity.class));
        when(sysDictViewMapStruct.toViews(entities)).thenReturn(views);

        StepVerifier.create(sysDictQueryService.index(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.getItems().size());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void index_shouldFilterByDictCode() {
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDictCode("test_dict");
        List<SysDictEntity> entities = Arrays.asList(mockEntity);
        List<SysDictView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysDictEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysDictEntity.class));
        when(sysDictViewMapStruct.toViews(entities)).thenReturn(views);

        StepVerifier.create(sysDictQueryService.index(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.getItems().size());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void index_shouldReturnEmptyWhenNoData() {
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        ReactiveSelectOperation.ReactiveSelect<SysDictEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.empty()).when(mockTerminatingSelect).all();
        doReturn(Mono.just(0L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysDictEntity.class));
        when(sysDictViewMapStruct.toViews(Collections.emptyList())).thenReturn(Collections.emptyList());

        StepVerifier.create(sysDictQueryService.index(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(0, response.getItems().size());
                    assertEquals(0L, response.getTotal());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void queryAll_shouldReturnAllDicts() {
        List<SysDictEntity> entities = Arrays.asList(mockEntity);
        List<SysDictView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysDictEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        when(sysDictViewMapStruct.toViews(entities)).thenReturn(views);

        StepVerifier.create(sysDictQueryService.queryAll())
                .assertNext(result -> {
                    assertNotNull(result);
                    assertEquals(1, result.size());
                    assertEquals("Test Dict", result.get(0).getDictName());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void queryAll_shouldReturnEmptyWhenNoData() {
        ReactiveSelectOperation.ReactiveSelect<SysDictEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.empty()).when(mockTerminatingSelect).all();
        when(sysDictViewMapStruct.toViews(Collections.emptyList())).thenReturn(Collections.emptyList());

        StepVerifier.create(sysDictQueryService.queryAll())
                .expectNext(Collections.emptyList())
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void recycle_shouldReturnDeletedDicts() {
        SysDictPageQuery query = new SysDictPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        mockEntity.setDeleteStatus(true);
        mockView.setDeleteStatus(true);
        List<SysDictEntity> entities = Arrays.asList(mockEntity);
        List<SysDictView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysDictEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysDictEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysDictEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysDictEntity.class));
        when(sysDictViewMapStruct.toViews(entities)).thenReturn(views);

        StepVerifier.create(sysDictQueryService.recycle(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.getItems().size());
                    assertEquals(true, response.getItems().get(0).getDeleteStatus());
                })
                .verifyComplete();
    }

    @Test
    void queryItemLabelByDictCode_shouldReturnItemLabel() {
        String dictCode = "test_dict";
        Integer itemValue = 1;

        SysDictItemView itemView = new SysDictItemView();
        itemView.setId(1L);
        itemView.setItemLabel("Test Item");

        setupMockSelectReturningEntity();
        when(sysDictViewMapStruct.toView(mockEntity)).thenReturn(mockView);
        when(sysDictItemQueryService.queryItemLabelByItemValueAndDictId(1L, itemValue))
                .thenReturn(Mono.just(itemView));

        StepVerifier.create(sysDictQueryService.queryItemLabelByDictCode(dictCode, itemValue))
                .assertNext(label -> {
                    assertNotNull(label);
                    assertEquals("Test Item", label);
                })
                .verifyComplete();
    }

    @Test
    void queryItemLabelByDictCode_shouldReturnEmptyWhenDictNotFound() {
        String dictCode = "nonexistent";
        Integer itemValue = 1;

        setupMockSelectReturningEmpty();

        StepVerifier.create(sysDictQueryService.queryItemLabelByDictCode(dictCode, itemValue))
                .verifyComplete();
    }

    @Test
    void queryDictByCode_shouldReturnDictItems() {
        String code = "test_dict";

        SysDictItemView itemView = new SysDictItemView();
        itemView.setId(1L);
        itemView.setItemLabel("Test Item");

        setupMockSelectReturningEntity();
        when(sysDictViewMapStruct.toView(mockEntity)).thenReturn(mockView);
        when(sysDictItemQueryService.queryItemLabelByDictId(1L))
                .thenReturn(Mono.just(Arrays.asList(itemView)));

        StepVerifier.create(sysDictQueryService.queryDictByCode(code))
                .assertNext(items -> {
                    assertNotNull(items);
                    assertEquals(1, items.size());
                    assertEquals("Test Item", items.get(0).getItemLabel());
                })
                .verifyComplete();
    }

    @Test
    void queryDictByCode_shouldReturnEmptyWhenDictNotFound() {
        String code = "nonexistent";

        setupMockSelectReturningEmpty();

        StepVerifier.create(sysDictQueryService.queryDictByCode(code))
                .verifyComplete();
    }

    @Test
    void queryDictNameById_shouldReturnDictName() {
        Long id = 1L;

        setupMockSelectReturningEntity();
        when(sysDictViewMapStruct.toView(mockEntity)).thenReturn(mockView);

        StepVerifier.create(sysDictQueryService.queryDictNameById(id))
                .assertNext(dictName -> {
                    assertNotNull(dictName);
                    assertEquals("test_dict", dictName);
                })
                .verifyComplete();
    }

    @Test
    void queryDictNameById_shouldReturnEmptyWhenIdIsNull() {
        StepVerifier.create(sysDictQueryService.queryDictNameById(null))
                .verifyComplete();
    }

    @Test
    void queryDictNameById_shouldReturnEmptyWhenEntityNotFound() {
        Long id = 999L;

        setupMockSelectReturningEmpty();

        StepVerifier.create(sysDictQueryService.queryDictNameById(id))
                .verifyComplete();
    }

    @Test
    void queryDictNameById_shouldReturnEmptyWhenViewIsNull() {
        Long id = 1L;

        setupMockSelectReturningEntity();
        when(sysDictViewMapStruct.toView(mockEntity)).thenReturn(null);

        StepVerifier.create(sysDictQueryService.queryDictNameById(id))
                .verifyComplete();
    }
}
