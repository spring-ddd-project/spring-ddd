package com.springddd.application.service.user;

import com.springddd.application.service.permission.DataScopeCriteriaBuilder;
import com.springddd.application.service.user.dto.SysUserPageQuery;
import com.springddd.application.service.user.dto.SysUserQuery;
import com.springddd.application.service.user.dto.SysUserView;
import com.springddd.application.service.user.dto.SysUserViewMapStruct;
import com.springddd.domain.util.PageResponse;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
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
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.lang.reflect.Field;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@org.mockito.junit.jupiter.MockitoSettings(strictness = org.mockito.quality.Strictness.LENIENT)
class SysUserQueryServiceTest {

    @Mock
    private SysUserViewMapStruct sysUserViewMapStruct;

    @Mock
    private QueryFactory queryFactory;

    @Mock
    private DataScopeCriteriaBuilder dataScopeCriteriaBuilder;

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysUserEntity> selectOp;

    @Mock
    private ReactiveSelectOperation.TerminatingSelect<SysUserEntity> terminatingSelect;

    @InjectMocks
    private SysUserQueryService service;

    @BeforeEach
    void setUp() throws Exception {
        Field qfField = service.getClass().getSuperclass().getDeclaredField("queryFactory");
        qfField.setAccessible(true);
        qfField.set(service, queryFactory);

        Field dscbField = service.getClass().getSuperclass().getDeclaredField("dataScopeCriteriaBuilder");
        dscbField.setAccessible(true);
        dscbField.set(service, dataScopeCriteriaBuilder);

        when(queryFactory.getR2dbcEntityTemplate()).thenReturn(r2dbcEntityTemplate);
        when(dataScopeCriteriaBuilder.apply(any(Criteria.class), any())).thenAnswer(inv -> Mono.just(inv.getArgument(0)));
    }

    @Test
    @DisplayName("page 当 phone 不为空时应按 phone 过滤")
    void page_whenPhoneNotEmpty_shouldFilterByPhone() {
        SysUserPageQuery query = new SysUserPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setPhone("138");

        SysUserEntity entity = new SysUserEntity();
        entity.setId(1L);
        entity.setPhone("13800138000");

        SysUserView view = new SysUserView();
        view.setId(1L);
        view.setPhone("13800138000");

        when(r2dbcEntityTemplate.select(SysUserEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysUserViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysUserEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.page(query))
                .assertNext(page -> {
                    assertThat(page.getList()).hasSize(1);
                    assertThat(page.getList().get(0).getPhone()).isEqualTo("13800138000");
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("page 应返回分页结果")
    void page_shouldReturnPage() {
        SysUserPageQuery query = new SysUserPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setUsername("admin");

        SysUserEntity entity = new SysUserEntity();
        entity.setId(1L);
        entity.setUsername("admin");

        SysUserView view = new SysUserView();
        view.setId(1L);
        view.setUsername("admin");

        when(r2dbcEntityTemplate.select(SysUserEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysUserViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysUserEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.page(query))
                .assertNext(page -> {
                    assertThat(page.getList()).hasSize(1);
                    assertThat(page.getList().get(0).getUsername()).isEqualTo("admin");
                    assertThat(page.getPageNum()).isEqualTo(1);
                    assertThat(page.getPageSize()).isEqualTo(10);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("recycle 应返回回收站分页结果")
    void recycle_shouldReturnRecyclePage() {
        SysUserPageQuery query = new SysUserPageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        SysUserEntity entity = new SysUserEntity();
        entity.setId(1L);

        SysUserView view = new SysUserView();
        view.setId(1L);

        when(r2dbcEntityTemplate.select(SysUserEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysUserViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));
        when(r2dbcEntityTemplate.count(any(Query.class), eq(SysUserEntity.class))).thenReturn(Mono.just(1L));

        StepVerifier.create(service.recycle(query))
                .assertNext(page -> assertThat(page.getList()).hasSize(1))
                .verifyComplete();
    }

    @Test
    @DisplayName("queryUserByUsername 应返回用户信息")
    void queryUserByUsername_shouldReturnUser() {
        SysUserEntity entity = new SysUserEntity();
        entity.setId(1L);
        entity.setUsername("admin");

        SysUserView view = new SysUserView();
        view.setId(1L);
        view.setUsername("admin");

        when(r2dbcEntityTemplate.selectOne(any(Query.class), eq(SysUserEntity.class))).thenReturn(Mono.just(entity));
        when(sysUserViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(service.queryUserByUsername("admin"))
                .assertNext(result -> assertThat(result.getUsername()).isEqualTo("admin"))
                .verifyComplete();
    }
}
