package com.springddd.application.service.leaf;

import com.springddd.application.service.leaf.dto.LeafAllocPageQuery;
import com.springddd.application.service.leaf.dto.LeafAllocView;
import com.springddd.application.service.leaf.dto.LeafAllocViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeafAllocQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;
    @Mock
    private LeafAllocViewMapStruct leafAllocViewMapStruct;

    @Mock
    private org.springframework.data.r2dbc.core.ReactiveSelectOperation.ReactiveSelect<LeafAllocEntity> reactiveSelect;

    @Mock
    private org.springframework.data.r2dbc.core.ReactiveSelectOperation.TerminatingSelect<LeafAllocEntity> terminatingSelect;

    private LeafAllocQueryService queryService;

    @BeforeEach
    void setUp() {
        queryService = new LeafAllocQueryService(r2dbcEntityTemplate, leafAllocViewMapStruct);
    }

    private void mockSelect() {
        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setId(1L);
        entity.setBizTag("test");

        LeafAllocView view = new LeafAllocView();
        view.setId(1L);
        view.setBizTag("test");

        when(r2dbcEntityTemplate.select(LeafAllocEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(leafAllocViewMapStruct.toViewList(any())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(), eq(LeafAllocEntity.class))).thenReturn(Mono.just(1L));
    }

    @Test
    void page_shouldReturnPageResponse_whenResultsExist() {
        LeafAllocPageQuery query = new LeafAllocPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setBizTag("test");

        mockSelect();

        StepVerifier.create(queryService.page(query))
                .assertNext(response -> {
                    assertEquals(1, response.getTotal());
                    assertEquals(1, response.getItems().size());
                })
                .verifyComplete();
    }

    @Test
    void page_shouldReturnPageResponse_whenBizTagEmpty() {
        LeafAllocPageQuery query = new LeafAllocPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        mockSelect();

        StepVerifier.create(queryService.page(query))
                .assertNext(response -> {
                    assertEquals(1, response.getTotal());
                })
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse() {
        LeafAllocPageQuery query = new LeafAllocPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        mockSelect();

        StepVerifier.create(queryService.recycle(query))
                .assertNext(response -> {
                    assertEquals(1, response.getTotal());
                    assertEquals(1, response.getItems().size());
                })
                .verifyComplete();
    }
}
