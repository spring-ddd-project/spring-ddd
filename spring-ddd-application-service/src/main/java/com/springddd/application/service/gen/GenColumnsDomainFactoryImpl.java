package com.springddd.application.service.gen;

import com.springddd.domain.gen.*;
import org.springframework.stereotype.Component;

@Component
public class GenColumnsDomainFactoryImpl implements GenColumnsDomainFactory {
    @Override
    public GenColumnsDomain newInstance(InfoId infoId, Prop prop, Table table, Form form, GenColumnsExtendInfo extendInfo) {
        GenColumnsDomain domain = new GenColumnsDomain();
        domain.setInfoId(infoId);
        domain.setProp(prop);
        domain.setTable(table);
        domain.setForm(form);
        domain.setExtendInfo(extendInfo);
        domain.setDeleteStatus(false);
        return domain;
    }
}
