package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRoleMenuDataScopeView;
import com.springddd.application.service.role.dto.SysRoleMenuDataScopeViewMapStruct;
import com.springddd.infrastructure.persistence.entity.SysRoleMenuDataScopeEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleMenuDataScopeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

class SysRoleMenuDataScopeQueryServiceTest {

    private SysRoleMenuDataScopeRepository sysRoleMenuDataScopeRepository;
    private SysRoleMenuDataScopeViewMapStruct sysRoleMenuDataScopeViewMapStruct;
    private SysRoleMenuDataScopeQueryService service;

    @BeforeEach
    void setUp() {
        sysRoleMenuDataScopeRepository = Mockito.mock(SysRoleMenuDataScopeRepository.class);
        sysRoleMenuDataScopeViewMapStruct = Mockito.mock(SysRoleMenuDataScopeViewMapStruct.class);
        service = new SysRoleMenuDataScopeQueryService(sysRoleMenuDataScopeRepository, sysRoleMenuDataScopeViewMapStruct);
    }

    @Test
    void listByRoleIdShouldReturnViews() {
        SysRoleMenuDataScopeEntity entity = new SysRoleMenuDataScopeEntity();
        entity.setId(1L);
        entity.setRoleId(1L);
        entity.setMenuId(10L);
        SysRoleMenuDataScopeView view = new SysRoleMenuDataScopeView();
        view.setId(1L);

        Mockito.when(sysRoleMenuDataScopeRepository.findByRoleIdAndDeleteStatusFalse(1L)).thenReturn(Flux.just(entity));
        Mockito.when(sysRoleMenuDataScopeViewMapStruct.toViews(List.of(entity))).thenReturn(List.of(view));

        StepVerifier.create(service.listByRoleId(1L))
                .assertNext(result -> {
                    org.junit.jupiter.api.Assertions.assertEquals(1, result.size());
                    org.junit.jupiter.api.Assertions.assertEquals(1L, result.get(0).getId());
                })
                .verifyComplete();
    }

    @Test
    void findByRoleIdAndMenuIdShouldReturnView() {
        SysRoleMenuDataScopeEntity entity = new SysRoleMenuDataScopeEntity();
        entity.setId(1L);
        entity.setRoleId(1L);
        entity.setMenuId(10L);
        SysRoleMenuDataScopeView view = new SysRoleMenuDataScopeView();
        view.setId(1L);

        Mockito.when(sysRoleMenuDataScopeRepository.findByRoleIdAndMenuIdAndDeleteStatusFalse(1L, 10L)).thenReturn(Flux.just(entity));
        Mockito.when(sysRoleMenuDataScopeViewMapStruct.toView(entity)).thenReturn(view);

        StepVerifier.create(service.findByRoleIdAndMenuId(1L, 10L))
                .assertNext(result -> org.junit.jupiter.api.Assertions.assertEquals(1L, result.getId()))
                .verifyComplete();
    }
}
