package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.ObjectNameNullException;
import com.springddd.domain.gen.exception.ObjectTypeNullException;
import com.springddd.domain.gen.exception.ValueObjectNullException;
import org.springframework.util.ObjectUtils;

public record GenAggregateValueObject(String objectName, String objectValue, Byte objectType) {

    public GenAggregateValueObject {
        if (ObjectUtils.isEmpty(objectName)) {
            throw new ObjectNameNullException();
        }
        if (ObjectUtils.isEmpty(objectValue)) {
            throw new ValueObjectNullException();
        }
        if (ObjectUtils.isEmpty(objectType)) {
            throw new ObjectTypeNullException();
        }
    }
}
