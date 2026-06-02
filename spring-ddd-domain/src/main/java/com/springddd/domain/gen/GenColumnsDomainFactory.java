package com.springddd.domain.gen;

public interface GenColumnsDomainFactory {

    GenColumnsDomain newInstance(InfoId infoId, Prop prop, Table table, Form form, I18n i18n, GenColumnsExtendInfo extendInfo);
}
