package com.springddd.domain.gen.state;

import com.springddd.domain.gen.GenAggregateDomain;

public interface AggregateState {
    void delete(GenAggregateDomain domain);
    void restore(GenAggregateDomain domain);
}













































