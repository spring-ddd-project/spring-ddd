package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.I18nEnNullException;
import org.springframework.util.ObjectUtils;

public record I18n(String en, String locale) {

    public I18n {
        if (ObjectUtils.isEmpty(en)) {
            throw new I18nEnNullException();
        }
    }
}
