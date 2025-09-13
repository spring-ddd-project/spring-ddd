package com.springddd.domain.gen;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenColumnsDomain extends AbstractDomainMask {

    private ColumnsId id;

    private InfoId infoId;

    private Prop prop;

    private Table table;

    private Form form;

    private I18n i18n;

    private GenColumnsExtendInfo extendInfo;

    public void create() {}

    public void update(Prop prop, Table table, Form form, I18n i18n, GenColumnsExtendInfo extendInfo) {
        this.prop = prop;
        this.table = table;
        this.form = form;
        this.i18n = i18n;
        this.extendInfo = extendInfo;
    }

    public void delete() {
        super.setDeleteStatus(true);
    }
}
