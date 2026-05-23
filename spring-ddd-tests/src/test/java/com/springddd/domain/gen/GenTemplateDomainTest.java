package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GenTemplateDomainTest {

    @Test
    void testCreate() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.create();
    }

    @Test
    void testUpdate() {
        GenTemplateDomain domain = new GenTemplateDomain();
        TemplateInfo info = new TemplateInfo("test", "content");
        domain.update(info);
        assertThat(domain.getTemplateInfo().templateName()).isEqualTo("test");
        assertThat(domain.getTemplateInfo().templateContent()).isEqualTo("content");
    }

    @Test
    void testDelete() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
    }

    @Test
    void testRestore() {
        GenTemplateDomain domain = new GenTemplateDomain();
        domain.setDeleteStatus(true);
        domain.restore();
        assertThat(domain.getDeleteStatus()).isFalse();
    }
}
