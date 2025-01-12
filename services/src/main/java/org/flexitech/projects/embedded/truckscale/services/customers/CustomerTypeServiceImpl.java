package org.flexitech.projects.embedded.truckscale.services.customers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dao.customers.CustomerTypeDAO;
import org.flexitech.projects.embedded.truckscale.dto.customers.CustomerTypeDTO;
import org.flexitech.projects.embedded.truckscale.entities.customers.CustomerTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerTypeServiceImpl implements CustomerTypeService {
	
	private final Logger logger = LogManager.getLogger(getClass());
	
	@Autowired
	CustomerTypeDAO customerTypeDAO;

	@Override
	public CustomerTypeDTO getCustomerTypeById(Long id) {
		CustomerTypes c = this.customerTypeDAO.get(id);
		if(CommonValidators.isValidObject(c)) {
			return new CustomerTypeDTO(c);
		}
		return null;
	}

	@Override
	public CustomerTypeDTO manageCustomerType(CustomerTypeDTO dto) {
		if(!CommonValidators.isValidObject(dto)) {
			return null;
		}
		CustomerTypes c = null;
		if(CommonValidators.validLong(dto.getId())) {
			c = this.customerTypeDAO.get(dto.getId());
			c.setUpdatedTime(new Date());
		}else {
			c = new CustomerTypes();
			c.setCreatedTime(new Date());
		}
		
		c.setCode(dto.getCode());
		c.setName(dto.getName());
		c.setStatus(dto.getStatus());
		
		this.customerTypeDAO.saveOrUpdate(c);
		
		return new CustomerTypeDTO(c);
	}

	@Override
	public List<CustomerTypeDTO> getAllCustomerTypes(Integer status) {
		List<CustomerTypes> types = this.customerTypeDAO.getAllCustomerTypes(status);
		if(CommonValidators.validList(types)) {
			return types.stream().map(CustomerTypeDTO::new).collect(Collectors.toList());
		}
		return new ArrayList<CustomerTypeDTO>();
	}

	@Override
	public boolean deleteCustomerType(Long id) {
		try {
			CustomerTypes c = this.customerTypeDAO.get(id);
			if(CommonValidators.isValidObject(c)) {
				this.customerTypeDAO.delete(c);
				return true;
			}
		}catch(Exception e) {
			logger.error("Error on deleting customer types: {}", ExceptionUtils.getStackTrace(e));
		}
		return false;
	}

}
