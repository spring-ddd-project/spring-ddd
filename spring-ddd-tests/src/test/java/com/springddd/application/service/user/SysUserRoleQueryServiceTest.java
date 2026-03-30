package com.springddd.application.service.user;

import com.springddd.application.service.user.dto.SysUserRoleQuery;
import com.springddd.application.service.user.dto.SysUserRoleView;
import com.springddd.application.service.user.dto.SysUserRoleViewMapStruct;
import com.springddd.infrastructure.persistence.entity.SysUserRoleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SysUserRoleQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysUserRoleViewMapStruct sysUserRoleViewMapStruct;

    private SysUserRoleQueryService sysUserRoleQueryService;

    @BeforeEach
    void setUp() {
        sysUserRoleQueryService = new SysUserRoleQueryService(r2dbcEntityTemplate, sysUserRoleViewMapStruct);
    }

    @Test
    void queryLinkUserAndRole_shouldReturnRoleViews_whenUserHasRoles() {
        Long userId = 1L;

        SysUserRoleEntity entity1 = new SysUserRoleEntity();
        entity1.setId(1L);
        entity1.setUserId(userId);
        entity1.setRoleId(10L);

        SysUserRoleEntity entity2 = new SysUserRoleEntity();
        entity2.setId(2L);
        entity2.setUserId(userId);
        entity2.setRoleId(20L);

        List<SysUserRoleView> views = Arrays.asList(
                new SysUserRoleView() {{ setId(1L); setUserId(userId); setRoleId(10L); }},
                new SysUserRoleView() {{ setId(2L); setUserId(userId); setRoleId(20L); }}
        );

        when(r2dbcEntityTemplate.select(SysUserRoleEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.just(entity1, entity2));
        when(sysUserRoleViewMapStruct.toViewList(any())).thenReturn(views);

        reactor.core.publisher.Mono<List<SysUserRoleView>> result = sysUserRoleQueryService.queryLinkUserAndRole(userId);

        StepVerifier.create(result)
                .expectNextMatches(roleList -> roleList.size() == 2)
                .verifyComplete();
    }

    @Test
    void queryLinkUserAndRole_shouldReturnEmptyList_whenUserHasNoRoles() {
        Long userId = 1L;

        when(r2dbcEntityTemplate.select(SysUserRoleEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.empty());
        when(sysUserRoleViewMapStruct.toViewList(any())).thenReturn(Collections.emptyList());

        reactor.core.publisher.Mono<List<SysUserRoleView>> result = sysUserRoleQueryService.queryLinkUserAndRole(userId);

        StepVerifier.create(result)
                .expectNextMatches(roleList -> roleList.isEmpty())
                .verifyComplete();
    }

    @Test
    void queryLinkUserAndRoleByRoleId_shouldReturnUserRoleViews_whenRoleHasUsers() {
        Long roleId = 10L;

        SysUserRoleEntity entity1 = new SysUserRoleEntity();
        entity1.setId(1L);
        entity1.setUserId(1L);
        entity1.setRoleId(roleId);

        List<SysUserRoleView> views = Collections.singletonList(
                new SysUserRoleView() {{ setId(1L); setUserId(1L); setRoleId(roleId); }}
        );

        when(r2dbcEntityTemplate.select(SysUserRoleEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.just(entity1));
        when(sysUserRoleViewMapStruct.toViewList(any())).thenReturn(views);

        reactor.core.publisher.Mono<List<SysUserRoleView>> result = sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(roleId);

        StepVerifier.create(result)
                .expectNextMatches(roleList -> roleList.size() == 1)
                .verifyComplete();
    }

    @Test
    void queryLinkUserAndRoleByRoleId_shouldReturnEmptyList_whenRoleHasNoUsers() {
        Long roleId = 10L;

        when(r2dbcEntityTemplate.select(SysUserRoleEntity.class).matching(any(Query.class)).all())
                .thenReturn(Flux.empty());
        when(sysUserRoleViewMapStruct.toViewList(any())).thenReturn(Collections.emptyList());

        reactor.core.publisher.Mono<List<SysUserRoleView>> result = sysUserRoleQueryService.queryLinkUserAndRoleByRoleId(roleId);

        StepVerifier.create(result)
                .expectNextMatches(roleList -> roleList.isEmpty())
                .verifyComplete();
    }
}
