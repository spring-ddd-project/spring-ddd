package com.springddd.domain.gen;

public record GenInfoExtendInfo(Byte propValueObject,
                                String propColumnKey,
                                String propColumnName,
                                String propColumnType,
                                String propColumnComment,
                                String propJavaEntity,
                                String propJavaType,
                                Long propDictId,
                                Byte tableVisible,
                                Byte tableOrder,
                                Byte tableFilter,
                                Integer tableFilterComponent,
                                Integer tableFilterType,
                                Integer formComponent,
                                Byte formVisible,
                                Byte formRequired) {
}
