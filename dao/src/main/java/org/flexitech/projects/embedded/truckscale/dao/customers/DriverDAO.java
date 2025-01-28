package org.flexitech.projects.embedded.truckscale.dao.customers;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.customers.Drivers;

public interface DriverDAO extends CommonDAO<Drivers, Long> {
	List<Drivers> getAllDrivers(Integer status);
	Drivers getDriverByName(String name);
}
