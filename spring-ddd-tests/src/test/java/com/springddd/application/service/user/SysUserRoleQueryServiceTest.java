package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.application.service.user.dto.SysUserRoleViewMapStruct;
import com.springddd.infrastructure.persistence.entity.SysUserRoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.core.ReactiveSelectOperation;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SysUserRoleQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysUserRoleViewMapStruct sysUserRoleViewMapStruct;

    @Mock
    private ReactiveSelectOperation.ReactiveSelect<SysUserRoleEntity> reactiveSelect;

    private SysUserRoleQueryService sysUserRoleQueryService;

    @BeforeEach
    void setUp() {
        sysUserRoleQueryService = new SysUserRoleQueryService(r2dbcEntityTemplate, sysUserRoleViewMapStruct);
    }

    @Test
    void queryLinkUserAndRole_shouldReturnViews_whenEntitiesExist() {
        Long userId = 1L;
        SysUserRoleEntity entity = new SysUserRoleEntity();
        entity.setId(1L);
        entity.setUserId(userId);
        entity.setRoleId(2L);

        SysUserRoleView view = new SysUserRoleView();
        view.setId(1L);
        view.setUserId(userId);
        view.setRoleId(2L);

        when(r2dbcEntityTemplate.select(SysUserRoleEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(sysUserRoleViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysUserRoleQueryService.queryLinkUserAndRole(userId))
                .expectNext(Collections.singletonList(view))
                .verifyComplete();
    }

    @Test
    void queryLinkUserAndRoleByRoleId_shouldReturnViews_whenEntitiesExist() {
        Long roleId = 2L;
        SysUserRoleEntity entity = new SysUserRoleEntity();
        entity.setId(1L);
        entity.setUserId(1L);
        entity.setRoleId(roleId);

        SysUserRoleView view = new SysUserRoleView();
        view.setId(1L);
        view.setUserId(1L);
        view.setRoleId(roleId);

        when(r2dbcEntityTemplate.select(SysUserRoleEntity.class)).thenReturn(reactiveSelect);
        when(reactiveSelect.matching(any(Query.class))).thenReturn(reactiveSelect);
        when(reactiveSelect.all()).thenReturn(Flux.just(entity));
        when(sysUserRoleViewMapStruct.toViewList(any())).thenReturn(Collections.singletonList(view));

        StepVerifier.create(sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(roleId))
                .expectNext(Collections.singletonList(view))
                .verifyComplete();
    }
}
