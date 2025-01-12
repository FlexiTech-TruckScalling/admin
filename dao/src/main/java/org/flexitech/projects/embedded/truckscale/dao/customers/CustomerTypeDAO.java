package org.flexitech.projects.embedded.truckscale.dao.customers;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerTypes;

public interface CustomerTypeDAO extends CommonDAO<CustomerTypes, Long> {
	List<CustomerTypes> getAllCustomerTypes(Integer status);
}
