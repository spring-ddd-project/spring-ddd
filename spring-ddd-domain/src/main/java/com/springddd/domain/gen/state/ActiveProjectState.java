package com.springddd.domain.gen.state;

import com.springddd.domain.gen.GenProjectInfoDomain;

public class ActiveProjectState implements ProjectState {
    @Override
    public void delete(GenProjectInfoDomain domain) {
        domain.setDeleteStatus(true);
        domain.setState(new DeletedProjectState());
    }
}






