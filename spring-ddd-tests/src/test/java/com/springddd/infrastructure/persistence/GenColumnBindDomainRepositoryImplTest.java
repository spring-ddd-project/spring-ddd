package com.springddd.infrastructure.persistence;

import com.springddd.domain.gen.ColumnBindId;
import com.springddd.domain.gen.GenColumnBindBasicInfo;
import com.springddd.domain.gen.GenColumnBindDomain;
import com.springddd.domain.gen.GenColumnBindDomainRepository;
import com.springddd.infrastructure.persistence.entity.GenColumnBindEntity;
import com.springddd.infrastructure.persistence.r2dbc.GenColumnBindRepository;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GenColumnBindDomainRepositoryImplTest {

    @Test
    void shouldHaveGenColumnBindDomainRepositoryImplClass() {
        assertNotNull(GenColumnBindDomainRepositoryImpl.class);
    }

    @Test
    void shouldImplementGenColumnBindDomainRepository() {
        assertTrue(GenColumnBindDomainRepository.class.isAssignableFrom(GenColumnBindDomainRepositoryImpl.class));
    }

    @Test
    void shouldHaveRequiredArgsConstructor() {
        assertNotNull(GenColumnBindDomainRepositoryImpl.class.getConstructors().length > 0);
    }

    @Test
    void shouldHaveLoadMethod() {
        assertNotNull(GenColumnBindDomainRepositoryImpl.class.getDeclaredMethod("load", ColumnBindId.class));
    }

    @Test
    void shouldHaveSaveMethod() {
        assertNotNull(GenColumnBindDomainRepositoryImpl.class.getDeclaredMethod("save", GenColumnBindDomain.class));
    }

    @Test
    void genColumnBindDomain_shouldHaveBasicInfo() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        GenColumnBindBasicInfo basicInfo = new GenColumnBindBasicInfo("String", "String", "input", "string");
        domain.setBasicInfo(basicInfo);
        assertEquals(basicInfo, domain.getBasicInfo());
    }
}
