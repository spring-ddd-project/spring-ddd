package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.TableNameNullException;
import org.springframework.util.ObjectUtils;

public record TableName(String value) {

    public TableName {
        if (ObjectUtils.isEmpty(value)) {
            throw new TableNameNullException();
        }
    }
}
