package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenColumnBindDomainTest {

    @Test
    void testCreate() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.create();
    }

    @Test
    void testUpdate() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        GenColumnBindBasicInfo info = new GenColumnBindBasicInfo("varchar", "String", (byte) 1, (byte) 1);
        domain.update(info);
        assertThat(domain.getBasicInfo().columnType()).isEqualTo("varchar");
    }

    @Test
    void testDelete() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
    }

    @Test
    void testRestore() {
        GenColumnBindDomain domain = new GenColumnBindDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
    }
}
