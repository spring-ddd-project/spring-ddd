package com.springddd.domain.gen;

import com.springddd.domain.gen.exception.FormComponentNullException;
import com.springddd.domain.gen.exception.FormRequiredNullException;
import com.springddd.domain.gen.exception.FormVisibleNullException;
import org.springframework.util.ObjectUtils;

public record Form(Byte formComponent, Boolean formVisible, Boolean formRequired) {

    public Form {
        if (ObjectUtils.isEmpty(formComponent)) {
            throw new FormComponentNullException();
        }
        if (ObjectUtils.isEmpty(formVisible)) {
            throw new FormVisibleNullException();
        }
        if (ObjectUtils.isEmpty(formRequired)) {
            throw new FormRequiredNullException();
        }
    }
}
