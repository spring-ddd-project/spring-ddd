package com.springddd.domain.gen.state;

import com.springddd.domain.gen.GenAggregateDomain;

public class ActiveState implements AggregateState {
    @Override
    public void delete(GenAggregateDomain domain) {
        domain.setDeleteStatus(true);
        domain.setState(new DeletedState());
    }

    @Override
    public void restore(GenAggregateDomain domain) {
        // Already active
    }
}












































