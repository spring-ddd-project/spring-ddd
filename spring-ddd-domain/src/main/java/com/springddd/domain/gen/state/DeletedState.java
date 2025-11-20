package com.springddd.domain.gen.state;

import com.springddd.domain.gen.GenAggregateDomain;

public class DeletedState implements AggregateState {
    @Override
    public void delete(GenAggregateDomain domain) {
        // Already deleted
    }

    @Override
    public void restore(GenAggregateDomain domain) {
        domain.setDeleteStatus(false);
        domain.setState(new ActiveState());
    }
}


