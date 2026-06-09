package org.flexitech.projects.embedded.truckscale.services.superadmin;

import java.util.List;

import org.flexitech.projects.embedded.truckscale.dto.superadminuser.SuperAdminCustomerDTO;

public interface SupAdminCustService {
	SuperAdminCustomerDTO getCustomerById(Long id);
	SuperAdminCustomerDTO manageCustomer(SuperAdminCustomerDTO dto);
	List<SuperAdminCustomerDTO> getAllCustomers(Integer status);
	//List<CustomerDTO> searchCustomers(CustomerSearchDTO searchDTO);
	boolean deleteSupAdminCustomer(Long id);
}
