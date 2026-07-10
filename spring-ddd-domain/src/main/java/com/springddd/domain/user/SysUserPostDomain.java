package com.springddd.domain.user;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserPostDomain extends AbstractDomainMask {

    private UserPostId id;

    private UserPostInfo userPostInfo;

    public void create() {}

    public void update(UserPostInfo info) {
        this.userPostInfo = info;
    }

    public void delete() {
        super.setDeleteStatus(true);
    }

    public void restore() {
        super.setDeleteStatus(false);
    }
}
