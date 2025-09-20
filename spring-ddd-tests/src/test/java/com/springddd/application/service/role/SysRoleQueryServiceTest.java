package com.springddd.application.service.role;

import com.springddd.application.service.role.dto.SysRolePageQuery;
import com.springddd.application.service.role.dto.SysRoleQuery;
import com.springddd.application.service.role.dto.SysRoleView;
import com.springddd.application.service.role.dto.SysRoleViewMapStruct;
import com.springddd.infrastructure.persistence.entity.SysRoleEntity;
import com.springddd.infrastructure.persistence.r2dbc.SysRoleRepository;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SysRoleQueryServiceTest {

    @Mock
    private R2dbcEntityTemplate r2dbcEntityTemplate;

    @Mock
    private SysRoleViewMapStruct sysRoleViewMapStruct;

    @Mock
    private SysRoleRepository sysRoleRepository;

    @InjectMocks
    private SysRoleQueryService sysRoleQueryService;

    private SysRoleEntity mockEntity;
    private SysRoleView mockView;

    @BeforeEach
    void setUp() {
        mockEntity = new SysRoleEntity();
        mockEntity.setId(1L);
        mockEntity.setRoleName("Test Role");
        mockEntity.setRoleCode("TEST_ROLE");
        mockEntity.setRoleDesc("Test Role Description");
        mockEntity.setDataScope(1);
        mockEntity.setDataPermission(null);
        mockEntity.setRoleStatus(true);
        mockEntity.setOwnerStatus(true);
        mockEntity.setDeptId(1L);
        mockEntity.setDeleteStatus(false);
        mockEntity.setVersion(0);

        mockView = new SysRoleView();
        mockView.setId(1L);
        mockView.setRoleName("Test Role");
        mockView.setRoleCode("TEST_ROLE");
        mockView.setRoleDesc("Test Role Description");
        mockView.setDataScope(1);
        mockView.setRoleStatus(true);
        mockView.setOwnerStatus(true);
        mockView.setDeptId(1L);
        mockView.setDeleteStatus(false);
        mockView.setVersion(0);
    }

    @SuppressWarnings("unchecked")
    @Test
    void index_shouldReturnPageResponse() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        query.setRoleName("Test");

        List<SysRoleEntity> entities = Arrays.asList(mockEntity);
        List<SysRoleView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysRoleEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysRoleEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysRoleEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysRoleEntity.class));
        when(sysRoleViewMapStruct.toViewList(entities)).thenReturn(views);

        StepVerifier.create(sysRoleQueryService.index(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.getItems().size());
                    assertEquals(1L, response.getTotal());
                    assertEquals("Test Role", response.getItems().get(0).getRoleName());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void index_shouldReturnEmptyWhenNoData() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);

        ReactiveSelectOperation.ReactiveSelect<SysRoleEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysRoleEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysRoleEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.empty()).when(mockTerminatingSelect).all();
        doReturn(Mono.just(0L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysRoleEntity.class));
        when(sysRoleViewMapStruct.toViewList(Collections.emptyList())).thenReturn(Collections.emptyList());

        StepVerifier.create(sysRoleQueryService.index(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(0, response.getItems().size());
                    assertEquals(0L, response.getTotal());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void recycle_shouldReturnDeletedRoles() {
        SysRolePageQuery query = new SysRolePageQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        mockEntity.setDeleteStatus(true);
        mockView.setDeleteStatus(true);

        List<SysRoleEntity> entities = Arrays.asList(mockEntity);
        List<SysRoleView> views = Arrays.asList(mockView);

        ReactiveSelectOperation.ReactiveSelect<SysRoleEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysRoleEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysRoleEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Flux.just(mockEntity)).when(mockTerminatingSelect).all();
        doReturn(Mono.just(1L)).when(r2dbcEntityTemplate).count(any(Query.class), eq(SysRoleEntity.class));
        when(sysRoleViewMapStruct.toViewList(entities)).thenReturn(views);

        StepVerifier.create(sysRoleQueryService.recycle(query))
                .assertNext(response -> {
                    assertNotNull(response);
                    assertEquals(1, response.getItems().size());
                    assertEquals(1L, response.getTotal());
                    assertEquals(true, response.getItems().get(0).getDeleteStatus());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void getById_shouldReturnRole() {
        Long id = 1L;

        ReactiveSelectOperation.ReactiveSelect<SysRoleEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysRoleEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysRoleEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Mono.just(mockEntity)).when(mockTerminatingSelect).one();
        when(sysRoleViewMapStruct.toView(mockEntity)).thenReturn(mockView);

        StepVerifier.create(sysRoleQueryService.getById(id))
                .assertNext(result -> {
                    assertNotNull(result);
                    assertEquals("Test Role", result.getRoleName());
                    assertEquals("TEST_ROLE", result.getRoleCode());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void getById_shouldReturnEmptyWhenNotFound() {
        Long id = 999L;

        ReactiveSelectOperation.ReactiveSelect<SysRoleEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysRoleEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysRoleEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Mono.empty()).when(mockTerminatingSelect).one();

        StepVerifier.create(sysRoleQueryService.getById(id))
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void getByCode_shouldReturnRole() {
        String code = "TEST_ROLE";

        ReactiveSelectOperation.ReactiveSelect<SysRoleEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysRoleEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysRoleEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Mono.just(mockEntity)).when(mockTerminatingSelect).one();
        when(sysRoleViewMapStruct.toView(mockEntity)).thenReturn(mockView);

        StepVerifier.create(sysRoleQueryService.getByCode(code))
                .assertNext(result -> {
                    assertNotNull(result);
                    assertEquals("Test Role", result.getRoleName());
                    assertEquals("TEST_ROLE", result.getRoleCode());
                })
                .verifyComplete();
    }

    @SuppressWarnings("unchecked")
    @Test
    void getByCode_shouldReturnEmptyWhenNotFound() {
        String code = "NOT_FOUND";

        ReactiveSelectOperation.ReactiveSelect<SysRoleEntity> mockSelect =
                mock(ReactiveSelectOperation.ReactiveSelect.class);
        ReactiveSelectOperation.TerminatingSelect<SysRoleEntity> mockTerminatingSelect =
                mock(ReactiveSelectOperation.TerminatingSelect.class);

        doReturn(mockSelect).when(r2dbcEntityTemplate).select(eq(SysRoleEntity.class));
        doReturn(mockTerminatingSelect).when(mockSelect).matching(any(Query.class));
        doReturn(Mono.empty()).when(mockTerminatingSelect).one();

        StepVerifier.create(sysRoleQueryService.getByCode(code))
                .verifyComplete();
    }

    @Test
    void getAllRole_shouldReturnAllRoles() {
        List<SysRoleEntity> entities = Arrays.asList(mockEntity);
        List<SysRoleView> views = Arrays.asList(mockView);

        when(sysRoleRepository.findAll()).thenReturn(Flux.just(mockEntity));
        when(sysRoleViewMapStruct.toViewList(entities)).thenReturn(views);

        StepVerifier.create(sysRoleQueryService.getAllRole())
                .assertNext(result -> {
                    assertNotNull(result);
                    assertEquals(1, result.size());
                    assertEquals("Test Role", result.get(0).getRoleName());
                })
                .verifyComplete();
    }

    @Test
    void getAllRole_shouldReturnEmptyWhenNoData() {
        when(sysRoleRepository.findAll()).thenReturn(Flux.empty());
        when(sysRoleViewMapStruct.toViewList(Collections.emptyList())).thenReturn(Collections.emptyList());

        StepVerifier.create(sysRoleQueryService.getAllRole())
                .expectNext(Collections.emptyList())
                .verifyComplete();
    }
}
