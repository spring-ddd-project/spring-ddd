package com.springddd.infrastructure.persistence;

import com.springddd.domain.dept.DeptId;
import com.springddd.domain.dept.SysDeptDomain;
import com.springddd.infrastructure.persistence.entity.SysDeptEntity;
import com.springddd.infrastructure.persistence.factory.EntityFactory;
import com.springddd.infrastructure.persistence.r2dbc.SysDeptRepository;
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
class SysDeptDomainRepositoryImplTest {

    @Mock
    private SysDeptRepository sysDeptRepository;

    @Mock
    private EntityFactory entityFactory;

    @InjectMocks
    private SysDeptDomainRepositoryImpl repository;

    @Test
    @DisplayName("load 应通过 findById 和 entityFactory 返回 domain")
    void load_shouldReturnDomain() {
        DeptId deptId = new DeptId(1L);
        SysDeptEntity entity = new SysDeptEntity();
        SysDeptDomain domain = new SysDeptDomain();

        given(sysDeptRepository.findById(1L)).willReturn(Mono.just(entity));
        given(entityFactory.createSysDeptDomain(entity)).willReturn(domain);

        StepVerifier.create(repository.load(deptId))
                .expectNext(domain)
                .verifyComplete();
    }

    @Test
    @DisplayName("load 当记录不存在时应返回空 Mono")
    void load_whenNotFound_shouldReturnEmpty() {
        DeptId deptId = new DeptId(1L);

        given(sysDeptRepository.findById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.load(deptId))
                .verifyComplete();
    }

    @Test
    @DisplayName("save 应通过 entityFactory 转换并返回 id")
    void save_shouldReturnId() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setDeptIdentifier(new DeptId(1L));
        SysDeptEntity entity = new SysDeptEntity();
        SysDeptEntity savedEntity = new SysDeptEntity();
        savedEntity.setId(1L);

        given(entityFactory.createSysDeptEntity(domain)).willReturn(entity);
        given(sysDeptRepository.save(entity)).willReturn(Mono.just(savedEntity));

        StepVerifier.create(repository.save(domain))
                .expectNext(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("delete 应调用 deleteById 并返回 Mono<Void>")
    void delete_shouldCallDeleteById() {
        SysDeptDomain domain = new SysDeptDomain();
        domain.setDeptIdentifier(new DeptId(1L));

        given(sysDeptRepository.deleteById(1L)).willReturn(Mono.empty());

        StepVerifier.create(repository.delete(domain))
                .verifyComplete();

        verify(sysDeptRepository).deleteById(1L);
    }
}
