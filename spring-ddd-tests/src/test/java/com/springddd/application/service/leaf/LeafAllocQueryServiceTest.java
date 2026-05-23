package com.springddd.application.service.leaf;

import com.springddd.application.service.leaf.dto.LeafAllocPageQuery;
import com.springddd.application.service.leaf.dto.LeafAllocView;
import com.springddd.application.service.leaf.dto.LeafAllocViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.LeafAllocEntity;
import com.springddd.infrastructure.persistence.factory.QueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class LeafAllocQueryServiceTest {

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private LeafAllocViewMapStruct leafAllocViewMapStruct;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<LeafAllocEntity> selectOp;

    @Mock
    private ReactiveSelectOperation.TerminatingSelect<LeafAllocEntity> terminatingSelect;

    @InjectMocks
    private LeafAllocQueryService service;

    @BeforeEach
    void setUp() {
        when(queryFactory.getR2dbcEntityTemplate()).thenReturn(r2dbcEntityTemplate);
    }

    @Test
    @DisplayName("index 应返回分页结果")
    void index_shouldReturnPage() {
        LeafAllocPageQuery query = new LeafAllocPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setId(1L);
        entity.setBizTag("test");

        LeafAllocView view = new LeafAllocView();
        view.setId(1L);
        view.setBizTag("test");

        when(r2dbcEntityTemplate.select(LeafAllocEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(leafAllocViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(LeafAllocEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.index(query))
                .assertNext(page -> {
                    assertThat(page.getList()).hasSize(1);
                    assertThat(page.getList().get(0).getBizTag()).isEqualTo("test");
                    assertThat(page.getPageNum()).isEqualTo(1);
                    assertThat(page.getPageSize()).isEqualTo(10);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("recycle 应返回回收站分页结果")
    void recycle_shouldReturnRecyclePage() {
        LeafAllocPageQuery query = new LeafAllocPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        LeafAllocEntity entity = new LeafAllocEntity();
        entity.setId(1L);

        LeafAllocView view = new LeafAllocView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(LeafAllocEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(leafAllocViewMapStruct.toViews(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(LeafAllocEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.recycle(query))
                .assertNext(page -> assertThat(page.getList()).hasSize(1))
                .verifyComplete();
    }
}
