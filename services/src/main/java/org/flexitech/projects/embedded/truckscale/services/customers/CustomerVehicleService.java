package org.flexitech.projects.embedded.truckscale.services.customers;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerVehicleDTO;

public interface CustomerVehicleService {
	CustomerVehicleDTO getCustomerVehicleById(Long id);
	CustomerVehicleDTO manageCustomerVehicle(CustomerVehicleDTO dto) throws Exception;
	List<CustomerVehicleDTO> getAllCustomerVehicle(Long customerId, Integer status) throws Exception;
	boolean deleteCustomerVehicle(Long id, Long customerId) throws Exception;
	boolean isVehicleNumberAlreadyUserd(String number, Long ignoreId);
}
