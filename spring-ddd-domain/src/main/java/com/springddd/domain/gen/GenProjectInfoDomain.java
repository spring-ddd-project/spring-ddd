package com.springddd.domain.gen;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenProjectInfoDomain extends AbstractDomainMask {

    private InfoId id;

    private ProjectInfo projectInfo;

    private GenProjectInfoExtendInfo extendInfo;

    public void create() {
    }

    public void update(ProjectInfo projectInfo, GenProjectInfoExtendInfo extendInfo) {
        this.projectInfo = projectInfo;
        this.extendInfo = extendInfo;
    }

    public void delete() {
        super.setDeleteStatus(true);
    }
}
