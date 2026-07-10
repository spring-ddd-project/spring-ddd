package com.springddd.domain.user;

import com.springddd.domain.user.exception.UserPostPostIdNullException;
import com.springddd.domain.user.exception.UserPostUserIdNullException;
import org.springframework.util.ObjectUtils;

public record UserPostInfo(Long userId, Long postId) {

    public UserPostInfo {
        if (ObjectUtils.isEmpty(userId)) {
            throw new UserPostUserIdNullException();
        }
        if (ObjectUtils.isEmpty(postId)) {
            throw new UserPostPostIdNullException();
        }
    }
}
