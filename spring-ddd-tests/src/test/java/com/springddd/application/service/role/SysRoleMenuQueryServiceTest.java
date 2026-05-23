package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuView;
import com.springddd.application.service.role.dto.SysRoleMenuViewMapStruct;
import com.springddd.infrastructure.persistence.entity.SysRoleMenuEntity;
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
class SysRoleMenuQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysRoleMenuViewMapStruct sysRoleMenuViewMapStruct;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysRoleMenuEntity> selectOp;

    @Mock
    private ReactiveSelectOperation.TerminatingSelect<SysRoleMenuEntity> terminatingSelect;

    @InjectMocks
    private SysRoleMenuQueryService service;

    @Test
    @DisplayName("queryLinkRoleAndMenus 应返回角色菜单关联列表")
    void queryLinkRoleAndMenus_shouldReturnList() {
        SysRoleMenuEntity entity = new SysRoleMenuEntity();
        entity.setRoleId(1L);
        entity.setMenuId(2L);

        SysRoleMenuView view = new SysRoleMenuView();
        view.setRoleId(1L);
        view.setMenuId(2L);

        when(r2dbcEntityTemplate.select(SysRoleMenuEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysRoleMenuViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));

        StepVerifier.create(service.queryLinkRoleAndMenus(1L))
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    assertThat(list.get(0).getMenuId()).isEqualTo(2L);
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("queryLinkRoleAndMenusByMenuId 应返回角色菜单关联列表")
    void queryLinkRoleAndMenusByMenuId_shouldReturnList() {
        SysRoleMenuEntity entity = new SysRoleMenuEntity();
        entity.setRoleId(1L);
        entity.setMenuId(2L);

        SysRoleMenuView view = new SysRoleMenuView();
        view.setRoleId(1L);
        view.setMenuId(2L);

        when(r2dbcEntityTemplate.select(SysRoleMenuEntity.class)).thenReturn(selectOp);
        when(selectOp.matching(any(Query.class))).thenReturn(terminatingSelect);
        when(terminatingSelect.all()).thenReturn(Flux.just(entity));
        when(sysRoleMenuViewMapStruct.toViewList(anyList())).thenReturn(List.of(view));

        StepVerifier.create(service.queryLinkRoleAndMenusByMenuId(2L))
                .assertNext(list -> {
                    assertThat(list).hasSize(1);
                    assertThat(list.get(0).getRoleId()).isEqualTo(1L);
                })
                .verifyComplete();
    }
}
