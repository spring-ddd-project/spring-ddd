package com.springddd.domain.gen;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GenColumnsDomainTest {

    private GenColumnsDomain createDomain() {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setId(new ColumnsId(1L));
        domain.setInfoId(new InfoId(1L));
        return domain;
    }

    private Prop createProp() {
        return new Prop("key", "name", "varchar", "comment", "String", "Entity");
    }

    private Table createTable() {
        return new Table(true, true, false, (byte) 0, (byte) 0);
    }

    private Form createForm() {
        return new Form((byte) 1, true, false);
    }

    private I18n createI18n() {
        return new I18n("Name", "名称");
    }

    @Test
    void testCreate() {
        GenColumnsDomain domain = createDomain();
        domain.create();
    }

    @Test
    void testUpdate() {
        GenColumnsDomain domain = createDomain();
        Prop prop = createProp();
        Table table = createTable();
        Form form = createForm();
        I18n i18n = createI18n();
        GenColumnsExtendInfo ext = new GenColumnsExtendInfo(1L, (byte) 1);
        domain.update(prop, table, form, i18n, ext);

        assertThat(domain.getProp().propColumnName()).isEqualTo("name");
        assertThat(domain.getTable().tableVisible()).isTrue();
        assertThat(domain.getForm().formVisible()).isTrue();
        assertThat(domain.getI18n().en()).isEqualTo("Name");
        assertThat(domain.getExtendInfo().propDictId()).isEqualTo(1L);
    }

    @Test
    void testUpdateWithNullThrows() {
        GenColumnsDomain domain = createDomain();
        assertThatThrownBy(() -> domain.update(null, createTable(), createForm(), createI18n(), new GenColumnsExtendInfo(1L, (byte) 1)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> domain.update(createProp(), null, createForm(), createI18n(), new GenColumnsExtendInfo(1L, (byte) 1)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> domain.update(createProp(), createTable(), null, createI18n(), new GenColumnsExtendInfo(1L, (byte) 1)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> domain.update(createProp(), createTable(), createForm(), null, new GenColumnsExtendInfo(1L, (byte) 1)))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> domain.update(createProp(), createTable(), createForm(), createI18n(), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testDelete() {
        GenColumnsDomain domain = createDomain();
        domain.delete();
        assertThat(domain.getDeleteStatus()).isTrue();
    }
}
