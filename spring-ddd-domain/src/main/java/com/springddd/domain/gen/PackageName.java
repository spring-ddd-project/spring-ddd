package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.PackageNameNullException;
import org.springframework.util.ObjectUtils;

public record PackageName(String value) {

    public PackageName {
        if (ObjectUtils.isEmpty(value)) {
            throw new PackageNameNullException();
        }
    }
}
