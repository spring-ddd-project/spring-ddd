package com.springddd.domain.post;

import com.springddd.domain.post.exception.PostStatusNullException;
import com.springddd.domain.post.exception.SortOrderNullException;
import org.springframework.util.ObjectUtils;

public record PostExtendInfo(Long parentId, Integer sortOrder, Boolean postStatus) {

    public PostExtendInfo {
        if (ObjectUtils.isEmpty(sortOrder)) {
            throw new SortOrderNullException();
        }
        if (ObjectUtils.isEmpty(postStatus)) {
            throw new PostStatusNullException();
        }
    }
}
