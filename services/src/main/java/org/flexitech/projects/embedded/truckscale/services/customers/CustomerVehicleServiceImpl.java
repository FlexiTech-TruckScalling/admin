package org.flexitech.projects.embedded.truckscale.services.customers;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.customers.CustomerDAO;
import org.flexitech.projects.embedded.truckscale.dao.customers.CustomerVehicleDAO;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerVehicleDTO;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerVehicles;
import org.flexitech.projects.embedded.truckscale.entities.customers.Customers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerVehicleServiceImpl implements CustomerVehicleService {

	@Autowired
	CustomerVehicleDAO customerVehicleDAO;

	@Autowired
	CustomerDAO customerDAO;

	@Override
	public CustomerVehicleDTO getCustomerVehicleById(Long id) {
		CustomerVehicles v = this.customerVehicleDAO.get(id);
		if (CommonValidators.isValidObject(v)) {
			return new CustomerVehicleDTO(v);
		}
		return null;
	}

	@Override
	public CustomerVehicleDTO manageCustomerVehicle(CustomerVehicleDTO dto) throws Exception {
		if (!CommonValidators.isValidObject(dto)) {
			throw new Exception("Reference object is empty!");
		}
		if (!CommonValidators.validLong(dto.getCustomerId())) {
			throw new Exception("Please select a customer!");
		}

		CustomerVehicles v = null;

		if (CommonValidators.validLong(dto.getId())) {
			v = this.customerVehicleDAO.get(dto.getId());
			v.setUpdatedTime(new Date());
		} else {
			v = new CustomerVehicles();
			v.setCreatedTime(new Date());
		}

		Customers c = this.customerDAO.get(dto.getCustomerId());

		v.setCustomer(c);
		v.setNumber(dto.getNumber());
		v.setWeight(dto.getWeight());
		v.setStatus(dto.getStatus());

		this.customerVehicleDAO.saveOrUpdate(v);

		CustomerVehicleDTO updated = new CustomerVehicleDTO(v);
		updated.setCustomerId(dto.getCustomerId());
		return updated;
	}

	@Override
	public List<CustomerVehicleDTO> getAllCustomerVehicle(Long customerId, Integer status) throws Exception {
		if (!CommonValidators.validLong(customerId)) {
			throw new Exception("Please select a customer!");
		}
		List<CustomerVehicles> vehicles = this.customerVehicleDAO.getAllCustomerVehicles(customerId, status);
		if (CommonValidators.validList(vehicles)) {
			return vehicles.stream().map(CustomerVehicleDTO::new).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public boolean deleteCustomerVehicle(Long id, Long customerId) throws Exception {
		if (!CommonValidators.validLong(customerId)) {
			throw new Exception("Please select a customer!");
		}
		if (!CommonValidators.validLong(id)) {
			throw new Exception("Resource not available!");
		}
		CustomerVehicles v = this.customerVehicleDAO.get(id);
		if (CommonValidators.isValidObject(v)) {
			if (!customerId.equals(v.getCustomer().getId())) {
				throw new Exception("Customer vehicle is not associated with this customer!");
			}else {
				this.customerVehicleDAO.delete(v);
				return true;
			}
		}
		return false;
	}

}
