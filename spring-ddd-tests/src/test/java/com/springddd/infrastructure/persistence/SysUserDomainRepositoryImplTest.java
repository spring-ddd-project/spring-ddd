package com.springddd.infrastructure.persistence;

import com.springddd.domain.user.SysUserDomain;
import com.springddd.domain.user.UserId;
import com.springddd.infrastructure.persistence.entity.SysUserEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.SysUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SysUserDomainRepositoryImplTest {

    @Mock
    private SysUserRepository sysUserRepository;

    @Mock
    private EntityFactory entityFactory;

    @InjectMocks
    private SysUserDomainRepositoryImpl repository;

    @Test
    @DisplayName("load 应通过 findById 和 entityFactory 返回 domain")
    void load_shouldReturnDomain() {
        UserId userId = new UserId(1L);
        SysUserEntity entity = new SysUserEntity();
        SysUserDomain domain = new SysUserDomain();

        given(sysUserRepository.findById(1L)).willReturn(Mono.just(entity));
        given(entityFactory.createSysUserDomain(entity)).willReturn(domain);

        StepVerifier.create(repository.load(userId))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("load 当记录不存在时应返回空 Mono")
    void load_whenNotFound_shouldReturnEmpty() {
        UserId userId = new UserId(1L);

        given(sysUserRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.load(userId))
                .verifyComplete();
    }

    @Test
    @DisplayName("save 应通过 entityFactory 转换并返回 id")
    void save_shouldReturnId() {
        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(new UserId(1L));
        SysUserEntity entity = new SysUserEntity();
        SysUserEntity savedEntity = new SysUserEntity();
        savedEntity.setId(1L);

        given(entityFactory.createSysUserEntity(domain)).willReturn(entity);
        given(sysUserRepository.save(entity)).willReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete 应调用 deleteById 并返回 Mono<Void>")
    void delete_shouldCallDeleteById() {
        SysUserDomain domain = new SysUserDomain();
        domain.setUserId(new UserId(1L));

        given(sysUserRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.delete(domain))
                .verifyComplete();

        verify(sysUserRepository).deleteById(1L);
    }
}
