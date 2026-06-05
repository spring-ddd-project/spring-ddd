package com.springddd.domain.gen;

import com.springddd.domain.AbstractDomainMask;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GenProjectInfoDomain extends AbstractDomainMask implements Cloneable {

    private InfoId id;

    private ProjectInfo projectInfo;

    private GenProjectInfoExtendInfo extendInfo;

    @Override
    public GenProjectInfoDomain clone() {
        try {
            GenProjectInfoDomain clone = (GenProjectInfoDomain) doClone();
            if (this.id != null) clone.setId(new InfoId(this.id.value()));
            if (this.projectInfo != null) {
                ProjectInfo info = new ProjectInfo(this.projectInfo.tableName(), this.projectInfo.packageName(), this.projectInfo.className(), this.projectInfo.moduleName(), this.projectInfo.projectName());
                clone.setProjectInfo(info);
            }
            if (this.extendInfo != null) {
                GenProjectInfoExtendInfo ext = new GenProjectInfoExtendInfo(this.extendInfo.requestName(), this.extendInfo.projectVersion());
                clone.setExtendInfo(ext);
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    private com.springddd.domain.gen.state.ProjectState state;

    public void setState(com.springddd.domain.gen.state.ProjectState state) {
        this.state = state;
    }

    public void create() {
    }

    public void update(ProjectInfo projectInfo, GenProjectInfoExtendInfo extendInfo) {
        this.projectInfo = projectInfo;
        this.extendInfo = extendInfo;
    }

    public void delete() {
        if (state == null) state = getDeleteStatus() ? new com.springddd.domain.gen.state.DeletedProjectState() : new com.springddd.domain.gen.state.ActiveProjectState();
        state.delete(this);
    }
}
