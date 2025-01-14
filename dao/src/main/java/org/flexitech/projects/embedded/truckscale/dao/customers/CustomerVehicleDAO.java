package org.flexitech.projects.embedded.truckscale.dao.customers;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerVehicles;

public interface CustomerVehicleDAO extends CommonDAO<CustomerVehicles, Long> {
	List<CustomerVehicles> getAllCustomerVehicles(Integer status);
	List<CustomerVehicles> getAllCustomerVehicles(Long customerId, Integer status);
	boolean isVehicleNumberAlreadyUserd(String number, Long ignoreId);
}
