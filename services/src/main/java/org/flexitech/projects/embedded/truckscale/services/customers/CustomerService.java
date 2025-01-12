package org.flexitech.projects.embedded.truckscale.services.customers;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerDTO;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerSearchDTO;

public interface CustomerService {
	CustomerDTO getCustomerById(Long id);
	CustomerDTO manageCustomer(CustomerDTO dto);
	List<CustomerDTO> getAllCustomers(Integer status);
	List<CustomerDTO> searchCustomers(CustomerSearchDTO searchDTO);
	boolean deleteCustomer(Long id);
}
