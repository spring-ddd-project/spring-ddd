package com.springddd.domain.dept;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SysDeptDomainRepositoryTest {

    @Test
    void repository_shouldExtendDomainRepository() {
        assertTrue(DomainRepository.class.isAssignableFrom(SysDeptDomainRepository.class));
    }

    @Test
    void repository_shouldBeInterface() {
        assertTrue(SysDeptDomainRepository.class.isInterface());
    }

    @Test
    void repository_shouldHaveCorrectTypeParameters() {
        SysDeptDomainRepository repository = new SysDeptDomainRepository() {
            @Override
            public reactor.core.publisher.Mono<SysDeptDomain> load(DeptId aggregateRootId) {
                return reactor.core.publisher.Mono.empty();
            }

            @Override
            public reactor.core.publisher.Mono<Long> save(SysDeptDomain aggregateRoot) {
                return reactor.core.publisher.Mono.just(1L);
            }
        };

        assertNotNull(repository);
    }
}
