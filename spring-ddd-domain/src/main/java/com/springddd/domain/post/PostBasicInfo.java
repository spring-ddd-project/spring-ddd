package com.springddd.domain.post;

import com.springddd.domain.post.exception.PostCodeNullException;
import com.springddd.domain.post.exception.PostNameNullException;
import org.springframework.util.ObjectUtils;

public record PostBasicInfo(String postCode, String postName) {

    public PostBasicInfo {
        if (ObjectUtils.isEmpty(postCode)) {
            throw new PostCodeNullException();
        }
        if (ObjectUtils.isEmpty(postName)) {
            throw new PostNameNullException();
        }
    }
}
