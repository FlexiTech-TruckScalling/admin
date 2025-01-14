package org.flexitech.projects.embedded.truckscale.services.customers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.customers.CustomerDAO;
import org.flexitech.projects.embedded.truckscale.dao.customers.CustomerTypeDAO;
import org.flexitech.projects.embedded.truckscale.dao.customers.CustomerVehicleDAO;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerDTO;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerSearchDTO;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerTypes;
import org.flexitech.projects.embedded.truckscale.entities.customers.Customers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	CustomerVehicleDAO customerVehicleDAO;

	@Autowired
	CustomerTypeDAO customerTypeDAO;

	private final Logger logger = LogManager.getLogger(getClass());

	@Override
	public CustomerDTO getCustomerById(Long id) {
		Customers c = this.customerDAO.get(id);
		if (CommonValidators.isValidObject(c)) {
			return new CustomerDTO(c);
		}
		return null;
	}

	@Override
	public CustomerDTO manageCustomer(CustomerDTO dto) {
		if (!CommonValidators.isValidObject(dto)) {
			return null;
		}

		Customers c = null;

		if (CommonValidators.validLong(dto.getId())) {
			c = this.customerDAO.get(dto.getId());
			c.setUpdatedTime(new Date());
		} else {
			c = new Customers();
			c.setCreatedTime(new Date());
		}

		c.setName(dto.getName());
		c.setCode(dto.getCode());
		c.setPhoneNumber(dto.getPhoneNumber());
		c.setAddress(dto.getAddress());
		c.setStatus(dto.getStatus());

		if (CommonValidators.validList(dto.getCustomerTypeIds())) {
			Set<CustomerTypes> newCustomerTypes = new HashSet<>();
			for (Long typeId : dto.getCustomerTypeIds()) {
				CustomerTypes type = this.customerTypeDAO.get(typeId);
				if (CommonValidators.isValidObject(type)) {
					newCustomerTypes.add(type);
				} else {
					logger.warn("No customer type found!");
				}
			}

			// Synchronize existing and new customer types
			Set<CustomerTypes> existingCustomerTypes = c.getCustomerTypes();
			if (existingCustomerTypes == null) {
				existingCustomerTypes = new HashSet<>();
			}

			existingCustomerTypes.removeIf(existingType -> !newCustomerTypes.contains(existingType));

			for (CustomerTypes type : newCustomerTypes) {
				if (!existingCustomerTypes.contains(type)) {
					existingCustomerTypes.add(type);
				}
			}

			c.setCustomerTypes(existingCustomerTypes);
		} else {
			c.setCustomerTypes(null);
		}

		this.customerDAO.saveOrUpdate(c);

		return new CustomerDTO(c);
	}

	@Override
	public List<CustomerDTO> getAllCustomers(Integer status) {
		List<Customers> customers = this.customerDAO.getAllCustomer(status);
		if(CommonValidators.validList(customers)) {
			return customers.stream().map(CustomerDTO::new).collect(Collectors.toList());
		}
		return new ArrayList<CustomerDTO>();
	}

	@Override
	public boolean deleteCustomer(Long id) {
		try {
			
			Customers c = this.customerDAO.get(id);
			if(CommonValidators.isValidObject(c)) {
				this.customerDAO.delete(c);
				return true;
			}
			
		}catch (Exception e) {
			logger.error("Error on deleting customer: {}", ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

	@Override
	public List<CustomerDTO> searchCustomers(CustomerSearchDTO searchDTO) {
	    List<Customers> customers = this.customerDAO.searchCustomer(searchDTO);

	    if (CommonValidators.validList(customers)) {
	        Integer totalCount = this.customerDAO.countCustomer(searchDTO);
	        if (totalCount != null && totalCount > 0) {
	            int pageCount = (int) Math.ceil((totalCount * 1.0) / Math.max(searchDTO.getLimit(), 1));  // Avoid divide by zero
	            searchDTO.setTotalRecords(totalCount);
	            searchDTO.setPageCount(pageCount);
	        } else {
	            searchDTO.setTotalRecords(0);
	            searchDTO.setPageCount(0);
	        }

	        return customers.stream()
	                        .map(CustomerDTO::new)
	                        .collect(Collectors.toList());
	    }

	    return Collections.emptyList();
	}

}
