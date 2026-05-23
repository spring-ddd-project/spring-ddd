package com.springddd.infrastructure.persistence;

import com.springddd.domain.menu.MenuId;
import com.springddd.domain.menu.SysMenuDomain;
import com.springddd.infrastructure.persistence.entity.SysMenuEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.SysMenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SysMenuDomainRepositoryImplTest {

    @Mock
    private SysMenuRepository sysMenuRepository;

    @Mock
    private EntityFactory entityFactory;

    @InjectMocks
    private SysMenuDomainRepositoryImpl repository;

    @Test
    @DisplayName("load 应通过 findById 和 entityFactory 返回 domain")
    void load_shouldReturnDomain() {
        MenuId menuId = new MenuId(1L);
        SysMenuEntity entity = new SysMenuEntity();
        SysMenuDomain domain = new SysMenuDomain();

        given(sysMenuRepository.findById(1L)).willReturn(Mono.just(entity));
        given(entityFactory.createSysMenuDomain(entity)).willReturn(domain);

        StepVerifier.create(repository.load(menuId))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("load 当记录不存在时应返回空 Mono")
    void load_whenNotFound_shouldReturnEmpty() {
        MenuId menuId = new MenuId(1L);

        given(sysMenuRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.load(menuId))
                .verifyComplete();
    }

    @Test
    @DisplayName("save 应通过 entityFactory 转换并返回 id")
    void save_shouldReturnId() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new MenuId(1L));
        SysMenuEntity entity = new SysMenuEntity();
        SysMenuEntity savedEntity = new SysMenuEntity();
        savedEntity.setId(1L);

        given(entityFactory.createSysMenuEntity(domain)).willReturn(entity);
        given(sysMenuRepository.save(entity)).willReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete 应调用 deleteById 并返回 Mono<Void>")
    void delete_shouldCallDeleteById() {
        SysMenuDomain domain = new SysMenuDomain();
        domain.setMenuId(new MenuId(1L));

        given(sysMenuRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.delete(domain))
                .verifyComplete();

        verify(sysMenuRepository).deleteById(1L);
    }
}
