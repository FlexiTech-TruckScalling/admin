package org.flexitech.projects.embedded.truckscale.dao.customers;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dao.common.CommonDAO;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerSearchDTO;
import org.flexitech.projects.embedded.truckscale.entities.customers.Customers;

public interface CustomerDAO extends CommonDAO<Customers, Long> {
	List<Customers> getAllCustomer(Integer status);
	List<Customers> searchCustomer(CustomerSearchDTO searchDTO);
	Integer countCustomer(CustomerSearchDTO searchDTO);
}
