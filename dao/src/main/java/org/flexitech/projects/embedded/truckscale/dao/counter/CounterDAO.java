package org.flexitech.projects.embedded.truckscale.dao.counter;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.counters.Counters;

public interface CounterDAO extends CommonDAO<Counters, Long> {
	List<Counters> getAllCounterByStatus(Integer status);
}
