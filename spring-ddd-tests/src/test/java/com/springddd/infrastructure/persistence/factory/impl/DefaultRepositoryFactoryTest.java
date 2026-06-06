package com.springddd.infrastructure.persistence.factory.impl;

import com.springddd.domain.dept.SysDeptDomainRepository;
import com.springddd.domain.dict.SysDictDomainRepository;
import com.springddd.domain.dict.SysDictItemDomainRepository;
import com.springddd.domain.gen.*;
import com.springddd.domain.menu.SysMenuDomainRepository;
import com.springddd.domain.role.SysRoleDomainRepository;
import com.springddd.domain.user.SysUserDomainRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class DefaultRepositoryFactoryTest {

    @Mock private GenAggregateDomainRepository genAggregateDomainRepository;
    @Mock private SysUserDomainRepository sysUserDomainRepository;
    @Mock private SysDeptDomainRepository sysDeptDomainRepository;
    @Mock private SysDictDomainRepository sysDictDomainRepository;
    @Mock private SysDictItemDomainRepository sysDictItemDomainRepository;
    @Mock private SysMenuDomainRepository sysMenuDomainRepository;
    @Mock private SysRoleDomainRepository sysRoleDomainRepository;
    @Mock private GenColumnBindDomainRepository genColumnBindDomainRepository;
    @Mock private GenColumnsDomainRepository genColumnsDomainRepository;
    @Mock private GenProjectInfoDomainRepository genProjectInfoDomainRepository;
    @Mock private GenTemplateDomainRepository genTemplateDomainRepository;

    private DefaultRepositoryFactory factory;

    @BeforeEach
    void setUp() {
        factory = new DefaultRepositoryFactory(
                genAggregateDomainRepository,
                sysUserDomainRepository,
                sysDeptDomainRepository,
                sysDictDomainRepository,
                sysDictItemDomainRepository,
                sysMenuDomainRepository,
                sysRoleDomainRepository,
                genColumnBindDomainRepository,
                genColumnsDomainRepository,
                genProjectInfoDomainRepository,
                genTemplateDomainRepository
        );
    }

    @Test
    @DisplayName("getGenAggregateDomainRepository 应返回正确的仓库实例")
    void getGenAggregateDomainRepository_shouldReturnCorrectRepository() {
        assertThat(factory.getGenAggregateDomainRepository()).isSameAs(genAggregateDomainRepository);
    }

    @Test
    @DisplayName("getSysUserDomainRepository 应返回正确的仓库实例")
    void getSysUserDomainRepository_shouldReturnCorrectRepository() {
        assertThat(factory.getSysUserDomainRepository()).isSameAs(sysUserDomainRepository);
    }

    @Test
    @DisplayName("getSysDeptDomainRepository 应返回正确的仓库实例")
    void getSysDeptDomainRepository_shouldReturnCorrectRepository() {
        assertThat(factory.getSysDeptDomainRepository()).isSameAs(sysDeptDomainRepository);
    }

    @Test
    @DisplayName("getSysDictDomainRepository 应返回正确的仓库实例")
    void getSysDictDomainRepository_shouldReturnCorrectRepository() {
        assertThat(factory.getSysDictDomainRepository()).isSameAs(sysDictDomainRepository);
    }

    @Test
    @DisplayName("getSysDictItemDomainRepository 应返回正确的仓库实例")
    void getSysDictItemDomainRepository_shouldReturnCorrectRepository() {
        assertThat(factory.getSysDictItemDomainRepository()).isSameAs(sysDictItemDomainRepository);
    }

    @Test
    @DisplayName("getSysMenuDomainRepository 应返回正确的仓库实例")
    void getSysMenuDomainRepository_shouldReturnCorrectRepository() {
        assertThat(factory.getSysMenuDomainRepository()).isSameAs(sysMenuDomainRepository);
    }

    @Test
    @DisplayName("getSysRoleDomainRepository 应返回正确的仓库实例")
    void getSysRoleDomainRepository_shouldReturnCorrectRepository() {
        assertThat(factory.getSysRoleDomainRepository()).isSameAs(sysRoleDomainRepository);
    }

    @Test
    @DisplayName("getGenColumnBindDomainRepository 应返回正确的仓库实例")
    void getGenColumnBindDomainRepository_shouldReturnCorrectRepository() {
        assertThat(factory.getGenColumnBindDomainRepository()).isSameAs(genColumnBindDomainRepository);
    }

    @Test
    @DisplayName("getGenColumnsDomainRepository 应返回正确的仓库实例")
    void getGenColumnsDomainRepository_shouldReturnCorrectRepository() {
        assertThat(factory.getGenColumnsDomainRepository()).isSameAs(genColumnsDomainRepository);
    }

    @Test
    @DisplayName("getGenProjectInfoDomainRepository 应返回正确的仓库实例")
    void getGenProjectInfoDomainRepository_shouldReturnCorrectRepository() {
        assertThat(factory.getGenProjectInfoDomainRepository()).isSameAs(genProjectInfoDomainRepository);
    }

    @Test
    @DisplayName("getGenTemplateDomainRepository 应返回正确的仓库实例")
    void getGenTemplateDomainRepository_shouldReturnCorrectRepository() {
        assertThat(factory.getGenTemplateDomainRepository()).isSameAs(genTemplateDomainRepository);
    }
}
