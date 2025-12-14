package com.springddd.domain.gen.memento;

import com.springddd.domain.gen.ProjectInfo;
import com.springddd.domain.gen.GenProjectInfoExtendInfo;
import lombok.Getter;

@Getter
public class GenProjectInfoMemento {
    private final ProjectInfo projectInfo;
    private final GenProjectInfoExtendInfo extendInfo;

    public GenProjectInfoMemento(ProjectInfo projectInfo, GenProjectInfoExtendInfo extendInfo) {
        this.projectInfo = projectInfo;
        this.extendInfo = extendInfo;
    }
}















