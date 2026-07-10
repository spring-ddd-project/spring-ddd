package com.springddd.domain.post;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SysPostDomain extends AbstractDomainMask {

    private PostId id;

    private PostBasicInfo postBasicInfo;

    private PostExtendInfo postExtendInfo;

    public void create() {}

    public void update(PostBasicInfo basicInfo, PostExtendInfo extendInfo) {
        this.postBasicInfo = basicInfo;
        this.postExtendInfo = extendInfo;
    }

    public void delete() {
        super.setDeleteStatus(true);
    }

    public void restore() {
        super.setDeleteStatus(false);
    }
}
