package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.application.service.user.dto.SysUserRoleViewMapStruct;
import com.springddd.infrastructure.persistence.entity.SysUserRoleEntity;
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
import reactor.test.StepVerifier;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysUserRoleQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysUserRoleViewMapStruct sysUserRoleViewMapStruct;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysUserRoleEntity> selectOp;

    @Mock
    private ReactiveSelectOperation.TerminatingSelect<SysUserRoleEntity> terminatingSelect;

    @InjectMocks
    private SysUserRoleQueryService service;

    @Test
    @DisplayName("queryLinkUserAndRole 应返回用户角色关联列表")
    void queryLinkUserAndRole_shouldReturnList() {
        SysUserRoleEntity entity = new SysUserRoleEntity();
        entity.setUserId(1L);
        entity.setRoleId(2L);

        SysUserRoleView view = new SysUserRoleView();
        view.setUserId(1L);
        view.setRoleId(2L);

        when(r2dbcEntityTemplate.select(SysUserRoleEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysUserRoleViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));

        StepVerifier.create(service.queryLinkUserAndRole(1L))
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    assertThat(list.get(0).getRoleId()).isEqualTo(2L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("queryLinkUserAndRoleByRoleId 应返回用户角色关联列表")
    void queryLinkUserAndRoleByRoleId_shouldReturnList() {
        SysUserRoleEntity entity = new SysUserRoleEntity();
        entity.setUserId(1L);
        entity.setRoleId(2L);

        SysUserRoleView view = new SysUserRoleView();
        view.setUserId(1L);
        view.setRoleId(2L);

        when(r2dbcEntityTemplate.select(SysUserRoleEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysUserRoleViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));

        StepVerifier.create(service.queryLinkUserAndRoleByRoleId(2L))
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    assertThat(list.get(0).getUserId()).isEqualTo(1L);
                })
                .verifyComplete();
    }
}
