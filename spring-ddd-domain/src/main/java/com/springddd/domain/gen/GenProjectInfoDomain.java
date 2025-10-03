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

    public com.springddd.domain.gen.memento.GenProjectInfoMemento saveToMemento() {
        return new com.springddd.domain.gen.memento.GenProjectInfoMemento(this.projectInfo, this.extendInfo);
    }

    public void restoreFromMemento(com.springddd.domain.gen.memento.GenProjectInfoMemento memento) {
        this.projectInfo = memento.getProjectInfo();
        this.extendInfo = memento.getExtendInfo();
    }
}
