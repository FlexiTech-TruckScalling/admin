package org.flexitech.projects.embedded.truckscale.services.customers;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerTypeDTO;

public interface CustomerTypeService {
	CustomerTypeDTO getCustomerTypeById(Long id);
	CustomerTypeDTO manageCustomerType(CustomerTypeDTO dto);
	List<CustomerTypeDTO> getAllCustomerTypes(Integer status);
	boolean deleteCustomerType(Long id);
	List<CustomerTypeDTO> getCustomerTypeByCustomerId(Long customerId) throws Exception;
}
