package com.springddd.domain.gen;

public interface GenColumnsDomainFactory {

    GenColumnsDomain newInstance(InfoId infoId, Prop prop, Table table, Form form, GenColumnsExtendInfo extendInfo);
}
