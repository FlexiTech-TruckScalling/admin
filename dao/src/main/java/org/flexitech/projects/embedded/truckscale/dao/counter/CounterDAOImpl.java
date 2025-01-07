package org.flexitech.projects.embedded.truckscale.dao.counter;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAOImpl;
import org.flexitech.projects.embedded.truckscale.entities.counters.Counters;
import org.springframework.stereotype.Repository;

@Repository
public class CounterDAOImpl extends CommonDAOImpl<Counters, Long> implements CounterDAO {

}
