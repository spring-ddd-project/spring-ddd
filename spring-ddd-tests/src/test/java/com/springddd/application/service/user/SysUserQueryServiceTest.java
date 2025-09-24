package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserPageQuery;
import com.springddd.application.service.user.dto.SysUserQuery;
import com.springddd.application.service.user.dto.SysUserView;
import com.springddd.application.service.user.dto.SysUserViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysUserQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysUserViewMapStruct sysUserViewMapStruct;

    private SysUserQueryService sysUserQueryService;

    @BeforeEach
    void setUp() {
        sysUserQueryService = new SysUserQueryService(r2dbcEntityTemplate, sysUserViewMapStruct);
    }

    @Test
    void page_shouldReturnPageResponse_whenDataExists() {
        SysUserPageQuery query = new SysUserPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDeleteStatus(false);
        query.setUsername("test");

        SysUserEntity entity = new SysUserEntity();
        entity.setId(1L);
        entity.setUsername("testuser");

        SysUserView view = new SysUserView();
        view.setId(1L);
        view.setUsername("testuser");

        List<SysUserView> views = Collections.singletonList(view);

        when(r2dbcEntityTemplate.select(SysUserEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.just(entity));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysUserEntity.class)))
                .thenReturn(Mono.just(1L));
        when(sysUserViewMapStruct.toViewList(any())).thenReturn(views);

        Mono<PageResponse<SysUserView>> result = sysUserQueryService.page(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 1 && response.getItems().size() == 1)
                .verifyComplete();
    }

    @Test
    void page_shouldReturnEmptyPage_whenNoData() {
        SysUserPageQuery query = new SysUserPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDeleteStatus(false);

        when(r2dbcEntityTemplate.select(SysUserEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.empty());
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysUserEntity.class)))
                .thenReturn(Mono.just(0L));
        when(sysUserViewMapStruct.toViewList(any())).thenReturn(Collections.emptyList());

        Mono<PageResponse<SysUserView>> result = sysUserQueryService.page(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 0 && response.getItems().isEmpty())
                .verifyComplete();
    }

    @Test
    void recycle_shouldReturnPageResponse_withDeletedItems() {
        SysUserPageQuery query = new SysUserPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setDeleteStatus(true);

        when(r2dbcEntityTemplate.select(SysUserEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.empty());
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysUserEntity.class)))
                .thenReturn(Mono.just(0L));
        when(sysUserViewMapStruct.toViewList(any())).thenReturn(Collections.emptyList());

        Mono<PageResponse<SysUserView>> result = sysUserQueryService.recycle(query);

        StepVerifier.create(result)
                .expectNextMatches(response -> response.getTotal() == 0)
                .verifyComplete();
    }

    @Test
    void queryUserByUsername_shouldReturnView_whenUserExists() {
        SysUserEntity entity = new SysUserEntity();
        entity.setId(1L);
        entity.setUsername("testuser");
        entity.setDeleteStatus(false);

        SysUserView view = new SysUserView();
        view.setId(1L);
        view.setUsername("testuser");

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysUserEntity.class)))
                .thenReturn(Mono.just(entity));
        when(sysUserViewMapStruct.toView(entity)).thenReturn(view);

        Mono<SysUserView> result = sysUserQueryService.queryUserByUsername("testuser");

        StepVerifier.create(result)
                .expectNextMatches(user -> "testuser".equals(user.getUsername()))
                .verifyComplete();
    }

    @Test
    void queryUserByUsername_shouldReturnEmpty_whenUserNotFound() {
        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysUserEntity.class)))
                .thenReturn(Mono.empty());

        Mono<SysUserView> result = sysUserQueryService.queryUserByUsername("nonexistent");

        StepVerifier.create(result)
                .verifyComplete();
    }
}
