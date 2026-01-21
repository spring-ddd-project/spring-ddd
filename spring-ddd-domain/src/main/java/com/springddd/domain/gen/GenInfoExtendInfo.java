package com.springddd.domain.gen;

public record GenInfoExtendInfo(Boolean propValueObject,
                                String propColumnKey,
                                String propColumnName,
                                String propColumnType,
                                String propColumnComment,
                                String propJavaEntity,
                                String propJavaType,
                                Long propDictId,
                                Boolean tableVisible,
                                Boolean tableOrder,
                                Boolean tableFilter,
                                Integer tableFilterComponent,
                                Integer tableFilterType,
                                Integer formComponent,
                                Boolean formVisible,
                                Boolean formRequired) {
}
